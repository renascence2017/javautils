package com.api.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Component
@EnableScheduling
@Slf4j
public class StatisticsScheduled {

    @Autowired
    ResultDetailMapper resultDetailMapper;

    @Autowired
    StatisticsResultMapper statisticsResultMapper;

    @Autowired
    TimePriceRecordService timePriceRecordService;

    @Autowired
    private TimePriceRecordMapper timePriceRecordMapper;

    @Autowired
    InterfaceService interfaceService;

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Resource
    private IPowerCacheAgent iPowerCacheAgent;

    @Autowired
    TimePriceCurrentMapper timePriceCurrentMapper;

    private static final String POWER_CACHE_KEY = "*********";

    //@Scheduled(cron = "0 0/1 * * * ?") //每两分钟执行一次
    @Scheduled(cron = "0 0 1 * * ?") //凌晨一点运行
    public void statistics() {
        log.info("statistics 开始");
        BaseResponse<TryLockResult> result = iPowerCacheAgent.tryLock(POWER_CACHE_KEY, 2 * 60 * 60);
        //获锁成功开始处理
        if (null != result && result.getRc().getCode() == 200) {
            log.info("statistics  获锁成功, 开始执行");
            processDay(null);
            processMonth(null);
        }
        iPowerCacheAgent.releaseLock(POWER_CACHE_KEY);
    }

    //预约修改价格
    //@Scheduled(cron = "0 0/1 * * * ?") //每两分钟执行一次
    @Scheduled(cron = "0 5 0 * * ? ") //凌晨零点5分运行
    public void appointment() {
        appointmentProcess(null);
    }

    public void appointmentProcess(String today) {
        if (StringUtils.isEmpty(today)) {
            today = DateUtil.yyyyMMdd(new Date());
        }
        log.info("执行预约改价:" + today);
        List<TimePriceRecord> list = timePriceRecordMapper.selectList(Wrappers.<TimePriceRecord>lambdaQuery()
                .eq(TimePriceRecord::getBeginDate, today).orderByDesc(TimePriceRecord::getBeginDate));
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                TimePriceRecord timePriceRecord = list.get(i);
                Interface interfaceEntity = new Interface()
                        .setId(timePriceRecord.getInterfaceId())
                        .setChargingMode(timePriceRecord.getChargingMode())
                        .setFixCharge(timePriceRecord.getFixCharge())
                        .setTieredCharge(timePriceRecord.getTieredCharge());
                int count = interfaceMapper.updateById(interfaceEntity);
                log.info("预约改价的接口:" + timePriceRecord.getInterfaceId() + ",count : " + count);
            }
        }

    }

    public void processDay(String day) {
        if (StringUtils.isEmpty(day)) {
            day = DateUtil.yyyyMMdd(new Date());
        }
        String month = day.substring(1, 6);
        /*日统计*/
        statisticsResultMapper.delete(new QueryWrapper<StatisticsResult>().lambda().eq(StatisticsResult::getDay, day));
        statisticsResultMapper.providerDayDetail(day);//供应商维度日详细
    }

    public void processMonth(String month) {
        if (StringUtils.isEmpty(month)) {
            month = DateUtil.yyyyMMdd(new Date()).substring(1, 6);
        }
        /*月统计*/
        statisticsResultMapper.delete(new QueryWrapper<StatisticsResult>().lambda().eq(StatisticsResult::getMonth, month));
        statisticsResultMapper.providerMonthsDetail(month);//供应商维度月详细
        statisticsResultMapper.providerMonthsTotal(month);//供应商维度月汇总
    }


    //统计月份的所有时间价格
    public void calTimePricePerMonth(String month) {
        //如果传进来的月份为空, 统计当月
        if (StringUtils.isEmpty(month)) {
            month = DateUtil.yyyyMMdd(new Date()).substring(1, 6);
        }
        //根据传入的日期获取第一天, 最后一天
        String monthFirstDay = month + "01";
        String monthLastDay = DateUtil.yyyyMMddLastDay(DateUtil.stringToDate(month + "01", DateUtil.dayFormat));
        //删除之前记录
        timePriceCurrentMapper.delete(new QueryWrapper<TimePriceCurrent>().lambda().eq(TimePriceCurrent::getMonth, month));
        timePriceCurrentMapper.delete(new QueryWrapper<TimePriceCurrent>().lambda().le(TimePriceCurrent::getDate, monthLastDay).ge(TimePriceCurrent::getDate, monthFirstDay));
        //当月所有interface的时间价格
        List<TimePriceRecord> list = timePriceRecordMapper.queryAllMonthPrice(0, month);
        //interfaceId为键, 保存到map中
        Map<Long, List<TimePriceRecord>> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            TimePriceRecord timePriceRecord = list.get(i);
            Long interfaceId = timePriceRecord.getInterfaceId();
            //新增
            if (CollectionUtils.isEmpty(map.get(interfaceId))) {
                map.put(interfaceId, new ArrayList<>(Arrays.asList(timePriceRecord)));
                //添加, 一个接口多个时间价格
            } else {
                List list2 = map.get(interfaceId);
                list2.add(timePriceRecord);
                map.put(interfaceId, list2);
            }
        }
        //遍历map获取真实的时间价格
        Set<Map.Entry<Long, List<TimePriceRecord>>> set = map.entrySet();
        for (Map.Entry<Long, List<TimePriceRecord>> ee : set) {
            Long interfaceId = ee.getKey();
            List<TimePriceRecord> list2 = ee.getValue();
            //接口一个月只有一个价格,插入数据库
            if (list2.size() == 1) {
                TimePriceRecord timePriceRecord2 = list2.get(0);
                TimePriceCurrent timePriceCurrent = new TimePriceCurrent().setMonth(month)
                        .setInterfaceId(interfaceId).setFee(calFee(timePriceRecord2, month))
                        .setChargingMode(timePriceRecord2.getChargingMode())
                        .setChargingStandard(timePriceRecord2.getChargingStandard());
                timePriceCurrentMapper.insert(timePriceCurrent);
                //接口一个月有多个价格
            } else {
                for (int i = 0; i < list2.size(); i++) {
                    TimePriceRecord timePriceRecord2 = list2.get(i);
                    //当月阶段开始
                    String beginDateBegin = timePriceRecord2.getBeginDate();
                    //当月阶段结束, 最后一个结束为月份的最后一天
                    String beginDateEnd;
                    //最后一个阶段, 获取月份最后一天,并且加1(方便后边阶段的遍历)
                    if (i == (list2.size()-1)) {
                        beginDateEnd = String.valueOf(new Integer(monthLastDay) + 1);
                        //其他阶段获取下个阶段的开始时间
                    } else {
                        beginDateEnd = list2.get(i + 1).getBeginDate();
                    }
                    //日期转换成整型, 遍历每个阶段, 获取日价格并插入到数据库
                    for (int j = new Integer(beginDateBegin); j < new Integer(beginDateEnd); j++) {
                        TimePriceCurrent timePriceCurrent = new TimePriceCurrent().setDate(String.valueOf(j))
                                .setInterfaceId(interfaceId).setFee(calFee(timePriceRecord2, month))
                                .setChargingMode(timePriceRecord2.getChargingMode())
                                .setChargingStandard(timePriceRecord2.getChargingStandard());
                        timePriceCurrentMapper.insert(timePriceCurrent);
                    }
                }
            }

        }
    }

    //获取费用 阶梯费用计算方式为当月的查询查得数量所在阶梯, 当月有多个阶梯费用, 也是计算当月所有查询查得数量所在阶梯
    private double calFee(TimePriceRecord timePriceRecord, String month) {
        /**计费功能*/
        double cost = 0;
        //计费标准:1查得收费,0查询收费
        String chargingStandard = timePriceRecord.getChargingStandard();
        //计费模式:1阶梯计价,0固定计价
        String chargingMode = timePriceRecord.getChargingMode();
        //阶梯费用 格式: [{"from":"范围起始","to":"范围终止","charge":"价格"},{"from":"范围起始","to":"范围终止","charge":"价格"}]
        String tieredCharge = timePriceRecord.getTieredCharge();
        //阶梯费用逻辑
        if (StringUtils.equals("1", chargingMode)) {
            JSONArray jsonArray = JSONArray.parseArray(tieredCharge);
            int queryCount;
            //查询成功数量
            if ("0".equals(chargingStandard)) {
                queryCount = resultDetailMapper.slectQueryResult(timePriceRecord.getInterfaceId(), month);
                //查得成功数量
            } else {
                queryCount = resultDetailMapper.slectQuerySuccess(timePriceRecord.getInterfaceId(), month);
            }
            //确定所在阶梯获取阶梯价格
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int from = jsonObject.getIntValue("from");
                int to = jsonObject.getIntValue("to");
                if (queryCount >= from && queryCount <= to) {
                    cost = jsonObject.getDouble("charge");
                    //价格已确定, 退出循环
                    break;
                }
            }
            //固定费用直接赋值
        } else {
            cost = timePriceRecord.getFixCharge().doubleValue();
        }
        return cost;
    }


}

