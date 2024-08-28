package microservices.book.multiplication.configuration;

import microservices.book.event.challenge.ChallengeSolvedEvent;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.DefaultSchemaResolver;
import org.springframework.pulsar.core.PulsarTopic;
import org.springframework.pulsar.core.SchemaResolver;

@Configuration
public class PulsarConfiguration {

    @Bean
    public SchemaResolver.SchemaResolverCustomizer<DefaultSchemaResolver> schemaResolverCustomizer() {
        return (schemaResolver) -> {
            schemaResolver.addCustomSchemaMapping(ChallengeSolvedEvent.class, Schema.JSON(ChallengeSolvedEvent.class));
            //add more
        };
    }

    @Bean
    public PulsarTopic partitionedTopic(@Value("${pulsar.attempts.topic}") final String topicName,
                                        @Value("${pulsar.attempts.partitions}") Integer partitions){
        return PulsarTopic.builder(topicName).numberOfPartitions(partitions).build();
    }
}
