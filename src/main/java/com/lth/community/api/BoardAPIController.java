package com.lth.community.api;

import com.lth.community.service.BoardService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.WritingMemberVO;
import com.lth.community.vo.board.WritingNonMemberVO;
import com.lth.community.vo.member.MemberLoginResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardAPIController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestBody WritingMemberVO member) {
        MessageVO response = boardService.memberWriting(authentication.getName(), member);
        return new ResponseEntity<>(response, response.getCode());
    }
    @PostMapping("/non")
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestBody WritingNonMemberVO nonMemberVO) {
        MessageVO response = boardService.nonMemberWriting(nonMemberVO);
        return new ResponseEntity<>(response, response.getCode());
    }
}
