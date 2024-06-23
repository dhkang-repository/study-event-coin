package com.example.event_flux.web;

import com.example.event_flux.price.CoinPriceEvent;
import com.example.event_flux.util.BithumbAPIClient;
import com.example.event_flux.util.BithumbApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@RestController
public class CoinPriceController {
    private final Sinks.Many<CoinPriceEvent> sink;
    private final BithumbAPIClient bithumbAPIClient;
    @Autowired
    public CoinPriceController(BithumbAPIClient bithumbAPIClient) {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.bithumbAPIClient = bithumbAPIClient;
    }

    @EventListener
    public void handleCoinPriceEvent(CoinPriceEvent event) {
        this.sink.tryEmitNext(event);
    }

    @GetMapping("/balance/{name}")
    public void getBalance(@PathVariable(name = "name", required = true) String name) {
        bithumbAPIClient.getBalance(name);
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
