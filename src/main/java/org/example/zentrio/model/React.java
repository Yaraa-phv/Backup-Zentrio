package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.response.MemberResponseData;
import org.example.zentrio.enums.ReactTypes;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class React {

    private  UUID reactId ;
    private ReactTypes reactType ;
    private LocalDateTime reactDate ;
    private UUID authorId ;
    private MemberResponseData commentBy;
    private UUID announcementId ;
}
