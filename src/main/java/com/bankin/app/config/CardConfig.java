package com.bankin.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("app.card")
public class CardConfig {
    private Integer availableDuration;
    private CreditConfig credit;
}
