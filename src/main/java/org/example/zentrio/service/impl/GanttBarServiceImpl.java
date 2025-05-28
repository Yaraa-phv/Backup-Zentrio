package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.GanttBarRepository;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.service.GanttBarService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GanttBarServiceImpl implements GanttBarService {

    private final GanttBarRepository ganttBarRepository;
    private final GanttChartRepository ganttChartRepository;
    private final BoardRepository boardRepository;

    @Override
    public GanttBar createGanttBarByGanttChartId(GanttBarRequest ganttBarRequest) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        GanttChart ganttChart = ganttChartRepository.getGanttChartByGanttChartId(ganttBarRequest.getGanttChartId());
        if (ganttChart == null) {
            throw new BadRequestException("GanttChart not found");
        }
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId, ganttChart.getBoardId());
        if(memberId == null) {
            throw new ForbiddenException("You're not a manager of this board can't create Gantt bar");
        }

        return ganttBarRepository.createGanttBarByGanttChartId(ganttBarRequest, ganttBarRequest.getGanttChartId());
    }

    @Override
    public GanttBar getGanttBarById(UUID ganttBarId, UUID ganttChartId) {
        GanttBar ganttBar = ganttBarRepository.getGanttBarById(ganttBarId,ganttChartId);
        if (ganttBar == null) {
            throw new BadRequestException("Gantt bar with id " + ganttBarId + " not found");
        }
        return ganttBar;
    }

    @Override
    public HashSet<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId) {
        GanttChart ganttChart = ganttChartRepository.getGanttChartByGanttChartId(ganttChartId);
        if (ganttChart == null) {
            throw new BadRequestException("Gantt chart not found");
        }
        return ganttBarRepository.getAllGanttBarsByGanttChartId(ganttChartId);
    }

    @Override
    public GanttBar updateGanttBarByGanttBarId(GanttBarRequest ganttBarRequest, UUID ganttBarId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        getGanttBarById(ganttBarId,ganttBarRequest.getGanttChartId());
        GanttChart ganttChart = ganttChartRepository.getGanttChartByGanttChartId(ganttBarRequest.getGanttChartId());
        if (ganttChart == null) {
            throw new BadRequestException("Gantt chart not found");
        }
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId,ganttChart.getBoardId());
        if(memberId == null) {
            throw new ForbiddenException("You're not a manager of this board can't updated Gantt bar");
        }
        return ganttBarRepository.updateGanttBarByGanttBarId(ganttBarRequest,ganttBarId,ganttBarRequest.getGanttChartId());
    }

    @Override
    public void deleteGanttBarByGanttBarId(UUID ganttBarId,UUID ganttChartId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        GanttChart ganttChart = ganttChartRepository.getGanttChartByGanttChartId(ganttChartId);
        if (ganttChart == null) {
            throw new BadRequestException("Gantt chart not found");
        }
        getGanttBarById(ganttBarId,ganttChartId);
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId,ganttChart.getBoardId());
        if(memberId == null) {
            throw new ForbiddenException("You're not a manager of this board can't deleted Gantt bar");
        }
        ganttBarRepository.deleteGanttBarByGanttBarId(ganttBarId,ganttChartId);
    }

    @Override
    public HashSet<GanttBar> getAllGanttBarsForCurrentUser() {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return ganttBarRepository.getAllGanttBarsForCurrentUser(userId);
    }

    @Override
    public HashSet<GanttBar> getAllGanttBars() {
        return ganttBarRepository.getAllGanttBars();
    }
}
