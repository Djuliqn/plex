package com.plex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @JsonProperty(value = "type")
    private String tokenType;

    @JsonProperty(value = "access_token")
    private String accessToken;
}
