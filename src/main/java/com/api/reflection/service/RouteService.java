package com.api.reflection.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service("routeService")
@Slf4j
public class RouteService {

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Autowired
    ResultDetailMapper resultDetailMapper;

    @Autowired
    private XmanDataValidParamInfoMapper xmanDataValidParamInfoMapper;

    @Autowired
    ApiRouteHelperService apiRouteHelperService;

    @Autowired
    private JsonMappingService jsonMappingService;

    private static final Integer expireTime = 60 * 60 * 24;

    private static ThreadLocal<XmanDataInterfaceDO> interfaceDOThreadLocal = new ThreadLocal<>();

    private ReentrantLock lock = new ReentrantLock() ;

    /**
     * 异常统一捕获, 检查是否设置RequestNo 保存日志
     *
     * @param routerRequestDTO
     * @param routeName
     * @return
     */
    public RouteResponseDTO executeRoute(RouterRequestDTO routerRequestDTO, String routeName) {
        RouteResponseDTO routeResponseDTO;
        long start = System.currentTimeMillis();
        try {
            routeResponseDTO = callInterface(routerRequestDTO, routeName);
        } catch (Exception e) {
            log.warn("executeApi:", e);
            routeResponseDTO = new RouteResponseDTO(SYSTEM_ERR.getCode());
        }
        if (StringUtils.isEmpty(routeResponseDTO.getRequestNo())) {
            routeResponseDTO.setRequestNo(routerRequestDTO.getRequestNo());
        }
        long end = System.currentTimeMillis();
        apiRouteHelperService.saveResultToDB(routerRequestDTO.getUserId(),routerRequestDTO.getRequestInfo(),
                interfaceDOThreadLocal.get(), routeResponseDTO, String.valueOf(end - start), routeName);
        interfaceDOThreadLocal.remove();
        return routeResponseDTO;

    }

    /**
     * 入口 做初始校验
     *
     * @param routerRequestDTO
     * @param routeName
     * @return
     */
    public RouteResponseDTO callInterface(RouterRequestDTO routerRequestDTO, String routeName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String requestInfo = routerRequestDTO.getRequestInfo();
        log.info("传入的参数:" + requestInfo);
        //根据路由名称查询该路由下所有的接口和各接口权重
        long start0 = System.currentTimeMillis();
        String userId = routerRequestDTO.getUserId();
        List<XmanRouteDetailDO> interfaces = PowerCacheUtil.getArray("xman_route" + routeName + userId, XmanRouteDetailDO.class);
        log.info("缓存耗时..." + (System.currentTimeMillis() - start0));
        if (interfaces == null) {
            interfaces = routeMapper.findInterfacesByRouteName(routeName, userId);
            if (interfaces == null || interfaces.size() <= 0) {
                return new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
            }
            PowerCacheUtil.setArray("xman_route" + routeName + userId, interfaces, expireTime);
        }
        log.info("耗时测试..." + (System.currentTimeMillis() - start0));

        return getInterfaceByWeight(interfaces, routerRequestDTO, routeName);
    }

    /**
     * 执行 权重选择
     *
     * @param interfaces
     * @param routerRequestDTO
     * @param routeName
     * @return
     */
    private RouteResponseDTO getInterfaceByWeight(List<XmanRouteDetailDO> interfaces, RouterRequestDTO routerRequestDTO, String routeName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        XmanRouteDetailDO routeDetailDO;
        switch (interfaces.size()) {
            case 0:
                log.info("can't find interface by router {}", routeName);
                return new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
            case 1:
                routeDetailDO = interfaces.get(0);
                XmanDataInterfaceDO interfaceDO = interfaceMapper.findInterfacesById(routeDetailDO.getInterfaceId());
                interfaceDOThreadLocal.set(interfaceDO);//设置本地变量
                //接口不存在返回
                if (null == interfaceDO) {
                    log.info("can't find only interface by router {}", routeName);
                    return new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
                }
                return executeQuery(interfaceDO, routerRequestDTO, routeName);
            default:
                return failover(interfaces, routerRequestDTO, routeName);
        }
    }

    /**
     * 执行 错误重试
     *
     * @return
     */
    private RouteResponseDTO failover(List<XmanRouteDetailDO> interfaces, RouterRequestDTO routerRequestDTO, String routeName) {
        RouteResponseDTO routeResponseDTO = null;
        XmanRouteDetailDO routeDetailDO;
        //用来做权重选择的列表
        for (int i = 0; i < interfaces.size(); i++) {
            try {
                log.info("执行第{}次权重选择", i + 1);
                routeDetailDO = smoothWeightedRoundRobin(interfaces);
                if (null == routeDetailDO) {
                    log.info("权重配置问题, 请联系管理员修改! ");
                    routeResponseDTO = new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
                    break;
                }
                XmanDataInterfaceDO interfaceDO2 = interfaceMapper.findInterfacesById(routeDetailDO.getInterfaceId());
                //接口不存在返回
                if (null == interfaceDO2) {
                    log.info("can't find multi interface by router {}", routeName);
                    routeResponseDTO = new RouteResponseDTO(SERVICE_NOT_EXIST.getCode());
                    break;
                }
                routeResponseDTO = executeQuery(interfaceDO2, routerRequestDTO, routeName);
                String errorCode = routeResponseDTO.getErrCode();
                //执行错误重试的条件
                if (!StringUtils.equals(errorCode, SOURCE_FORBIDDEN.getCode())
                        && !StringUtils.equals(errorCode, SOURCE_NOT_AVIABLE.getCode())
                        && !StringUtils.equals(errorCode, SOURCE_ERR.getCode())
                        && !StringUtils.equals(errorCode, SOURCE_TIMEOUT.getCode())) {
                    interfaceDOThreadLocal.set(interfaceDO2);//设置线程本地变量
                    break;
                }
            } catch (Exception e) {
                log.warn("执行错误重试报错: ", e);
                log.info("...........将会执行重试..............");
            }
        }
        return routeResponseDTO;

    }

    /**
     * 执行 运营商查询
     *
     * @param interfaceDO
     * @param routerRequestDTO
     * @param routeName
     * @return
     */
    private RouteResponseDTO executeQuery(XmanDataInterfaceDO interfaceDO, RouterRequestDTO routerRequestDTO, String routeName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String requestInfo = routerRequestDTO.getRequestInfo();
        //mock开关
        int mockSwitch = interfaceDO.getMockSwitch();
        String mockResult = interfaceDO.getMock();
        String methodName = interfaceDO.getMethodName();
        String callMode = interfaceDO.getCallMode();

        Map<String, Object> bodyParam = BeanUtil.json2map(requestInfo);
        Object service = SpringUtil.getBean(interfaceDO.getClassName());
        log.info("Router-" + callMode + ", 服务: " + service.getClass().getTypeName() + ", 方法: " + methodName + ", 参数: " + routerRequestDTO.getRequestInfo());
        RouteResponseDTO result;
        //jsonMapping方式调用获取
        if ("jsonMapping".equals(callMode)) {
            result = jsonMappingService.callByJsonMappingService((ProviderSendInterface) service, interfaceDO, JSONObject.parseObject(routerRequestDTO.getRequestInfo()));
            //反射方式调用获取
        } else {
            if (mockSwitch == 1 && StringUtils.isNotEmpty(mockResult)) {
                return JSONObject.parseObject(mockResult, RouteResponseDTO.class);
            }
            result = apiRouteHelperService.callByReflect(service, methodName, bodyParam);
        }

        if (result == null) {
            return new RouteResponseDTO(SOURCE_FORBIDDEN.getCode());
        }

        result.setRequestNo(String.valueOf(routerRequestDTO.getRequestNo()));
        return result;
    }

    /**
     * 权重选择策略（随机）
     *
     * @param routList
     * @return
     */
    private XmanRouteDetailDO chooseByWeight(List<XmanRouteDetailDO> routList) {
        if (CollectionUtils.isEmpty(routList)) {
            return null;
        }
        Integer weightSum = 0;

        for (XmanRouteDetailDO rt : routList) {
            weightSum += rt.getWeight();
        }
        //权重之和为零, 所有权重都置为10
        boolean isZero = false;
        if (weightSum <= 0) {
            isZero = true;
            weightSum = routList.size() * 10;
        }
        // n in [0, weightSum)
        Integer random = RandomUtils.nextInt(weightSum);
        Integer m = 0;
        for (XmanRouteDetailDO rt : routList) {
            int currentWeight = rt.getWeight();
            if (isZero) {
                currentWeight = 10;
            }
            if (m <= random && random < m + currentWeight) {
                log.info("权重总和{}, 权重选择随机数{},当前接口权重 {} ", weightSum, random, currentWeight);
                log.debug("This Random router chanelId is {}", rt.getId());
                return rt;
            }
            m += currentWeight;
        }
        return null;
    }

    /**
     * 平滑加权轮询
     * @param routList
     * @return
     */
    public XmanRouteDetailDO smoothWeightedRoundRobin(List<XmanRouteDetailDO> routList){
        try {
            lock.lock();
            return this.selectInner(routList) ;
        }finally {
            lock.unlock();
        }
    }

    private XmanRouteDetailDO selectInner(List<XmanRouteDetailDO> nodeList){
        int totalWeight = 0 ;
        XmanRouteDetailDO maxNode = null ;
        int maxWeight = 0 ;
        for (int i = 0; i < nodeList.size(); i++) {
            XmanRouteDetailDO n = nodeList.get(i);
            totalWeight += n.getWeight() ;
            // 每个节点的当前权重要加上原始的权重
            n.setCurrentWeight(n.getCurrentWeight() + n.getWeight());
            // 保存当前权重最大的节点
            if (maxNode == null || maxWeight < n.getCurrentWeight() ) {
                maxNode = n ;
                maxWeight = n.getCurrentWeight() ;
            }
        }
        // 被选中的节点权重减掉总权重
        maxNode.setCurrentWeight(maxNode.getCurrentWeight() - totalWeight);
        PowerCacheUtil.setArray("xman_route" + nodeList.get(0).getRouteName() + nodeList.get(0).getUserId(), nodeList, expireTime);
        return maxNode ;
    }

    /**
     * 按超时策略调用接口
     */
    @SuppressWarnings("unused")
    private String callByTimeout(RouterRequestDTO routerRequestDTO) {
        //TODO
        return null;
    }



    public void resetInterfacesRedis(RouterRequestDTO routerRequestDTO, String routeName) {
        String userId = routerRequestDTO.getUserId();
        List<XmanRouteDetailDO> interfaces = routeMapper.findInterfacesByRouteName(routeName, userId);
        PowerCacheUtil.setArray("xman_route" + routeName + userId, interfaces, expireTime);
    }


    public void deleteInterfacesRedis(RouterRequestDTO routerRequestDTO, String routeName) {
        String userId = routerRequestDTO.getUserId();
        String cacheKey = "xman_route" + routeName + userId;
        PowerCacheUtil.delete(cacheKey);
    }


    public void resetValidRedis(RouterRequestDTO routerRequestDTO, String routeName) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", routeName);
        String cacheKey = "data-serviceName-" + routeName;

        List<XmanDataValidParamInfoDO> list = PowerCacheUtil.getArray(cacheKey, XmanDataValidParamInfoDO.class);
        if (list == null || list.size() == 0) {
            list = xmanDataValidParamInfoMapper.findListByServiceName(map);
            PowerCacheUtil.setArray(cacheKey, list, expireTime);
        }
    }


    public void deleteValidRedis(RouterRequestDTO routerRequestDTO, String routeName) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", routeName);
        String cacheKey = "data-serviceName-" + routeName;
        PowerCacheUtil.delete(cacheKey);
    }


}
