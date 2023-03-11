package com.lth.community.vo;

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
public class MemberLoginResponseVO {
    private Boolean status;
    private String message;
    private HttpStatus code;
    private TokenVO token;
}
