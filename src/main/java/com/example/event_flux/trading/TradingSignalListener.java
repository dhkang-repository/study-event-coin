package com.example.event_flux.trading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TradingSignalListener {
    @EventListener
    public void handleTradingSignalEvent(TradingSignalEvent event) {
        if("BUY".equals(event.getSignalType())) {
            log.info("Buy signal for " + event.getCoinName() + " at $" + event.getPrice());
            // 매수 로직 구현
        } else if ("SELL".equals(event.getSignalType())) {
            log.info("Sell signal for " + event.getCoinName() + " at $" + event.getPrice());
            // 매도 로직 구현
        } else if ("HOLD".equals(event.getSignalType())) {
            log.info("HOLD signal for " + event.getCoinName() + " at $" + event.getPrice());
            // 매도 로직 구현
        }
    }
}
