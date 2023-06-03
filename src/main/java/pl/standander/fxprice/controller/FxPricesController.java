package pl.standander.fxprice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.standander.fxprice.model.FxPrice;
import pl.standander.fxprice.service.FxPricesService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class FxPricesController {

    private final FxPricesService fxPricesService;

    @GetMapping("fx-prices")
    public List<FxPrice> getFxPrices() {
        return fxPricesService.getLatestFxPrices();
    }
}
