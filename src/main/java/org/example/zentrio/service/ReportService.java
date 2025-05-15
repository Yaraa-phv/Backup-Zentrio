package org.example.zentrio.service;

import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.Report;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    Report getReportByBoardId(UUID boardId);

    List<AllMember> getMember(UUID boardId);

}
