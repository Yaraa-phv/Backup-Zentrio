package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private UUID reportId;
    private LocalDate creationDate;
    private Integer version;
    private  UUID boardId;
    private List<AllMember> allMembers;
    private String boardName;
    private List<AllTasks> allTasks;
}
