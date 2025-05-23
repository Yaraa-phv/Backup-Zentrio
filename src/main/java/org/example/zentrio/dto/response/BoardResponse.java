package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.Member;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponse {
    private Board board;
    private List<Member> members;
}
