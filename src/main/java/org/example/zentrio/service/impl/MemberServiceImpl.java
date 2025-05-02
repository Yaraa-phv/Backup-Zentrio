package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.repository.RoleRepository;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.AppUserService;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.MemberService;
import org.example.zentrio.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AppUserService appUserService;
    private final BoardService boardService;
    private final WorkspaceRepository workspaceRepository;

    //    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public Member insertManagerToBoard(ManagerRequest managerRequest) {

        UUID userId = appUserService.getCurrentUserId();
        List<Workspace> workspace = workspaceRepository.getAllWorkspaces(userId);
//        UUID workspaceId = workspaceRepository.getWorkspaceById()
//        Board board = boardService



        return memberRepository.insertManagerToBoard(managerRequest);
    }
}