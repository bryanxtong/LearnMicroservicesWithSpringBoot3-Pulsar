package microservices.book.logs.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.PulsarTopic;

@Configuration
public class PulsarConfiguration {
    @Bean
    public PulsarTopic partitionedLogsTopic(){
        return PulsarTopic.builder("logs").numberOfPartitions(4).build();
    }
}
