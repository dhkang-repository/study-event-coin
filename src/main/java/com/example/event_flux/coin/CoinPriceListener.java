package com.example.event_flux.coin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoinPriceListener {
    @EventListener
    public void handleCoinPriceEvent(CoinPriceEvent event) {
        log.info("Received price update: " + event.getCoinName() + " - $" + event.getPrice());
    }
}
