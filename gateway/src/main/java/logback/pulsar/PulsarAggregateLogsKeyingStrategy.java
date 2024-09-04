package logback.pulsar;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.github.bryan.logback.pulsar.keying.KeyingStrategy;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Used key format [host-port-application] for pulsar
 */
public class PulsarAggregateLogsKeyingStrategy extends ContextAwareBase implements KeyingStrategy<Object>, LifeCycle {
    private byte[] keyHash = null;
    private boolean errorWasShown = false;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        String hostName ="";
        try {
            hostName = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        //final String hostname = context.getProperty("host");
        final String hostPort = context.getProperty("port");
        final String applicationId = context.getProperty("applicationId");

        if (hostName == null || hostPort == null || applicationId == null) {
            if (!errorWasShown) {
                addError("Hostname/hostport/applicationId could not be found in context. HostNamePartitioningStrategy will not work.");
                errorWasShown = true;
            }
        } else {
            String keys = hostName + "-" + hostPort + "-" + applicationId;
            keyHash = ByteBuffer.allocate(4).putInt(keys.hashCode()).array();
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        errorWasShown = false;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public byte[] createKey(Object o) {
        return keyHash;
    }
}
