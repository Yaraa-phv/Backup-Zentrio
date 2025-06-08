package org.example.zentrio.service;

import org.example.zentrio.dto.request.AnnouncementRequest;
import org.example.zentrio.model.Announcement;

import java.util.UUID;

public interface AnnouncementService {


    Announcement createAnnouncement(AnnouncementRequest announcementRequest);

    Announcement getAnnouncementById(UUID announcementId);


    Announcement updateAnnouncementById(UUID announcementId, String content);

    Announcement updateAnnouncementPinnedById(UUID announcementId);


    Void deletedAnnouncementPinnedById(UUID announcementId);
}
