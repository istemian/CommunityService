package com.lth.community.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostNonMemberVO {
    private Long postNo;
    private String pw;
    private String title;
    private String content;
}
