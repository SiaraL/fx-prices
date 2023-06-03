package pl.standander.fxprice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.standander.fxprice.model.FxPrice;
import pl.standander.fxprice.service.FxPricesService;
import pl.standander.fxprice.service.MessageFeederImpl;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class FxPricesServiceTest {

    @Test
    void checkLatestPrice() {
        List<String> csvData = List.of(
                "112, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110",
                "111, EUR/JPY, 117.61,118.91,01-06-2020 12:00:09:110",
                "110, EUR/JPY, 118.61,119.50,01-06-2020 12:00:02:110",
                "125, EUR/PLN, 4,4.5,01-06-2020 12:10:02:110",
                "121, EUR/PLN, 3.8,4.4,01-06-2020 12:05:02:110",
                "119, EUR/PLN, 3.7,4.3,01-06-2020 12:01:02:110",
                "132, EUR/USD, 1.1,1.2,01-06-2020 12:10:02:111",
                "131, EUR/USD, 1,1.1,01-06-2020 12:01:02:110"
        );

        FxPrice expectedPrice1 = new FxPrice();
        expectedPrice1.setId(112);
        expectedPrice1.setAsk(119.91);
        expectedPrice1.setBid(119.61);
        expectedPrice1.setName("EUR/JPY");
        expectedPrice1.setDate("01-06-2020 12:01:02:110");
        FxPrice expectedPrice2 = new FxPrice();
        expectedPrice2.setId(125);
        expectedPrice2.setAsk(4.5);
        expectedPrice2.setBid(4);
        expectedPrice2.setName("EUR/PLN");
        expectedPrice2.setDate("01-06-2020 12:10:02:110");
        FxPrice expectedPrice3 = new FxPrice();
        expectedPrice3.setId(132);
        expectedPrice3.setAsk(1.2);
        expectedPrice3.setBid(1.1);
        expectedPrice3.setName("EUR/USD");
        expectedPrice3.setDate("01-06-2020 12:10:02:111");
        List<FxPrice> expectedValues = List.of(expectedPrice1, expectedPrice2, expectedPrice3);

        SubmissionPublisher publisher = new SubmissionPublisher<>();
        MessageFeederImpl messageFeeder = mock(MessageFeederImpl.class);
        when(messageFeeder.init()).thenReturn(List.of(
                "109, EUR/JPY, 118.61,119.50,01-06-2020 12:00:00:110"
        ));
        when(messageFeeder.getPublisher()).thenReturn(publisher);

        FxPricesService fxPricesService = new FxPricesService(messageFeeder);

        Flow.Subscription mockSubscription = mock(Flow.Subscription.class);
        fxPricesService.onSubscribe(mockSubscription);

        fxPricesService.onNext(csvData);

        var result = fxPricesService.getLatestFxPrices();

        assertThat(result).isEqualTo(expectedValues);
    }
}
