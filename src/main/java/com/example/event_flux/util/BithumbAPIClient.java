package com.example.event_flux.util;

import com.example.event_flux.props.BithumbAPIProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class BithumbAPIClient {
    private final WebClient webClient;
    private final BithumbAPIProperties bithumbAPIProperties;

    public BithumbAPIClient(BithumbAPIProperties bithumbAPIProperties) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.bithumb.com")
                .build();
        this.bithumbAPIProperties = bithumbAPIProperties;
    }

    public void getBalance(String name) {
        String endpoint = "/info/balance";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("endpoint", endpoint);
        params.put("currency", name);

        String paramsString = createQueryString(params);
        String nonce = String.valueOf(System.currentTimeMillis());
//        String nonce = "1719155492708";

        try {
            String apiSignature = BithumbApiUtil.generateSignature(endpoint + "\0" + paramsString + "\0" + nonce,
                    bithumbAPIProperties.getSecretKey());
            Mono<ResponseEntity<String>> accept = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path(endpoint).build())
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .header("Api-Key", bithumbAPIProperties.getConnectKey())
                    .header("Api-Nonce", nonce)
                    .header("Api-Sign", apiSignature)
//                    .header("Api-Client-Type", "2")

                    .bodyValue(paramsString)
                    .exchangeToMono(clientResponse -> {
                        return clientResponse.toEntity(String.class);
                    });
            String s = accept.block().toString();
            System.out.println(s);
        } catch (Exception e) {
            Mono.error(e);
        }
    }

    private String createQueryString(Map<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return queryString.toString();
    }

}
