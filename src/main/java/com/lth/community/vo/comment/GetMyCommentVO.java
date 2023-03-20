package com.lth.community.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMyCommentVO {
    private Long postNo;
    private String postTitle;
    private Long no;
    private String nickname;
    private String comment;
    private LocalDateTime createDt;
}
