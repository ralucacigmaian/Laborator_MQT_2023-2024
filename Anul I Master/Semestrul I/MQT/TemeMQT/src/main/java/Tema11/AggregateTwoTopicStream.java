package Tema11;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class AggregateTwoTopicStream {

    private static final String BOOTSTRAP_SERVER = "localhost:9092";
    private static final String CLIENT_ID = "aggregateTwoTopicsStream";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        properties.put("application.id", "aggregateTwoTopicsStream-application");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream1 = builder.stream("topic1stream");

        KStream<String, String> stream2 = builder.stream("topic2stream");

        KStream<String, String> aggregatedStream = stream1.merge(stream2);

        aggregatedStream.to("aggregated-topic", Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}