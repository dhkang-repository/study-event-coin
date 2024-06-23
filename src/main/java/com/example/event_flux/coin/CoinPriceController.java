package com.example.event_flux.coin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class CoinPriceController {
    private final Sinks.Many<CoinPriceEvent> sink;

    @Autowired
    public CoinPriceController() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @EventListener
    public void handleCoinPriceEvent(CoinPriceEvent event) {
        this.sink.tryEmitNext(event);
    }

    @GetMapping("/prices")
    public Flux<ServerSentEvent<CoinPriceEvent>> streamCoinPrices() {
        return this.sink.asFlux().map(event ->
                ServerSentEvent.<CoinPriceEvent>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event("price-update")
                        .data(event)
                        .build()
        ).doOnCancel(() -> System.out.println("Client disconnected"));
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, welcome to the Coin Price Tracker!";
    }
}
