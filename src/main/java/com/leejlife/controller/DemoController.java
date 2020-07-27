package com.leejlife.controller;

import com.alibaba.rocketmq.client.producer.*;
import com.alibaba.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@ResponseBody
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {

//        return myLong;
//        return content;
        return "welcome hello word";
    }

    @RequestMapping(value = "/mq_send", method = RequestMethod.GET)
    public String mqSend() throws Exception {
        String msg = "demo msg test";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("DemoTopic", "DemoTag", msg.getBytes());
        // 发送延迟消息：
        // sendMsg.setDelayTimeLevel(2);
        //同步发送
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应信息：" + sendResult.toString());
        return "messageId:[" + sendResult.getMsgId() + "] send ok";
    }

    @RequestMapping(value = "/async_mq_send", method = RequestMethod.GET)
    public String mqSendAsync() throws Exception {
        String msg = "demo msg async test";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("DemoTopic", "DemoTag", msg.getBytes());
        // 发送延迟消息：
        // sendMsg.setDelayTimeLevel(2);
        //异步发送
        defaultMQProducer.send(sendMsg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("messageId:[" + sendResult.getMsgId() + "] 回调成功（异步发送）：" + sendResult.toString());
            }

            @Override
            public void onException(Throwable throwable) {
                //考虑重新发送
                logger.info("回调成功（异步发送）：发送失败。exception：" + throwable);
            }
        });
        logger.info("异步发送成功OK");
        return "messageId:[] 异步发送成功ok";
    }

    @RequestMapping(value = "/one_way_mq_send", method = RequestMethod.GET)
    public String mqSendOneWay() throws Exception {
        String msg = "demo msg async test";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("DemoTopic", "DemoTag", msg.getBytes());
        // 发送延迟消息：
        // sendMsg.setDelayTimeLevel(2);
        //OneWay方式发送
        defaultMQProducer.sendOneway(sendMsg);
        return "messageId:[] OneWay发送成功ok";
    }

    @RequestMapping(value = "/delay_mq_send", method = RequestMethod.GET)
    public String mqSendDelay() throws Exception {
        String msg = "demo msg delay test";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("DemoTopic", "DemoTag", msg.getBytes());
        sendMsg.setDelayTimeLevel(2);
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应信息：" + sendResult.toString());
        return "messageId:[" + sendResult.getMsgId() + "] 发送成功ok";
    }

    @RequestMapping(value = "/transaction_mq_send", method = RequestMethod.GET)
    public String transactionSendDelay() throws Exception {
        String msg = "demo transaction delay test";
        logger.info("开始发送消息：" + msg);
        Message sendMsg = new Message("TransactionDemoTopic", "transactionTag", msg.getBytes());
        TransactionSendResult result = transactionMQProducer.sendMessageInTransaction(sendMsg, new LocalTransactionExecuter() {
            @Override
            public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
//                logger.info("执行本地方法成功");
//                return LocalTransactionState.COMMIT_MESSAGE;
                logger.info("执行本地方法阻塞线程等待回查...");
                try {
                    Thread.sleep(1000 * 100);
                    logger.info("执行本地方法成功");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return LocalTransactionState.COMMIT_MESSAGE;
//                logger.info("执行本地方法失败，事务回滚");
//                return LocalTransactionState.COMMIT_MESSAGE;
            }
        }, null);

        return "transactionId:" + result.getTransactionId();
    }

}
