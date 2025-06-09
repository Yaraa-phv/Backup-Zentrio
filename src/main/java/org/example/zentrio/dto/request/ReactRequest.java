package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.ReactTypes;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactRequest {

    @NotNull
    private ReactTypes reactType ;

    @NotNull
    private UUID announcementId ;
}
