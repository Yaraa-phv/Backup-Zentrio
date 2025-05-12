package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.repository.RoleRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class GanttChartServiceImpl implements GanttChartService {

    private final GanttChartRepository ganttChartRepository;
    private final BoardService boardService;
    private final RoleRepository roleRepository;


    public UUID userID (){
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    @Override
    public GanttChart createGanttChart(UUID boardId, GanttChartRequest ganttChartRequest) {
        GanttChart ganttChart = ganttChartRepository.getGanttChartByBoardId(boardId);
        if (ganttChart != null) {
            throw new ConflictException("Gantt Chart already exists can not create twice");
        }
        String role= roleRepository.getRoleNameByUserIdAndBoardId(boardId, userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("You are not the manager of this board");
        }
        return  ganttChartRepository.createGanttChart(boardId, ganttChartRequest, LocalDateTime.now());
    }


    @Override
    public GanttChart getGanttChartByBoardId(UUID boardId) {
        Board  board=boardService.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board not found");
        }
        GanttChart chart= ganttChartRepository.getGanttChartByBoardId(boardId);
        if (chart == null) {
            throw new NotFoundException("Chart not found");
        }
        return chart;
    }

    @Override
    public GanttChart updateGannntChartById(UUID ganttChartId, GanttChartRequest ganttChartRequest) {
        GanttChart chart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (chart == null) {
            throw new NotFoundException("Chart not found");
        }
        String role= roleRepository.getRoleNameByUserIdAndBoardId(chart.getBoard_id(), userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("You are not the manager of this board");
        }
        return  ganttChartRepository.updateGannntChartById(ganttChartId, ganttChartRequest, LocalDateTime.now());
    }

    @Override
    public Void deleteGanttChartByID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }
        String role= roleRepository.getRoleNameByUserIdAndBoardId(ganttChart.getBoard_id(), userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("You are not the manager of this board");
        }

        return ganttChartRepository.deleteGanttChartByID(ganttChartId);
    }

    @Override
    public GanttChart getGanttChartByID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }
        return  ganttChart;
    }

}
