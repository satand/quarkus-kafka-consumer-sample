package org.acme;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.*;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class MyReactiveMessagingApplication {

    @ConfigProperty(name = "message.processing.millis") 
    long messageProcessingMillis;

    @Incoming("demo")
    public CompletionStage<Void> consume(Message<String> message) throws InterruptedException {
        Optional<IncomingKafkaRecordMetadata> x = message.getMetadata(IncomingKafkaRecordMetadata.class);
        System.out.println("Message: " + message.getPayload() + (x.isPresent() ? " - Partition: " + x.get().getPartition() : ""));
        Thread.sleep(messageProcessingMillis);
        return message.ack();
    }
}
