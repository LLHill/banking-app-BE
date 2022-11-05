package com.bankin.app.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class CardReq {
    @JsonProperty(value = "type")
    private String cardType;
    @ToString.Exclude
    private String PIN;
}
