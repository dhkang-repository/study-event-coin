package com.example.event_flux.price;

import org.springframework.context.ApplicationEvent;

public class CoinPriceEvent extends ApplicationEvent {
    private final String coinName;
    private final double price;


    public CoinPriceEvent(Object source, String coinName, double price) {
        super(source);
        this.coinName = coinName;
        this.price = price;
    }

    public String getCoinName() {
        return coinName;
    }

    public double getPrice() {
        return price;
    }
}
