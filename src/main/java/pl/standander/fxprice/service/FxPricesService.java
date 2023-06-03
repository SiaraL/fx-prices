package pl.standander.fxprice.service;

import org.springframework.stereotype.Service;
import pl.standander.fxprice.model.FxPrice;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Flow;

@Service
public class FxPricesService implements Flow.Subscriber<List<String>> {

    private ConcurrentMap latestFxPrices = new ConcurrentHashMap<String, FxPrice>();
    private final MessageFeederImpl messageFeeder;
    private Flow.Subscription subscription;

    public FxPricesService(MessageFeederImpl messageFeeder) {
        this.messageFeeder = messageFeeder;
        this.messageFeeder.init().forEach(elementCsv -> saveRecord(elementCsv));
        this.messageFeeder.getPublisher().subscribe(this);
    }

    public List<FxPrice> getLatestFxPrices() {
        return new ArrayList<>(this.latestFxPrices.values());
    }

    public void saveRecord(String csv) {
        FxPrice fxPrice = convertCSV2FxPrice(csv);
        this.latestFxPrices.put(fxPrice.getName(), fxPrice);
    }

    private FxPrice convertCSV2FxPrice(String csv) {
        csv.trim();
        String[] arr = csv.split(",");
        FxPrice fxPrice = new FxPrice();
        fxPrice.setId(Long.valueOf(arr[0]));
        fxPrice.setName(arr[1]);
        fxPrice.setBid(Double.parseDouble(arr[2]));
        fxPrice.setAsk(Double.parseDouble(arr[3]));
        fxPrice.setDate(arr[4]);

        return fxPrice;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<String> csvData) {
        subscription.request(1);
        List<FxPrice> result = new ArrayList<>();
        for (String csvElement : csvData) {
            FxPrice fxPrice = convertCSV2FxPrice(csvElement);
            result.add(fxPrice);
        }
        result
                .stream()
                .sorted((FxPrice p1, FxPrice p2) -> p1.getDate().compareTo(p2.getDate()))
                .forEach(fxPrice -> {
                    this.latestFxPrices.put(fxPrice.getName(), fxPrice);
                });
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
    }
}
