package com.lth.community.api;

import com.lth.community.service.MemberService;
import com.lth.community.vo.LoginVO;
import com.lth.community.vo.MemberInfoVO;
import com.lth.community.vo.MemberJoinVO;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.MemberLoginResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPIController {
    private final MemberService memberSecurityService;
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseVO> postMemberLogin(@RequestBody LoginVO login) {
        MemberLoginResponseVO response = memberSecurityService.login(login);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("")
    public ResponseEntity<MemberInfoVO> getMemberDetailInfo(Authentication authentication) {
        return new ResponseEntity<>(memberSecurityService.getMemberDetailInfo(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageVO> postMemberJoin(@RequestBody MemberJoinVO data) {
        return new ResponseEntity<>(memberSecurityService.memberJoin(data), HttpStatus.CREATED);
    }
}
