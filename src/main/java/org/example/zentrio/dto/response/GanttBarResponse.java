package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.Face;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GanttBarResponse {
    private String title;
    private Face face;
}
