package com.bankin.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TotalTransPerDay {
    private String transAmount;
    private String receiveAmount;
    private String modifiedDate;
}
