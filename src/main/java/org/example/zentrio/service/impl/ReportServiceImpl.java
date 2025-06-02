package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ChecklistResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.AllTasks;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Report;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.repository.ReportRepository;
import org.example.zentrio.repository.TaskRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.ReportService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final BoardService  boardService;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public UUID userId (){
        AppUser appUser= (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }


    @Override
    public Report getReportByBoardId(UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        String role= memberRepository.getRolePMByBoardIdAndUserId(boardId,userId());
        UUID pmID= memberRepository.getPmId(userId(),boardId);
        if (pmID==null){
            throw  new BadRequestException("You are not PM of this board");
        }
        if(role==null){
            throw new BadRequestException("You are not allow to get report by this board");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())){
            throw new BadRequestException("Only manager can view report by this board");
        }
        Boolean existReport= reportRepository.getExistReport(boardId, pmID);
        if(!existReport){
            reportRepository.createReport(LocalDateTime.now(),boardId,pmID);
        }


        Report report = reportRepository.getReportByBoardId(boardId);
        if (report == null) {
            throw new NotFoundException("Report not found");
        }

        Map<String, List<AllTasks>> tasksByStage = report.getAllTasks().stream()
                .collect(Collectors.groupingBy(AllTasks::getTask));

        List<AllTasks> mergedTasks = new ArrayList<>();

        for (String stage : tasksByStage.keySet()) {
            List<AllTasks> stageTasks = tasksByStage.get(stage);

            List<ChecklistResponse> allChecklists = new ArrayList<>();
            for (AllTasks task : stageTasks) {
                if (task.getTaskId() != null) {
                    allChecklists.addAll(reportRepository.getChecklistById(task.getTaskId()));
                }
            }

            AllTasks merged = new AllTasks();
            merged.setTask(stage);
            merged.setTaskId(null); // Optional
            merged.setTotal(stageTasks.size());
            merged.setChecklist(allChecklists);

            mergedTasks.add(merged);
        }

        report.setAllTasks(mergedTasks);
        return report;
    }


//    Report report = reportRepository.getReportByBoardId(boardId);
//    List<AllTasks> tasks = report.getAllTasks();
//
//    Map<String, Long> countByStage = tasks.stream()
//            .collect(Collectors.groupingBy(AllTasks::getTask, Collectors.counting()));
//
//
//for (AllTasks task : tasks) {
//        task.setTotal(countByStage.get(task.getTask()).intValue());
//    }




    @Override
    public List<AllMember> getMember(UUID boardId) {

        return reportRepository.getMember(boardId);
    }

    @Override
    public Map<String, Object> getAttachment(UUID checklistId) {
        return reportRepository.getAttachment(checklistId);
    }

    @Override
    public Set<ChecklistResponse> getChecklistById(UUID takId) {

        Set<ChecklistResponse>  response=new  HashSet<>(reportRepository.getChecklistById(takId));
        return response;
    }

    @Override
    public void updateVersionReport(Report report) {
        int version = report.getVersion() +1;
        reportRepository.updateVersion(report.getReportId(), version);

    }
}
