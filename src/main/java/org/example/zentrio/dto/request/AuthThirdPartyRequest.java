package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthThirdPartyRequest {

    @NotNull
    @NotBlank
    private String idToken;

    @NotNull
    @NotBlank
    private String provider;
}
