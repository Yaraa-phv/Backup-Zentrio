package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.GanttBarRepository;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.repository.RoleRepository;
import org.example.zentrio.service.GanttBarService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GanttBarServiceImpl implements GanttBarService {

    private final GanttBarRepository ganttBarRepository;
    private final GanttChartService ganttChartService;
    private final RoleRepository roleRepository;

    public UUID userID (){
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    @Override
    public GanttBar creatGanntBar(UUID ganntChartId , GanttBarRequest ganttBarRequest) {

       GanttChart ganttChart = ganttChartService.getGanttChartByID(ganntChartId);
       if (ganttChart == null) {
           throw new NotFoundException("GanttChart not found");
       }
       String role = roleRepository.getRoleNameByUserIdAndBoardId(ganttChart.getBoard_id(), userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
       if (!role.equals(RoleName.ROLE_MANAGER.name())) {
           throw new BadRequestException("only ROLE_MANAGER allowed");
       }
        return ganttBarRepository.creatGanntBar(ganntChartId,ganttBarRequest);
    }



    @Override
    public HashMap<String,GanttBar> getAllGanttBarByGanttChartID(UUID ganntChartId) {
        GanttChart ganttChart= ganttChartService.getGanttChartByID(ganntChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart not found");
        }
        HashMap<String,GanttBar> barHashMap = new HashMap<>();
        for( GanttBar bar: ganttBarRepository.getAllGanttBarByGanttChartID(ganntChartId) ){
            barHashMap.put(bar.getGanttBarId().toString(), bar);
        }

        return barHashMap;
    }

    @Override
    public GanttBar getGanttBarByGanttBarID(UUID geanntbarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBarID(geanntbarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        return ganttBar;
    }

    @Override
    public GanttBar updateGanttBarByGanttBarId(UUID ganntbarId, GanttBarRequest ganttBarRequest) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBarID(ganntbarId);


        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        GanttChart ganttChart= ganttChartService.getGanttChartByID(ganttBar.getGanttChartId());
        String role= roleRepository.getRoleNameByUserIdAndBoardId(ganttChart.getBoard_id(), userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("only ROLE_MANAGER allowed");
        }
        return ganttBarRepository.updateGanttBarByGanttBarId(ganntbarId,ganttBarRequest);
    }

    @Override
    public void deleteGanttBarByGanttBarId(UUID geanttbarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBarID(geanttbarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar already  deleted");
        }
        GanttChart ganttChart= ganttChartService.getGanttChartByID(ganttBar.getGanttChartId());
        String role= roleRepository.getRoleNameByUserIdAndBoardId(ganttChart.getBoard_id(), userID());
        if (role == null) {
            throw new BadRequestException("you don't have a role in this board can not delete");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("only ROLE_MANAGER allowed");
        }
        ganttBarRepository.deleteGanttBarByGanttBarId(geanttbarId);
    }

    @Override
    public GanttBar getGanttBarByGanttChartIdAndGanttBarId(UUID ganttChartId, UUID ganttBarId) {

        if (ganttBarId == null){
            throw new NotFoundException("Gantt Bar Id not found!");
        }
        return ganttBarRepository.getGanttBarByGanttChartIdAndGanttBarId(ganttChartId, ganttBarId);
    }

}
