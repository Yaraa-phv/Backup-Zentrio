package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
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
    public GanttChart createGanttChart(UUID boardId, GanttChartRequest ganttChartRequest) {

        GanttChart ganttChart = ganttChartRepository.getGanttChartByBoardId(boardId);
        if (ganttChart != null) {
            throw new BadRequestException("Gantt Chart already exists");
        }
        UUID memberId = memberRepository.getMemberIdByUserIdAndBoardId(userId(), boardId);
        if (memberId == null) {
            throw new NotFoundException(" You are not in member this board in");
        }

        userRole(boardId);
        return  ganttChartRepository.createGanttChart(boardId, ganttChartRequest, LocalDateTime.now(),memberId);
    }


    @Override
    public GanttChart getGanttChartByBoardId(UUID boardId) {

        UUID existedBoardId = boardService.checkExistedBoardId(boardId);

        return ganttChartRepository.getGanttChartByBoardId(existedBoardId);
    }

    @Override
    public GanttChart updateGanttChartById(UUID ganttChartId, GanttChartRequest ganttChartRequest) {
        GanttChart chart= getGanttChartByID(ganttChartId);
        userRole(chart.getBoard_id());
        return  ganttChartRepository.updateGanttChartById(ganttChartId, ganttChartRequest, LocalDateTime.now());
    }

    @Override
    public Void deleteGanttChartByID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }
        userRole(ganttChart.getBoard_id());

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
