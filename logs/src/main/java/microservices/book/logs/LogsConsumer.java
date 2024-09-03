package microservices.book.logs;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogsConsumer {
    @PulsarListener(topics = "logs",subscriptionType = SubscriptionType.Key_Shared, subscriptionName = "logs")
    public void log(Message<byte[]> record,
                    @Header("level") String level,
                    @Header("applicationId") String appId) {
        byte[] messageBytes = record.getValue();
        String msg = new String(messageBytes);
        Marker marker = MarkerFactory.getMarker(appId);
        switch (level) {
            case "INFO" -> log.info(marker, msg);
            case "ERROR" -> log.error(marker, msg);
            case "WARN" -> log.warn(marker, msg);
        }
    }
}
