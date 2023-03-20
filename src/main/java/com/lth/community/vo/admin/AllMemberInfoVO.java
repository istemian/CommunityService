package com.lth.community.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllMemberInfoVO {
    private Long seq;
    private String memberId;
    private String name;
    private String email;
    private String nickname;
    private Integer status;
    private String role;
    private LocalDateTime createDt;
    private LocalDateTime banEndDt;
    private String banReason;
    private LocalDateTime deleteDt;
}
