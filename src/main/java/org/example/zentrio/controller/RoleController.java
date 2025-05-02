package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Role;
import org.example.zentrio.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Role Controller")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get all roles")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles(){
        ApiResponse<List<Role>> response = ApiResponse.<List<Role>> builder()
                .success(true)
                .message("Get all roles successfully!")
                .status(HttpStatus.OK)
                .payload(roleService.getAllRoles())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get role name by role id")
    @GetMapping("role-id/{role-id}")
    public ResponseEntity<ApiResponse<Role>> getRoleNameByRoleId(@PathVariable("role-id") UUID roleId){
        ApiResponse<Role> response = ApiResponse.<Role> builder()
                .success(true)
                .message("Get role name by role id successfully!")
                .status(HttpStatus.OK)
                .payload(roleService.getRoleNameByRoleId(roleId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get role id by role name")
    @GetMapping("/role-name/{role-name}")
    public ResponseEntity<ApiResponse<Role>> getRoleNameByRoleId(@PathVariable("role-name") RoleName roleName){
        ApiResponse<Role> response = ApiResponse.<Role> builder()
                .success(true)
                .message("Get role name by role id successfully!")
                .status(HttpStatus.OK)
                .payload(roleService.getRoleIdByRoleName(roleName))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}