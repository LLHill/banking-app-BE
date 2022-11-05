package com.bankin.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransPerDay {
    private String transAmount;
    private String modifiedDate;
}
