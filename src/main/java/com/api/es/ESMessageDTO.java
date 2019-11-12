package com.api.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;


@Data
public class ESMessageDTO {

    /**
     * 业务id 给客户的
     */
    @JSONField(name = "id")
    private String businessId;

    /**
     * 任务id
     */
    @JSONField(name = "task_id")
    private Long taskId;
    /**
     * 批次
     */
    @JSONField(name = "batch_no")
    private String batchNo;
    /**
     * 发送类型 1直接，2轮询，3接口
     */
    @JSONField(name = "send_type")
    private String sendType;

    /**
     * 渠道 1 :短信 2:邮件 3:微信
     */
    @JSONField(name = "channel_type")
    private String channelType;

    /**
     * 发送对象
     */
    @JSONField(name = "send_obj")
    private String sendObj;

    /**
     * 短信类型
     */
    @JSONField(name = "sms_type")
    private String smsType;

    /**
     * 发送内容 带签名
     */
    @JSONField(name = "send_content")
    private String sendContent;

    /**
     * 发送时间
     */
    @JSONField(name = "send_time")
    private Date sendTime;
    /**
     * 消息id 供应商的
     */
    @JSONField(name = "message_uid")
    private String messageUid;

    /**
     * 发送结果 0 发送成功  其他发送失败
     */
    @JSONField(name = "send_status")
    private Integer sendStatus;

    /**
     * 发送失败原因
     */
    @JSONField(name = "fail_reason")
    private String failReason;

    /**
     * 回执时间
     */
    @JSONField(name = "receipt_time")
    private Date receiptTime;

    /**
     * 回执状态 FAIL-成功 SUCCESS-失败,UNKNOWN-未回执
     */
    @JSONField(name = "receipt_status")
    private String receiptStatus;

    /**
     * 回执编码
     */
    @JSONField(name = "receipt_result_code")
    private String receiptResultCode;

    /**
     * 供应商
     */
    @JSONField(name = "supplier_id")
    private Long supplierId;

    /**
     * 短信通道code
     */
    @JSONField(name = "sms_service_code")
    private String smsServiceCode;
    /**
     * 回执推送地址
     */
    @JSONField(name = "callback_url")
    private String callbackUrl;

    /**
     * 模板
     */
    @JSONField(name = "template_id")
    private Long templateId;

    /**
     * 签名
     */
    @JSONField(name = "signature_id")
    private Long signatureId;

    /**
     * 客户id
     */
    @JSONField(name = "custom_id")
    private Long customId;


    /**
     * 运营商 cmcc cucc ctcc
     */
    @JSONField(name = "carrier")
    private String carrier;

    /**
     * 供应商 短信内容拆分条数
     */
    @JSONField(name = "supplier_split_count")
    private Integer supplierSplitCount;

    /**
     * 客户 短信内容拆分条数
     */
    @JSONField(name = "custom_split_count")
    private Integer customSplitCount;
    /**
     * 创建时间
     */
    @JSONField(name = "gmt_created")
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @JSONField(name = "gmt_modified")
    private Date gmtModified;
}


