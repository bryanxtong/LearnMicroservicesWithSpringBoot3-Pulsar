package logback.pulsar;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.bryan.logback.pulsar.ProducerRecord;
import com.github.bryan.logback.pulsar.PulsarAppender;
import org.apache.pulsar.client.api.Producer;
import java.util.Map;

/**
 * add pulsar producer properties
 * @param <E>
 */
public class PulsarAppenderWithHeader<E> extends PulsarAppender<ILoggingEvent> {
    protected void append(ILoggingEvent e) {

        final byte[] payload = encoder.encode(e);
        final byte[] key = keyingStrategy.createKey(e);

        final Long timestamp = isAppendTimestamp() ? getTimestamp(e) : null;
        //add producer properties
        this.getMessageProperties().put("level",e.getLevel().toString());
        Map<String, String> copyOfPropertyMap = encoder.getContext().getCopyOfPropertyMap();
        if(copyOfPropertyMap != null){
            copyOfPropertyMap.entrySet().forEach(c-> this.getMessageProperties().put(c.getKey(), c.getValue()));
        }
        //add producer properties

        final ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(key, payload, messageProperties);
        //both common logs and pulsar client logs append may lead to dead lock, disable pulsar client logs as a temp solution [logging.level.org.apache.pulsar.client.impl=ERROR]
        final Producer<byte[]> producer = lazyProducer.get();

        if (producer != null) {
            deliveryStrategy.send(lazyProducer.get(), record, e, failedDeliveryCallback);
        } else {
            failedDeliveryCallback.onFailedDelivery(e, null);
        }
    }


}
