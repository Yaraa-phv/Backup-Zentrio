package org.example.zentrio.service;

import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;

public interface MemberService {
    Member insertManagerToBoard(ManagerRequest managerRequest);

//    Member insertManagerToBoard(UUID roleId, UUID boardId);
}
