package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.MemberRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Member;
import org.example.zentrio.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Member Controller")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Get all members by board id")
    @GetMapping("/getAll/board-id/{board-id}")
    public ResponseEntity<ApiResponse<HashMap<String, Member>>> getAllMembersByBoardId(@PathVariable("board-id")UUID boardId){

        ApiResponse<HashMap<String, Member>> response = ApiResponse.<HashMap<String, Member>>builder()
                .success(true)
                .message("Get all members by board id successfully!")
                .payload(memberService.getAllMembersByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Invite members by board id")
    @GetMapping("/invite/board-id/{board-id}/email/{email}")
    public ResponseEntity<ApiResponse<HashMap<String, Member>>> inviteMembersByBoardIdAndEmails(@PathVariable("board-id")UUID boardId, @PathVariable("email") List<String> emails){

        ApiResponse<HashMap<String, Member>> response = ApiResponse.<HashMap<String, Member>>builder()
                .success(true)
                .message("Get all members by board id successfully!")
                .payload(memberService.inviteMembersByBoardIdAndEmails(boardId, emails))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit role for member by board id and member id")
    @PutMapping("/edit-role/board-id/{board-id}/member-id/{member-id}")
    public ResponseEntity<ApiResponse<Member>> editRoleForMembersByBoardIdAndMemberId(
            @PathVariable("board-id")UUID boardId,
            @PathVariable("member-id") UUID memberId,
            @RequestBody MemberRequest memberRequest){

        System.out.println("memberRequest : " + memberRequest);

        ApiResponse<Member> response = ApiResponse.<Member>builder()
                .success(true)
                .message("Get all members by board id successfully!")
                .payload(memberService.editRoleForMembersByBoardIdAndMemberId(boardId, memberId, memberRequest))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

}
