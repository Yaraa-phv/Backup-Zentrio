package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.service.GanttChartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class GanttChartServiceImpl implements GanttChartService {

    private final GanttChartRepository ganttChartRepository;
    private final BoardRepository boardRepository;

    @Override
    public GanttChart createGanttChartByBoardId(GanttChartRequest ganttChartRequest) {

        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        Board board = boardRepository.getBoardByBoardId(ganttChartRequest.getBoardId());
        if (board == null) {
            throw new NotFoundException("Board not found");
        }
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId, board.getBoardId());
        if (memberId == null) {
            throw new ForbiddenException("You're not a manager in this board can't created gantt chart");
        }

        GanttChart existingGanttChart = ganttChartRepository.getGanttChartByBoardId(board.getBoardId());

        if (existingGanttChart != null) {
            throw new ConflictException("Gantt chart for board id " + board.getBoardId() + " already created");
        }

        // Create a new Gantt Chart
        LocalDateTime now = LocalDateTime.now();
        GanttChart ganttChart = new GanttChart();
        ganttChart.setTitle(ganttChartRequest.getTitle());
        ganttChart.setBoardId(board.getBoardId());
        ganttChart.setCreatedAt(now);
        ganttChart.setUpdatedAt(now);

        return ganttChartRepository.createGanttChartByBoardId(ganttChartRequest, board.getBoardId(), memberId);
    }

    @Override
    public GanttChart getGanttChartById(UUID ganttChartId, UUID boardId) {
        GanttChart ganttChart = ganttChartRepository.getGanttChartByIdAndBoardId(ganttChartId, boardId);
        if (ganttChart == null) {
            throw new NotFoundException("Gantt chart with " + ganttChartId + " not found");
        }
        return ganttChart;
    }

    @Override
    public void deleteGanttChartById(UUID ganttChartId, UUID boardId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        getGanttChartById(ganttChartId,boardId);
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId, boardId);
        if (memberId == null) {
            throw new ForbiddenException("You're not a manager in this board can't deleted");
        }
        ganttChartRepository.deleteGanttChartById(ganttChartId, boardId);
    }

    @Override
    public GanttChart getGanttChartByCurrentUser() {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return ganttChartRepository.getGanttChartByCurrentUser(userId);
    }

    @Override
    public GanttChart getAllGanttChartByBoardId(UUID boardId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board not found");
        }
        return ganttChartRepository.getAllGanttChartByBoardId(boardId);
    }

    @Override
    public GanttChart updateGanttChartByGanttChartId(GanttChartRequest ganttChartRequest, UUID ganttChartId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        getGanttChartById(ganttChartId, ganttChartRequest.getBoardId());
        UUID memberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId, ganttChartRequest.getBoardId());
        if (memberId == null) {
            throw new ForbiddenException("You're not a manager in this board can't updated gantt chart");
        }
        return ganttChartRepository.updateGanttChartByGanttChartId(ganttChartRequest, ganttChartId, ganttChartRequest.getBoardId());
    }


}
