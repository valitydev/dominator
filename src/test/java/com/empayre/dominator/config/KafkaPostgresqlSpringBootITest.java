package com.empayre.dominator.config;

import com.empayre.dominator.kafka.KafkaProducer;
import dev.vality.testcontainers.annotations.DefaultSpringBootTest;
import dev.vality.testcontainers.annotations.kafka.KafkaTestcontainerSingleton;
import dev.vality.testcontainers.annotations.postgresql.PostgresqlTestcontainerSingleton;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PostgresqlTestcontainerSingleton
@KafkaTestcontainerSingleton(
        properties = {
                "kafka.topics.party-management.enabled=true",
                "kafka.topics.identity.enabled=true",
                "kafka.topics.wallet.enabled=true",
        },
        topicsKeys = {
                "kafka.topics.party-management.id",
                "kafka.topics.identity.id",
                "kafka.topics.wallet.id",
        }
)
@DefaultSpringBootTest
@Import(KafkaProducer.class)
public @interface KafkaPostgresqlSpringBootITest {
}
