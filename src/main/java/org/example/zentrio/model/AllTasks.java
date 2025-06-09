package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.example.zentrio.dto.response.ChecklistResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTasks {
        private String task;
        private UUID taskId;
        private Integer total;
        private LocalDateTime startAt;
        private LocalDateTime finishAt;
        private List<ChecklistResponse> checklist;


}
