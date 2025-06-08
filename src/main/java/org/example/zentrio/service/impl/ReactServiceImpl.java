package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ReactRequest;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.model.Announcement;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.React;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.repository.ReactRepository;
import org.example.zentrio.service.ReactService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactServiceImpl implements ReactService {

    private final ReactRepository reactRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final AnnouncementServiceImpl announcementService;



    public UUID userId() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }


    public  UUID memberId(UUID boardId) {
        UUID memberId = memberRepository.getMemberIdInboard(userId(), boardId);
        if (memberId == null) {
            throw new ForbiddenException("You are not member of this board");
        }
        return memberId;
    }

    @Override
    public React createReact(ReactRequest reactRequest) {
     Announcement announcement= announcementService.getAnnouncementById(reactRequest.getAnnouncementId());
        UUID authorId = memberId(announcement.getBoardId());
        Boolean existingReact= reactRepository.getExistingReact(announcement.getAnnouncementId(), authorId);
        if (existingReact) {
            throw new ConflictException("You are already member of this board");
        }
        return reactRepository.createReact(reactRequest, LocalDateTime.now(),authorId);
    }
}
