package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class GanttChartServiceImpl implements GanttChartService {

    private final GanttChartRepository ganttChartRepository;
    private final BoardService boardService;
    private final MemberRepository memberRepository;

    public UUID userId(){
        AppUser authentication = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  authentication.getUserId();

    }
    @Override
    public void userRole(UUID boardID){
        boardService.getBoardByBoardId(boardID);
        String role = memberRepository.getRolePMByBoardIdAndUserId(boardID, userId());
        if (role == null) {
            throw new NotFoundException(" You are not in MANAGER this board in");
        }
        if(!role.equals(RoleName.ROLE_MANAGER.name())){
            throw new BadRequestException("Only MANAGER roles are allowed");
        }
        System.out.println(role);
    }



    @Override
    public GanttChart createGanttChart( GanttChartRequest ganttChartRequest) {

        GanttChart ganttChart = ganttChartRepository.getGanttChartByBoardId(ganttChartRequest.getBoardId());
        if (ganttChart != null) {
            throw new ConflictException("Gantt Chart already exists");
        }
        UUID memberId = memberRepository.getPmId(userId(), ganttChartRequest.getBoardId());
        if (memberId == null) {
            throw new ForbiddenException(" You are not in member this board in");
        }

        userRole(ganttChartRequest.getBoardId());
        return  ganttChartRepository.createGanttChart( ganttChartRequest, LocalDateTime.now(),memberId);
    }


    @Override
    public GanttChart getGanttChartByBoardId(UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        GanttChart  chart= ganttChartRepository.getGanttChartByBoardId(boardId);
        if (chart == null) {
            throw new NotFoundException(" You are not in MANAGER this board in");
        }
        return chart;
    }

    @Override
    public GanttChart updateGanttChartById(UUID ganttChartId,UUID boardId ,GanttChartRequest ganttChartRequest) {
        GanttChart chart= getGanttChartByID(ganttChartId);
        userRole(chart.getBoardId());
        return  ganttChartRepository.updateGanttChartById(ganttChartId,boardId, ganttChartRequest, LocalDateTime.now());
    }

    @Override
    public Void deleteGanttChartByID(UUID ganttChartId, UUID boardId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }
        userRole(ganttChart.getBoardId());

        return ganttChartRepository.deleteGanttChartByID(ganttChartId,boardId);
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
