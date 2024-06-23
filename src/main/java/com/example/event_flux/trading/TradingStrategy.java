package com.example.event_flux.trading;

import com.example.event_flux.price.CoinPriceEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TradingStrategy {
    private final CoinPriceTracker shortTermTracker;
    private final CoinPriceTracker longTermTracker;
    private final ApplicationEventPublisher eventPublisher;

    public TradingStrategy(ApplicationEventPublisher eventPublisher) {
        this.shortTermTracker = new CoinPriceTracker(5); // 단기 이동 평균 (5개 데이터)
        this.longTermTracker = new CoinPriceTracker(20); // 장기 이동 평균 (20개 데이터)
        this.eventPublisher = eventPublisher;
    }

    public void evaluate(CoinPriceEvent event) {
        double price = event.getPrice();
        shortTermTracker.addPrice(price);
        longTermTracker.addPrice(price);

        if(shortTermTracker.getSize() >= 5 && longTermTracker.getSize() >= 20) {
            double shortTermAvg = shortTermTracker.getAverage();
            double longTermAvg = longTermTracker.getAverage();

            if (shortTermAvg > longTermAvg) {
                eventPublisher.publishEvent(new TradingSignalEvent(this, "BUY", event.getCoinName(), price));
            } else if(shortTermAvg < longTermAvg) {
                eventPublisher.publishEvent(new TradingSignalEvent(this, "SELL", event.getCoinName(), price));
            } else {
                eventPublisher.publishEvent(new TradingSignalEvent(this, "HOLD", event.getCoinName(), price));
            }
        }

    }
}
