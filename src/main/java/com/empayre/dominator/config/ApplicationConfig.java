package com.empayre.dominator.config;

import com.empayre.dominator.domain.Dmn;
import com.empayre.dominator.exception.SerializationException;
import dev.vality.damsel.domain_config.RepositorySrv;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.apache.thrift.transport.TTransportException;
import org.jooq.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class ApplicationConfig {

    @Bean
    public RepositorySrv.Iface dominantClient(@Value("${dmt.url}") Resource resource,
                                              @Value("${dmt.networkTimeout}") int networkTimeout) throws IOException {
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(resource.getURI())
                .build(RepositorySrv.Iface.class);
    }

    @Bean
    public Schema schema() {
        return Dmn.DMN;
    }

    @Bean
    public TSerializer serializer() {
        try {
            return new TSerializer();
        } catch (TTransportException e) {
            throw new SerializationException(e);
        }
    }

    @Bean
    public TDeserializer deserializer() {
        try {
            return new TDeserializer();
        } catch (TTransportException e) {
            throw new SerializationException(e);
        }
    }
}
