package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementImage {

    private UUID announcementImageId;
    private String imageUrl;
    private LocalDateTime creationDate;
    private UUID authorId;
    private UUID announcementId;
}
