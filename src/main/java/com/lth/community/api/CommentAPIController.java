package com.lth.community.api;

import com.lth.community.service.CommentService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.DeletePostNonMemberVO;
import com.lth.community.vo.board.WritingMemberVO;
import com.lth.community.vo.board.WritingNonMemberVO;
import com.lth.community.vo.comment.CommentMemberVO;
import com.lth.community.vo.comment.CommentNonMemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentAPIController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestBody CommentMemberVO member, @RequestParam Long postNo) {
        MessageVO response = commentService.writing(authentication.getName(), postNo, member);
        return new ResponseEntity<>(response, response.getCode());
    }
    @PostMapping("/non")
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestBody CommentNonMemberVO nonMemberVO, @RequestParam Long postNo) {
        MessageVO response = commentService.nonWriting(nonMemberVO, postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<MessageVO> deletePost(Authentication authentication, @PathVariable Long no) {
        MessageVO response = commentService.delete(authentication.getName(), no);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/non/{no}")
    public ResponseEntity<MessageVO> nonDeletePost(@PathVariable Long no, @RequestBody DeletePostNonMemberVO data) {
        MessageVO response = commentService.nonDelete(no, data);
        return new ResponseEntity<>(response, response.getCode());
    }
}