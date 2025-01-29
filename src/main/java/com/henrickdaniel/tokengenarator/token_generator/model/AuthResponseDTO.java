package com.henrickdaniel.tokengenarator.token_generator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String acessToken;
    private String tokenType;

}
