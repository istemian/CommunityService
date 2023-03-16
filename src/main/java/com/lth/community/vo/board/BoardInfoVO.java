package com.lth.community.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardInfoVO {
    private Long no;
    private String nickname;
    private String title;
    private LocalDateTime creatDt;
    private LocalDateTime modifiedDt;
}
