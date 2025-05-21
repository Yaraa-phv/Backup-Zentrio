package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ChecklistResponse;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.AllTasks;
import org.example.zentrio.model.Report;
import org.example.zentrio.repository.ReportRepository;
import org.example.zentrio.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    @Override
    public Report getReportByBoardId(UUID boardId) {
        Report report = reportRepository.getReportByBoardId(boardId);
        if (report == null) {
            throw new NotFoundException("Report not found");
        }
        // Step 1: Count how many tasks per stage (task.getTask() is stage name)
        Map<String, Long> taskCountByStage = report.getAllTasks().stream()
                .collect(Collectors.groupingBy(AllTasks::getTask, Collectors.counting()));

        // Now fix the checklist null issue
        List<AllTasks> fixedTasks = new ArrayList<>();
        for (AllTasks task : report.getAllTasks()) {
            if (task.getTaskId() != null) {
                List<ChecklistResponse> checklists = reportRepository.getChecklistById(task.getTaskId());
                task.setChecklist(checklists);
            }
            task.setTotal(taskCountByStage.getOrDefault(task.getTask(), 0L).intValue());

            fixedTasks.add(task);
        }
        report.setAllTasks(fixedTasks);
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
    public Map<String, String> getAttachment(UUID checklistId) {
        return reportRepository.getAttachment(checklistId);
    }

    @Override
    public Set<ChecklistResponse> getChecklistById(UUID takId) {

        Set<ChecklistResponse>  response=new  HashSet<>(reportRepository.getChecklistById(takId));
        return response;
    }
}
