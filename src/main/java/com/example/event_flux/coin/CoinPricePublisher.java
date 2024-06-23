package com.example.event_flux.coin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CoinPricePublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final WebClient webClient = WebClient.create("https://api.bithumb.com");

    @Scheduled(fixedRate = 5000)
    public void publishCoinPrice() {
        String[] coins = {"BTC", "ETH", "XRP"};
        for (String coin : coins) {
            fetchCoinPrice(coin).subscribe(price -> {
                CoinPriceEvent event = new CoinPriceEvent(this, coin, price);
                applicationEventPublisher.publishEvent(event);
            });
        }
    }

    private Mono<Double> fetchCoinPrice(String coin) {
        return webClient.get().uri("/public/ticker/{coin}_KRW", coin).retrieve().bodyToMono(String.class).map(response -> {
            // JSON 파싱
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response);
                String priceString = root.path("data").path("closing_price").asText();
                return Double.parseDouble(priceString);
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        });
    }
}
