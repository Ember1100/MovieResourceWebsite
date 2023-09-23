package com.panda.system.domin.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TopicConfig {
    //观看电影消息
    private final String message = "topic.message";

    //spot点赞消息
    private final String spotLikeMessage = "spotLike.message";

    //评论消息队列
    private final String commentMessage = "comment.message";


    @Bean
    public Queue queueMessage() {
        return new Queue(this.message);
    }


    @Bean
    public Queue queueSpotLikeMessage() {
        return new Queue(this.spotLikeMessage);
    }


    @Bean
    public Queue queueCommentMessage() {
        return new Queue(this.commentMessage);
    }


    @Bean
    TopicExchange exchange(){
        return new TopicExchange("topicExchange");
    }


    @Bean
    Binding bindingExchangeMessages(Queue queueMessage,TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeSpotLikeMessages(Queue queueSpotLikeMessage,TopicExchange exchange) {
        return BindingBuilder.bind(queueSpotLikeMessage).to(exchange).with("spotLike.message");
    }

    @Bean
    Binding bindingExchangeCommentMessages(Queue queueCommentMessage,TopicExchange exchange) {
        return BindingBuilder.bind(queueCommentMessage).to(exchange).with("comment.message");
    }




}
