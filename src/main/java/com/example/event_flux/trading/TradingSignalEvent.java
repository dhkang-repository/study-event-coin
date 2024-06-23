package com.example.event_flux.trading;

import org.springframework.context.ApplicationEvent;

public class TradingSignalEvent extends ApplicationEvent {
    private final String signalType;
    private final String coinName;
    private final double price;

    public TradingSignalEvent(Object source, String signalType, String coinName, double price) {
        super(source);
        this.signalType = signalType;
        this.coinName = coinName;
        this.price = price;
    }

    public String getSignalType() {
        return signalType;
    }

    public String getCoinName() {
        return coinName;
    }

    public double getPrice() {
        return price;
    }
}
