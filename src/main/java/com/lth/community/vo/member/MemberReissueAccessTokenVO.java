package com.lth.community.vo.member;

import com.lth.community.security.vo.TokenVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReissueAccessTokenVO {
    private Boolean status;
    private String message;
    private HttpStatus code;
    private String grantType;
    private String accessToken;
}
