package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Member Controller is Not Done")
public class MemberController {

    private final MemberService memberService;
//
//    @Operation(summary = "Invite member by email")
//    @PostMapping("/invite")
//    public ResponseEntity<ApiResponse<Member>> inviteMemberByEmail(@RequestBody MemberRequest memberRequest){
//        ApiResponse<Member> response = ApiResponse.<Member> builder()
//                .success(true)
//                .message("Created board successfully!")
//                .status(HttpStatus.CREATED)
//                .payload(null)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(summary = "Invite member by email")
//    @PostMapping()
//    public ResponseEntity<ApiResponse<Member>> insertManagerToBoard(@RequestBody MemberRequest memberRequest){
//        ApiResponse<Member> response = ApiResponse.<Member> builder()
//                .success(true)
//                .message("Created board successfully!")
//                .status(HttpStatus.CREATED)
//                .payload(memberService.insertManagerToBoard(memberRequest))
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
}