package microservices.book.logs;


import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogsConsumer1 {

/*    @PulsarListener(topics = "logs",subscriptionType = SubscriptionType.Key_Shared, subscriptionName = "logs")
    public void log(Message<byte[]> record*//*,@Header(PulsarHeaders.KEY) byte[] key*//*) {
        byte[] messageBytes = record.getValue();
        String key = record.getKey();
        String msg = new String(messageBytes);
        System.out.println("============>"+new String(key) +msg + record.getProperties());

    }*/
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
