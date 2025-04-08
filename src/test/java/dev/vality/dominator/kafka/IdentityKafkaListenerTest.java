package dev.vality.dominator.kafka;

import dev.vality.dominator.config.KafkaPostgresqlSpringBootITest;
import dev.vality.dominator.service.IdentityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyList;

@KafkaPostgresqlSpringBootITest
public class IdentityKafkaListenerTest {

    @Value("${kafka.topics.identity.id}")
    public String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockitoBean
    private IdentityService identityService;

    @Test
    public void listenEmptyChanges() {
        kafkaProducer.sendMessage(topic);
        Mockito.verify(identityService, Mockito.timeout(TimeUnit.MINUTES.toMillis(1)).times(1))
                .handleEvents(anyList());
    }
}
