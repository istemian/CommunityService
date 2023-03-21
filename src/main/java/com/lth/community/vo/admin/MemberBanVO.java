package com.lth.community.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberBanVO {
    @Schema(description = "회원 아이디")
    private String id;
    @Schema(description = "정지를 원하는 일 수", example = "7")
    private Integer endDt;
    @Schema(description = "정지 사유", example = "욕설")
    private String reason;
}
