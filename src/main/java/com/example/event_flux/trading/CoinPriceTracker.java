package com.example.event_flux.trading;

import java.util.LinkedList;
import java.util.Queue;

public class CoinPriceTracker {
    private final Queue<Double> prices;
    private final int maxSize;
    private double sum;

    public CoinPriceTracker(int maxSize) {
        this.prices = new LinkedList<>();
        this.maxSize = maxSize;
        this.sum = 0.;
    }

    public void addPrice(double price) {
        if(prices.size() == maxSize) {
            sum -= prices.poll();
        }
        prices.offer(price);
        sum += price;
    }

    public double getAverage() {
        if(prices.isEmpty()) {
            return 0.;
        }
        return sum / prices.size();
    }

    public int getSize() {
        return prices.size();
    }
}
