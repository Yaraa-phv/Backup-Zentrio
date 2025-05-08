package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.MemberRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Member;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.repository.RoleRepository;
import org.example.zentrio.service.AuthService;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.MemberService;
import org.example.zentrio.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final TaskService taskService;

    @Override
    public HashMap<String, Member> getAllMembersByBoardId(UUID boardId) {

        boardId = boardService.checkExistedBoardId(boardId);
        if (boardId == null){
            throw new NotFoundException("Board id is not found to get all members!");
        }

        HashMap<String, Member> members = new HashMap<>();
        for (Member m : memberRepository.getAllMembersByBoardId(boardId)){
            members.put(m.getMemberId().toString(), m);
        }

        return members;

    }

    @Override
    public HashMap<String, Member> inviteMembersByBoardIdAndEmails(UUID boardId, List<String> emails) {

        return null;
    }

    @Override
    public Member editRoleForMembersByBoardIdAndMemberId(UUID boardId, UUID memberId, MemberRequest memberRequest) {

        String requestRoleName = memberRequest.getRoleName().toString();
        System.out.println("requestRoleName : "+requestRoleName);

        UUID currentUserId = authService.getCurrentAppUserId();
        memberId = checkExistedMemberByBoardId(boardId, memberId, currentUserId);
        if (memberId == null){
            throw new NotFoundException("Member id is not found!");
        }
        System.out.println("Role name : " + requestRoleName);

        UUID roleId = roleRepository.getRoleIdByRoleName(requestRoleName);
        System.out.println("Role Id : " + roleId);
        if (roleId == null){
            throw new BadRequestException("Role name does not found in here!");
        }

        return memberRepository.editRoleForMembersByBoardIdAndMemberId(memberId, roleId);
    }

    @Override
    public UUID checkExistedMemberByBoardId(UUID boardId, UUID memberId, UUID currentUserId) {

        boardId = boardService.checkExistedBoardId(boardId);
        boardId = taskService.checkExistedBoardByUserId(currentUserId, boardId);
        if (boardId == null){
            throw new NotFoundException("Board id is not found to get all members!");
        }

        memberId = memberRepository.getMemberIdByBoardIdAndMemberId(boardId, memberId);
        if (memberId == null){
            throw new NotFoundException("Member id is not found!");
        }

        return memberId;
    }
}
