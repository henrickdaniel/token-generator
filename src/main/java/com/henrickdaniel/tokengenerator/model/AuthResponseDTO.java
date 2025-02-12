package com.henrickdaniel.tokengenerator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType;

}
