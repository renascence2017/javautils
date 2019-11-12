package com.api.kafka;

import com.alibaba.fastjson.JSON;

import javax.annotation.Resource;

public class ConsumerKafkaListener implements MessageListener<String, String> {


    @Resource
    private SmsLogManager smsLogManager;

    /**
     * 消费消息接口，由应用来实现<br>
     *
     * @param record  消息
     * @param context
     * @return 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     */
    @Override
    public Action consume(ConsumerRecord<String, String> record, ConsumeContext context) {
        try {
            String result = record.value();
            log.info("接收到的消息:{}", result);
            XmanKafkaMessage xmanKafkaMessage = JSON.parseObject(result, XmanKafkaMessage.class);
            KafkaTypeEnum kafkaTypeEnum = KafkaTypeEnum.parseCode(xmanKafkaMessage.getTypeCode());
            switch (kafkaTypeEnum) {
                case SMS_INSERT: {
                    smsLogManager.save(JSON.parseObject(xmanKafkaMessage.getMessage(), SmsLogDTO.class));
                    break;
                }
                case SMS_UPDATE: {
                    smsLogManager.update(JSON.parseObject(xmanKafkaMessage.getMessage(), SmsLogDTO.class));
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            log.info("thor kafka consume error", e);
        }
        return Action.CommitMessage;
    }

}