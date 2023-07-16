package ru.gransoft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import ru.gransoft.dto.DocumentDto;

@Configuration
@EnableKafka

public class KafkaConfig {

    @Value("${kafka.group.id}")
    private String groupId;

    @Value("${kafka.reply.topic}")
    private String replyTopic;



    @Bean
    public ReplyingKafkaTemplate<String, DocumentDto, DocumentDto> replyingKafkaTemplate(ProducerFactory<String, DocumentDto> pf,
                                                                                         ConcurrentKafkaListenerContainerFactory<String, DocumentDto> factory) {
        ConcurrentMessageListenerContainer<String, DocumentDto> replyContainer = factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, DocumentDto> replyTemplate(ProducerFactory<String, DocumentDto> pf,
            ConcurrentKafkaListenerContainerFactory<String, DocumentDto> factory) {
        KafkaTemplate<String, DocumentDto> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
//    private static final String DLT_TOPIC_SUFFIX = ".dlt";
//
//    private final ProducerFactory<Object, Object> producerFactory;
//    private final ConsumerFactory<Object, Object> consumerFactory;
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
//            DefaultErrorHandler errorHandler
//    ) {
//        // Позволяет создавать консюмеров, которые могут обрабатывать сообщения из нескольких партиций Kafka одновременно,
//        // а также настраивать параметры такие как количество потоков, хэндлинг и т.д.
//        ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
//        // Настройка фабрики для создания консьюмера Kafka
//        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
//        // Возврат сообщений в DLT очередь
//        kafkaListenerContainerFactory.setCommonErrorHandler(errorHandler);
//        // Обработка сообщений в 4 потока
//        kafkaListenerContainerFactory.setConcurrency(4);
//        return kafkaListenerContainerFactory;
//    }
//
//    /**
//     * Обработчик исключений при получении сообщений из kafka по умолчанию.
//     */
//    @Bean
//    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
//        final var handler = new DefaultErrorHandler(deadLetterPublishingRecoverer);
//        // Обрабатываем любые исключения и отправляем в DLT
//        handler.addNotRetryableExceptions(Exception.class);
//        return handler;
//    }
//
//    /**
//     * Публикатор в dead-letter topic.
//     */
//    @Bean
//    public DeadLetterPublishingRecoverer publisher(KafkaTemplate<Object, Object> bytesTemplate) {
//        //  Определяем логику выбора партиции для отправки сообщения в DLT.
//        //  В данном случае, создаём новый объект TopicPartition, используя имя топика (consumerRecord.topic()) и добавляя суффикс DLT_TOPIC_SUFFIX,
//        //  а также номер партиции (consumerRecord.partition()).
//        //  Следовательно в DLT топике должно быть столько партиций сколько и в топике откуда читаем
//        return new DeadLetterPublishingRecoverer(bytesTemplate, (consumerRecord, exception) ->
//                new TopicPartition(consumerRecord.topic() + DLT_TOPIC_SUFFIX, consumerRecord.partition()));
//    }
}