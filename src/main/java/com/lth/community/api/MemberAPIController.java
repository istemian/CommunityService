package com.lth.community.api;

import com.lth.community.service.MemberService;
import com.lth.community.vo.*;
import com.lth.community.vo.board.BoardInfoVO;
import com.lth.community.vo.board.GetBoardVO;
import com.lth.community.vo.comment.GetMyCommentVO;
import com.lth.community.vo.member.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 CRUD API 입니다.")
public class MemberAPIController {
    private final MemberService memberService;
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseVO> postMemberLogin(@RequestBody LoginVO login) {
        MemberLoginResponseVO response = memberService.login(login);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "본인 정보 조회")
    @GetMapping("")
    public ResponseEntity<MemberInfoVO> getMemberDetailInfo(Authentication authentication) {
        return new ResponseEntity<>(memberService.getMemberDetailInfo(authentication.getName()), HttpStatus.OK);
    }

    @Operation(summary = "회원 가입")
    @PostMapping("")
    public ResponseEntity<MessageVO> postMemberJoin(@RequestBody MemberJoinVO data) {
        MessageVO response = memberService.memberJoin(data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시 일주일동안 보관 후 DB에서 삭제되며 관련된 모든 정보가 삭제됩니다.")
    @DeleteMapping("")
    public ResponseEntity<MessageVO> deleteMember(Authentication authentication) {
        MessageVO response = memberService.deleteMember(authentication.getName());
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "회원 수정", description = "원하는 정보를 수정할 수 있습니다. 하나씩도 가능합니다.")
    @PatchMapping("")
    public ResponseEntity<MessageVO> updateMember(Authentication authentication, @RequestBody MemberUpdateVO data) {
        MessageVO response = memberService.updateMember(authentication.getName(), data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "아이디 찾기", description = "이메일을 입력할 시 아이디를 return 합니다.")
    @PostMapping("/findId")
    public ResponseEntity<MessageVO> findMemberId(@RequestBody MemberFindIdVO email) {
        MessageVO response = memberService.findMemberId(email);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "비밀번호 찾기", description = "아이디와 이름, 이메일을 입력하면 임시비밀번호로 설정되며 이를 이메일로 전송합니다.")
    @PostMapping("/findPw")
    public ResponseEntity<MessageVO> findMemberPw(@RequestBody MemberFindPwVO data) {
        MessageVO response = memberService.findMemberPw(data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "내 게시글 조회", description = "내가 쓴 게시글을 전체 조회합니다.")
    @GetMapping("/myPost")
    public ResponseEntity<List<BoardInfoVO>> getMyPost(Authentication authentication) {
        return new ResponseEntity<>(memberService.getMyPost(authentication.getName()), HttpStatus.OK);
    }

    @Operation(summary = "내 댓글 조회", description = "내가 쓴 댓글을 전체 조회합니다.")
    @GetMapping("/myComment")
    public ResponseEntity<List<GetMyCommentVO>> getMyComment(Authentication authentication) {
        return new ResponseEntity<>(memberService.getMyComment(authentication.getName()), HttpStatus.OK);
    }
}
