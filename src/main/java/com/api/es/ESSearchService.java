package com.api.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.retry.annotation.Retryable;

import java.util.*;

@Data
@Component
public class ESSearchService {

    public static final String SUCCESS = "success";
    public static final String ERROR_MSG = "errorMsg";
    @Value("${es.serviceUrl}")
    private String serviceUrl = "************";
    @Value("${es.indexName}")
    private String esIndexName = "**********";


    private String readerIndexes;
    private static OkHttpClient client = new OkHttpClient();
    private static MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");

    /**
     * 批量插入
     * 429， 503错误，sleep一下重试
     *
     * @param documents
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public boolean bulkInsert(List<ESMessageDTO> documents) throws Exception {
        String writerIndexes = "search4" + esIndexName + "_current_writer";
        ESWriterDTO param = new ESWriterDTO();
        param.setIndexes(Lists.newArrayList(writerIndexes));
        param.setTypes(Lists.newArrayList(esIndexName));
        param.setDocuments(documents);
        log.info("入参 ： {} ", JSON.toJSONStringWithDateFormat(param, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONStringWithDateFormat(param, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        Request request = new Request.Builder()
                .url(serviceUrl + "/bulkInsert/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return false;
        }
        log.info("请求结果：{}", str);
        JSONObject res = JSON.parseObject(str);
        boolean success = res.getBoolean(SUCCESS);
        if (!success) {
            log.info("errorMsg = {}", res.getString(ERROR_MSG));
        }
        return success;
    }

    /**
     * 批量更新
     * TODO 429， 503错误，sleep一下重试
     *
     * @param documents
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public boolean bulkUpdate(List<ESMessageDTO> documents) throws Exception {
        String writerIndexes = "search4" + esIndexName + "_current_writer";
        ESWriterDTO param = new ESWriterDTO();
        param.setIndexes(Lists.newArrayList(writerIndexes));
        param.setTypes(Lists.newArrayList(esIndexName));
        param.setDocuments(documents);
        log.info("入参 ： {} ", JSON.toJSONStringWithDateFormat(param, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONStringWithDateFormat(param, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));

        Request request = new Request.Builder()
                .url(serviceUrl + "/bulkUpdate/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return false;
        }
        log.info("请求结果：{}", str);
        JSONObject res = JSON.parseObject(str);
        boolean success = res.getBoolean(SUCCESS);
        if (!success) {
            log.info("errorMsg = {}", res.getString(ERROR_MSG));
        }
        return success;
    }

    /**
     * 游标分页查询
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public ESSearchResult scroll(Map<String, Object> query, String scrollId, int size) throws Exception {
        ESSearchParamDto esSearchParamDto = new ESSearchParamDto();
        esSearchParamDto.getIndexes().add("search4" + esIndexName + "_current_reader");
        esSearchParamDto.getTypes().add(esIndexName);
        esSearchParamDto.setQuery("gmt_created:[\"" + query.get("timeStart") + "T00:00:00.000+0800\" TO \"" +
                query.get("timeEnd") + "T23:59:59.999+0800\"] ");
        if (null != query.get("taskId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND task_id:" + query.get("taskId"));
        }
        if (null != query.get("bacthNo") && StringUtils.isNotBlank(query.get("bacthNo") + "")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND bacth_no:" + query.get("bacthNo"));
        }
        if (null != query.get("customId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND custom_id:" + query.get("customId"));
        }
        if (null != query.get("phone") && StringUtils.isNotBlank(query.get("phone") + "")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND send_obj:" + query.get("phone"));
        }
        if (null != query.get("templateId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND template_id:" + query.get("templateId"));
        }
        if (null != query.get("signatureId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND signature_id:" + query.get("signatureId"));
        }
//        if (null != query.get("sendStatus")) {
//            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND send_status:" + query.get("sendStatus"));
//        }
        if (null != query.get("sendStatus")) {
            //这里做了转化
            //receipt_status 回执状态 FAIL-成功 SUCCESS-失败,UNKNOWN-未回执
            // 发送状态 1 未知 2 成功 3 失败
            if (Objects.equals(1, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:UNKNOWN");
            }
            if (Objects.equals(2, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:SUCCESS");
            }
            if (Objects.equals(3, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:FAIL");
            }
        }
        esSearchParamDto.setScrollId(scrollId);
        esSearchParamDto.setSize(size);
        Map<String, String> sort = Maps.newHashMap();
        sort.put("gmt_created", "desc");
        esSearchParamDto.setSort(sort);
        log.info("入参 ： {} ", JSON.toJSONStringWithDateFormat(esSearchParamDto, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONStringWithDateFormat(esSearchParamDto, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        Request request = new Request.Builder()
                .url(serviceUrl + "/scroll/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String resultYX = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", resultYX);
            return null;
        }
        log.info("请求结果：{}", resultYX);

        ESSearchResult esSearchResultYX = JSONObject.parseObject(resultYX, new TypeReference<ESSearchResult<ESMessageDTO>>() {
        });

        return esSearchResultYX;

    }


    /**
     * 简单查询
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public ESSearchResult searchByPage(Map<String, Object> query, int from, int size) throws Exception {
        ESSearchParamDto esSearchParamDto = new ESSearchParamDto();
        esSearchParamDto.getIndexes().add("search4" + esIndexName + "_current_reader");
        esSearchParamDto.getTypes().add(esIndexName);
        esSearchParamDto.setQuery("gmt_created:[\"" + query.get("timeStart") + "T00:00:00.000+0800\" TO \"" +
                query.get("timeEnd") + "T23:59:59.999+0800\"] ");
        if (null != query.get("taskId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND task_id:" + query.get("taskId"));
        }
        if (null != query.get("batchNo") && StringUtils.isNotBlank(query.get("batchNo") + "")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND batch_no:" + query.get("batchNo"));
        }
        if (null != query.get("customId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND custom_id:" + query.get("customId"));
        }
        if (null != query.get("phone") && StringUtils.isNotBlank(query.get("phone") + "")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND send_obj:" + query.get("phone"));
        }
        if (null != query.get("templateId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND template_id:" + query.get("templateId"));
        }
        if (null != query.get("signatureId")) {
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND signature_id:" + query.get("signatureId"));
        }
        //提交状态
        if (null != query.get("submitStatus")) {//receipt_status
            esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND NOT send_status:" + query.get("submitStatus"));
        }
        if (null != query.get("sendStatus")) {
            //这里做了转化
            //receipt_status 回执状态 FAIL-成功 SUCCESS-失败,UNKNOWN-未回执
            // 发送状态 1 未知 2 成功 3 失败
            if (Objects.equals(1, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:UNKNOWN");
            }
            if (Objects.equals(2, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:SUCCESS");
            }
            if (Objects.equals(3, query.get("sendStatus"))) {
                esSearchParamDto.setQuery(esSearchParamDto.getQuery() + " AND receipt_status:FAIL");
            }
        }
        esSearchParamDto.setFrom(from);
        esSearchParamDto.setSize(size);
        Map<String, String> sort = Maps.newHashMap();
        sort.put("gmt_created", "desc");
        esSearchParamDto.setSort(sort);
        log.info("入参 ： {} ", JSON.toJSONStringWithDateFormat(esSearchParamDto, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONStringWithDateFormat(esSearchParamDto, "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800"));
        Request request = new Request.Builder()
                .url(serviceUrl + "/search/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String resultYX = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", resultYX);
            return null;
        }
        log.info("请求结果：{}", resultYX);

        ESSearchResult esSearchResultYX = JSONObject.parseObject(resultYX, new TypeReference<ESSearchResult<ESMessageDTO>>() {
        });

        return esSearchResultYX;

    }

    /**
     * sql查询 总数
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public TaskCountDTO countSqlByCustomId(long customId, long taskId, String batchNo) throws Exception {

        StringBuilder sql = new StringBuilder("select *");
        sql.append(" from search4" + esIndexName + "_current_reader ");
        sql.append(" where custom_id = " + customId +
                " and task_id = " + taskId +
                " and batch_no = " + batchNo);
        sql.append(" group by receipt_status");
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql.toString());
        log.info("入参 ： {} ", JSON.toJSONString(sqlMap));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(sqlMap));
        Request request = new Request.Builder()
                .url(serviceUrl + "/sql/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return null;
        }
        log.info("请求结果：{}", str);

        TaskCountDTO taskCountDTO = JSON.parseObject(str, TaskCountDTO.class);
        return taskCountDTO;

    }

    /**
     * sql查询 总数
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public CunstomCountDTO countSql(String customId, String stime, String etime) throws Exception {

        StringBuilder sql = new StringBuilder("select *");
        sql.append(" from search4" + esIndexName + "_current_reader ");
        sql.append(" where custom_id = " + customId +
                " and gmt_created >= '" + stime + "T00:00:00.000+0800' " +
                " and gmt_created <= '" + etime + "T23:59:59.999+0800' ");
        sql.append(" group by custom_id");
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql.toString());
        log.info("入参 ： {} ", JSON.toJSONString(sqlMap));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(sqlMap));
        Request request = new Request.Builder()
                .url(serviceUrl + "/sql/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return null;
        }
        log.info("请求结果：{}", str);

        CunstomCountDTO cunstomCountDTO = JSON.parseObject(str, CunstomCountDTO.class);
        return cunstomCountDTO;

    }

    /**
     * sql查询 汇总统计
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public EsTaskIdResultDTO sumSqlByTask(String stime, String etime) throws Exception {
        StringBuilder sql = new StringBuilder("select " +
                "sum(custom_split_count) custom_bill_sum, " +
                "sum(supplier_split_count) supplier_bill_sum ");
        sql.append(" from search4" + esIndexName + "_current_reader ");
        sql.append(" where gmt_created >= '" + stime + "T00:00:00.000+0800' " +
                "and gmt_created <= '" + etime + "T23:59:59.999+0800'");
        sql.append(" group by task_id,template_id,carrier,receipt_status");
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql.toString());
        log.info("入参 ： {} ", JSON.toJSONString(sqlMap));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(sqlMap));
        Request request = new Request.Builder()
                .url(serviceUrl + "/sql/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return null;
        }
        log.info("请求结果：{}", str);
        EsTaskIdResultDTO esResult = JSON.parseObject(str, EsTaskIdResultDTO.class);
        return esResult;

    }

    /**
     * sql查询 短信渠道汇总统计
     * 429， 503错误，sleep一下重试
     */
    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public EsSmsServiceCodeResultDTO sumSqlBySmsServiceCode(String channelCode,String carrierCode, String stime, String etime) throws Exception {
        stime = DateTimeUtil.parse2String(DateTimeUtil.parse2Date(stime), DateTimeUtil.DATE_PATTERN_LONG_TIMEZONE);
        etime = DateTimeUtil.parse2String(DateTimeUtil.parse2Date(etime), DateTimeUtil.DATE_PATTERN_LONG_TIMEZONE);
        StringBuilder sql = new StringBuilder("select " +
                "sum(custom_split_count) custom_bill_sum, " +
                "sum(supplier_split_count) supplier_bill_sum ");
        sql.append(" from search4" + esIndexName + "_current_reader ");
        sql.append(" where sms_service_code = '" + channelCode + "' ");
        sql.append(" and carrier = '" + carrierCode + "' ");
        sql.append(" and gmt_created >= '" + stime + "' " +
                "and gmt_created <= '" + etime + "'");
        sql.append(" group by sms_service_code,carrier,receipt_status");
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql.toString());
        log.info("入参 ： {} ", JSON.toJSONString(sqlMap));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(sqlMap));
        Request request = new Request.Builder()
                .url(serviceUrl + "/sql/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return null;
        }
        log.info("请求结果：{}", str);
        EsSmsServiceCodeResultDTO esResult = JSON.parseObject(str, EsSmsServiceCodeResultDTO.class);
        return esResult;

    }

    @Retryable(value = {RetryException.class}, backoff = @Backoff(delay = 1000, multiplier = 1))
    public EsTemplateResultDTO sumSqlByTemplate(String stime, String etime) throws Exception {
        StringBuilder sql = new StringBuilder("select " +
                "sum(custom_split_count) custom_bill_sum, " +
                "sum(supplier_split_count) supplier_bill_sum ");
        sql.append(" from search4" + esIndexName + "_current_reader ");
        sql.append(" where send_type = 4 and gmt_created >= '" + stime + "T00:00:00.000+0800' " +
                "and gmt_created <= '" + etime + "T23:59:59.999+0800'");
        sql.append(" group by template_id,carrier,receipt_status");
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql.toString());
        log.info("入参 ： {} ", JSON.toJSONString(sqlMap));
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(sqlMap));
        Request request = new Request.Builder()
                .url(serviceUrl + "/sql/" + esIndexName)
                .post(body)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        if (429 == response.code() || 503 == response.code()) {
            log.warn("失败重试");
            throw new RetryException(response.code() + "", "失败重试");
        }
        if (!response.isSuccessful()) {
            log.info("请求失败：{}", str);
            return null;
        }
        log.info("请求结果：{}", str);
        EsTemplateResultDTO esResult = JSON.parseObject(str, EsTemplateResultDTO.class);
        return esResult;

    }


    public static void main(String[] args) throws Exception {
        ESSearchService esSearchService = new ESSearchService();

        EsSmsServiceCodeResultDTO resultDTO = esSearchService.sumSqlBySmsServiceCode("22","CUCC","2019-07-01 00:00:00", "2019-07-30 23:00:00");

        Date start = DateTimeUtil.parse2Date("2019-07-20 00:00:00");
        Date end = DateTimeUtil.parse2Date("2019-08-1 00:00:00");

        List a = SupplierBillDto.convertToList(resultDTO,start,end);
//        List<ESMessageDTO> documents = Lists.newArrayList();
//        for (int i = 0; i < 100; i++) {
//            ESMessageDTO esMessage = new ESMessageDTO();
//            esMessage.setBusinessId(UUID.randomUUID().toString().replace("-", ""));
//            esMessage.setCustomId(10000L);
//            esMessage.setSendObj("17681869126");
//            esMessage.setSendContent("test");
//            esMessage.setSendStatus(0);
//            esMessage.setSendTime(new Date());
//            esMessage.setReceiptTime(new Date());
//            esMessage.setFailReason("test");
//            esMessage.setTemplateId(RandomUtil.randomLong(100000, 100010));
//            esMessage.setSignatureId(0L);
//            esMessage.setSmsServiceCode("123456");
//            esMessage.setCustomSplitCount(1);
//            esMessage.setGmtModified(new Date());
//            esMessage.setTaskId(RandomUtil.randomLong(10000, 10010));
//            esMessage.setBatchNo(DateTimeUtil.getYYYYMMDD(new Date()));
//            esMessage.setCarrier(i % 3 == 0 ? "cucc" : "ctcc");
//            //FAIL-成功 SUCCESS-失败,UNKNOWN-未回执
//            esMessage.setReceiptStatus(i % 5 == 0 ? "SUCCESS" : "FAIL");
//            esMessage.setGmtCreated(new Date());
//            documents.add(esMessage);
//        }


//        esSearchService.bulkInsert(documents);


        System.out.println("A");

    }

}