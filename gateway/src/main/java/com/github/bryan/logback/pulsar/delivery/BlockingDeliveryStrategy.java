package com.github.bryan.logback.pulsar.delivery;

import ch.qos.logback.core.spi.ContextAwareBase;
import com.github.bryan.logback.pulsar.ProducerRecord;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.TypedMessageBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * DeliveryStrategy that waits on the producer if the output buffer is full.
 * The wait timeout is configurable with {@link BlockingDeliveryStrategy#setTimeout(long)}
 *
 * @since 0.0.1
 * @deprecated Use {@link AsynchronousDeliveryStrategy} instead.
 */
@Deprecated
public class BlockingDeliveryStrategy extends ContextAwareBase implements DeliveryStrategy {

    private long timeout = 0L;

    @Override
    public <K, V, E> boolean send(Producer<V> producer, ProducerRecord<K, V> record, E event, FailedDeliveryCallback<E> failureCallback) {
        try {

            TypedMessageBuilder<V> mb = producer.newMessage();
            K key = record.getKey();
            if (key instanceof byte[]) {
                mb.key(new String((byte[]) key));
            } else if (key instanceof String) {
                mb.key((String) key);
            }
            Map<String, String> properties = record.getProperties();
            mb.properties(properties).send();
            return true;
        } catch (PulsarClientException e) {
            failureCallback.onFailedDelivery(event, e);
        }
        return false;
    }

    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout for waits on full consumers.
     * <ul>
     *     <li>{@code timeout > 0}: Wait for {@code timeout} milliseconds</li>
     *     <li>{@code timeout == 0}: Wait infinitely
     * </ul>
     *
     * @param timeout a timeout in {@link TimeUnit#MILLISECONDS}.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


}
