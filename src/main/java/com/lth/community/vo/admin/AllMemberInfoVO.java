package com.lth.community.vo.admin;

import com.lth.community.entity.DeleteMemberInfoEntity;
import com.lth.community.entity.SuspensionMemberInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private SuspensionMemberInfoEntity suspensionDt;
    private DeleteMemberInfoEntity deleteDt;
}
