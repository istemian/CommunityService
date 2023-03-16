package com.lth.community.api;

import com.lth.community.service.BoardService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.GetBoardVO;
import com.lth.community.vo.board.WritingMemberVO;
import com.lth.community.vo.board.WritingNonMemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardAPIController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestBody WritingMemberVO member) {
        MessageVO response = boardService.writing(authentication.getName(), member);
        return new ResponseEntity<>(response, response.getCode());
    }
    @PostMapping("/non")
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestBody WritingNonMemberVO nonMemberVO) {
        MessageVO response = boardService.nonWriting(nonMemberVO);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<MessageVO> deletePost(Authentication authentication, @PathVariable Long no) {
        MessageVO response = boardService.delete(authentication.getName(), no);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/non/{no}/{pw}")
    public ResponseEntity<MessageVO> nonDeletePost(@PathVariable Long no, @PathVariable String pw) {
        MessageVO response = boardService.nonDelete(no, pw);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("")
    public ResponseEntity<GetBoardVO> getBoard(@RequestParam @Nullable String keyword, @PageableDefault(size = 10, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoard(keyword, pageable), HttpStatus.OK);
    }
}
