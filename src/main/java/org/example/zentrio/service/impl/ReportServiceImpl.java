package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ChecklistResponse;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.Report;
import org.example.zentrio.repository.ReportRepository;
import org.example.zentrio.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    @Override
    public Report getReportByBoardId(UUID boardId) {
        Report report= reportRepository.getReportByBoardId(boardId);
        if (report == null) {
            throw new NotFoundException("report not found");
        }
        return report;
    }

    @Override
    public List<AllMember> getMember(UUID boardId) {

        return reportRepository.getMember(boardId);
    }

    @Override
    public Map<String, String> getAttachment(UUID checklistId) {
        return reportRepository.getAttachment(checklistId);
    }

    @Override
    public Set<ChecklistResponse> getChecklistById(UUID checklistId) {

        Set<ChecklistResponse>  response=new  HashSet<>(reportRepository.getChecklistById(checklistId));


        return response;
    }
}
