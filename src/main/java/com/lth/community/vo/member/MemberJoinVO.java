package com.lth.community.vo.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinVO {
    private String id;
    private String pw;
    private String name;
    private String nickname;
    private String email;
    private LocalDateTime createDt;
}
