package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.Report;
import org.example.zentrio.repository.ReportRepository;
import org.example.zentrio.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
