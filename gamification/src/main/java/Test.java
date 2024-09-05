import org.apache.pulsar.client.impl.ProducerImpl;

public class Test {
    public static void main(String[] args) {
       final String PULSAR_LOGGER_PREFIX = ProducerImpl.class.getPackage().getName().replaceFirst("\\.impl$", "");
       System.out.println(PULSAR_LOGGER_PREFIX);

    }
}
