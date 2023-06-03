package pl.standander.fxprice.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Service
@EnableAsync
@EnableScheduling
public class MessageFeederImpl implements MessageFeeder {
    private long id = 0;
    private final SubmissionPublisher publisher = new SubmissionPublisher<>();

    public List<String> init() {
        List<String> result = new LinkedList<>();
        for (Map<String, Double> map : getMockDataSet().values()) {
            result.add(generator(map));
        }
        return result;
    }

    private String generator() {
        Map<String, Double> randomResult = getRandomFxType();
        return generator(randomResult);
    }

    private String generator(Map<String, Double> map) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");

        StringBuilder sb = new StringBuilder();
        sb.append(++this.id);
        sb.append(", ");
        sb.append(map.keySet().stream().toList().get(0));
        sb.append(", ");
        sb.append(map.values().stream().toList().get(0) * (1 - getRandomAmplitude()));
        sb.append(",");
        sb.append(map.values().stream().toList().get(0) * (1 + getRandomAmplitude()));
        sb.append(",");
        sb.append(formatter.format(new Date()));
        return sb.toString();
    }

    private double getRandomAmplitude() {
        return Math.abs(new Random().nextDouble(1) / 10); //0 - 10 %
    }

    private Map<String, Double> getRandomFxType() {
        Random random = new Random();
        int element = random.nextInt(getMockDataSet().size());
        return (Map<String, Double>) getMockDataSet().get(element);
    }

    private Map<Integer, Map<String, Double>> getMockDataSet() {
        Map types = new HashMap<Integer, Map<String, Double>>();

        double PLN = 1;
        double USD = 4.2;
        double EUR = 4.5;
        double JPY = 0.03;
        double GBP = 5.2;

        types.put(0, Map.of("EUR/USD", EUR / USD));
        types.put(1, Map.of("EUR/PLN", EUR / PLN));
        types.put(2, Map.of("EUR/JPY", EUR / JPY));
        types.put(3, Map.of("EUR/GBP", EUR / GBP));
        types.put(4, Map.of("USD/EUR", USD / EUR));
        types.put(5, Map.of("USD/PLN", USD / PLN));
        types.put(6, Map.of("USD/JPY", USD / JPY));
        types.put(7, Map.of("USD/GBP", USD / GBP));
        types.put(8, Map.of("PLN/EUR", PLN / EUR));
        types.put(9, Map.of("PLN/USD", PLN / USD));
        types.put(10, Map.of("PLN/JPY", PLN / JPY));
        types.put(11, Map.of("PLN/GBP", PLN / GBP));
        types.put(12, Map.of("JPY/EUR", JPY / EUR));
        types.put(13, Map.of("JPY/USD", JPY / USD));
        types.put(14, Map.of("JPY/PLN", JPY / PLN));
        types.put(15, Map.of("JPY/GBP", JPY / GBP));
        types.put(16, Map.of("GBP/EUR", GBP / EUR));
        types.put(17, Map.of("GBP/USD", GBP / USD));
        types.put(18, Map.of("GBP/PLN", GBP / PLN));
        types.put(19, Map.of("GBP/JPY", GBP / JPY));

        return types;
    }

    @Override
    @SneakyThrows
    @Scheduled(fixedDelay = 1000)
    public void onMessage() {
        List<String> result = new LinkedList<>();
        Random random = new Random();
        int number = random.nextInt(10) + 1;
        for (int i = 0; i < number; i++) {
            result.add(generator());
        }
        publisher.submit(result);
    }

    public Flow.Publisher getPublisher(){
        return this.publisher;
    }
}
