package org.example.zentrio.service;

import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.dto.request.MemberRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Member;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface MemberService {

    HashMap<String, Member> getAllMembersByBoardId(UUID boardId);

    HashMap<String, Member> inviteMembersByBoardIdAndEmails(UUID boardId, List<String> emails);

    Member editRoleForMembersByBoardIdAndMemberId(UUID boardId, UUID memberId, MemberRequest memberRequest);

    UUID checkExistedMemberByBoardId(UUID boardId, UUID memberId, UUID currentUserId);
}
