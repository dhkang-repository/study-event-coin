package com.example.event_flux.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:bithumb.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "bithumb")
public class BithumbAPIProperties {
    private String connectKey;
    private String secretKey;

    public String getConnectKey() {
        return connectKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setConnectKey(String connectKey) {
        this.connectKey = connectKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
