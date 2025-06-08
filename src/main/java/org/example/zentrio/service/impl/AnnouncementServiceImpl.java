package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AnnouncementRequest;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Announcement;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.repository.AnnouncementRepository;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.service.AnnouncementService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final BoardServiceImpl boardService;
    private final MemberRepository memberRepository;


    public UUID userId() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    public Boolean existingMember() {
        return null;
    }

    public UUID getPmId( UUID boardId ) {
        UUID pmId= memberRepository.getPmId(userId(),boardId);
        if ( pmId == null ) {
            throw new ForbiddenException("Only Pm allow to create announcement");
        }
        return pmId;
    }

    @Override
    public Announcement createAnnouncement(AnnouncementRequest announcementRequest) {
        Board board= boardService.getBoardByBoardId(announcementRequest.getBoardId());
        UUID pmId= getPmId(board.getBoardId());
        return announcementRepository.createAnnouncement(announcementRequest, pmId);
    }

    @Override
    public Announcement getAnnouncementById(UUID announcementId) {
        Announcement announcement= announcementRepository.getAnnouncementById(announcementId);
        if (announcement == null) {
            throw new NotFoundException("Announcement not found");
        }
        return announcement;
    }

    @Override
    public Announcement updateAnnouncementById(UUID announcementId, String content) {
        Announcement  announcement= getAnnouncementById(announcementId);
        UUID pmId= getPmId(announcement.getBoardId());
        if (!announcement.getAuthorId().equals(pmId)) {
            throw new ForbiddenException("Only Pm that created announcement it  allow to update announcement");
        }
        return announcementRepository.updateAnnouncementById(announcementId, content, LocalDateTime.now());
    }

    @Override
    public Announcement updateAnnouncementPinnedById(UUID announcementId) {
        Announcement  announcement= getAnnouncementById(announcementId);
        UUID pmId= getPmId(announcement.getBoardId());
        if (!announcement.getAuthorId().equals(pmId)) {
            throw new ForbiddenException("Only Pm that created announcement it  allow to update announcement");
        }
        Announcement announcement1;
        if (announcement.getIsPinned()== false){
            announcement1=  announcementRepository.updateAnnouncementPinnedById(true , announcement.getAnnouncementId());
        }else{
            announcement1  =   announcementRepository.updateAnnouncementPinnedById(false , announcement.getAnnouncementId());
        }
        return announcement1;
    }

    @Override
    public Void deletedAnnouncementPinnedById(UUID announcementId) {
        Announcement  announcement= getAnnouncementById(announcementId);
        UUID pmId= getPmId(announcement.getBoardId());
        if (!announcement.getAuthorId().equals(pmId)) {
            throw new ForbiddenException("Only Pm that created announcement it  allow to update announcement");
        }
        announcementRepository.deletedAnnouncementPinnedById(announcement.getAnnouncementId(), pmId);
        return null;
    }
}
