package com.panda.system.domin.mq.send;

import com.panda.system.domin.CommentsInfo;
import com.panda.system.domin.CommentsReply;
import com.panda.system.domin.mq.enitty.SpotLikeMessage;
import com.panda.system.domin.mq.enitty.WatchRecordEntity;
import com.panda.system.domin.param.CommentParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TopicSendMessage {


    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    public void send(WatchRecordEntity message) {
        log.info("send message {}",message);
        rabbitmqTemplate.convertAndSend("topicExchange","topic.message",message);
    }

    public void send(SpotLikeMessage spotLikeMessage){
        log.info("send message {}",spotLikeMessage);
        rabbitmqTemplate.convertAndSend("topicExchange","spotLike.message",spotLikeMessage);
    }

    public void send(CommentParam message){
        log.info("send message {}",message);
        rabbitmqTemplate.convertAndSend("topicExchange","comment.message",message);
    }



}
