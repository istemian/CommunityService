package com.lth.community.api;

import com.lth.community.service.MemberService;
import com.lth.community.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPIController {
    private final MemberService memberService;
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseVO> postMemberLogin(@RequestBody LoginVO login) throws Exception {
        MemberLoginResponseVO response = memberService.login(login);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("")
    public ResponseEntity<MemberInfoVO> getMemberDetailInfo(Authentication authentication) {
        return new ResponseEntity<>(memberService.getMemberDetailInfo(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageVO> postMemberJoin(@RequestBody MemberJoinVO data) throws Exception {
        MessageVO response = memberService.memberJoin(data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("")
    public ResponseEntity<MessageVO> deleteMember(Authentication authentication) {
        MessageVO response = memberService.deleteMember(authentication.getName());
        return new ResponseEntity<>(response, response.getCode());
    }
    @PatchMapping("")
    public ResponseEntity<MessageVO> updateMember(Authentication authentication, @RequestBody MemberUpdateVO data) {
        MessageVO response = memberService.updateMember(authentication.getName(), data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("findId")
    public ResponseEntity<MessageVO> findMemberId(@RequestParam String email) {
        MessageVO response = memberService.findMemberId(email);
        return new ResponseEntity<>(response, response.getCode());
    }
    @GetMapping("findPw")
    public ResponseEntity<MessageVO> findMemberPw(@RequestBody MemberFindPw data) {
        MessageVO response = memberService.findMemberPw(data);
        return new ResponseEntity<>(response, response.getCode());
    }
}
