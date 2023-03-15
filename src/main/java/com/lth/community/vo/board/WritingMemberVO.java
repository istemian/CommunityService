package com.lth.community.vo.board;

import com.lth.community.entity.MemberInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WritingMemberVO {
    private String title;
    private String content;
}
