package com.api.es;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SmsLogDTO implements Serializable {

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 批次
     */
    private String batchNo;
    /**
     * 发送类型 1直接，2轮询，3接口
     */
    private String sendType;

    /**
     * 渠道 1 :短信 2:邮件 3:微信
     */
    private String channelType;

    /**
     * 发送对象
     */
    private String sendObj;

    /**
     * 发送内容 带签名
     */
    private String sendContent;

    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 消息id 供应商的
     */
    private String messageUid;

    /**
     * 业务id 给客户的
     */
    private String businessId;

    /**
     * 发送结果 0 发送成功  其他发送失败
     */
    private Integer sendStatus;

    /**
     * 发送失败原因
     */
    private String failReason;

    /**
     * 回执时间
     */
    private Date receiptTime;

    /**
     * 回执状态 FAIL-成功 SUCCESS-失败,UNKNOWN-未回执
     */
    private String receiptStatus;

    /**
     * 回执编码
     */
    private String receiptResultCode;

    /**
     * 供应商
     */
    private Long supplierId;

    /**
     * 短信通道code
     */
    private String smsServiceCode;
    /**
     * 回执推送地址
     */
    private String callbackUrl;

    /**
     * 模板
     */
    private Long templateId;

    /**
     * 签名
     */
    private Long signatureId;

    /**
     * 客户id
     */
    private Long customId;


    /**
     * 运营商 cmcc cucc ctcc
     */
    private String carrier;

    /**
     * 供应商 短信内容拆分条数
     */
    private Integer supplierSplitCount;

    /**
     * 客户 短信内容拆分条数
     */
    private Integer customSplitCount;
    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
