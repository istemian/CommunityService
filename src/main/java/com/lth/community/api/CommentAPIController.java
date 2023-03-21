package com.lth.community.api;

import com.lth.community.service.CommentService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.DeletePostNonMemberVO;
import com.lth.community.vo.board.WritingMemberVO;
import com.lth.community.vo.board.WritingNonMemberVO;
import com.lth.community.vo.comment.CommentMemberVO;
import com.lth.community.vo.comment.CommentNonMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 CD API 입니다.")
public class CommentAPIController {
    private final CommentService commentService;

    @Operation(summary = "회원 댓글 등록")
    @PostMapping("")
    public ResponseEntity<MessageVO> writingComment(Authentication authentication, @RequestBody CommentMemberVO member, @RequestParam Long postNo) {
        MessageVO response = commentService.writing(authentication.getName(), postNo, member);
        return new ResponseEntity<>(response, response.getCode());
    }
    @Operation(summary = "비회원 댓글 등록", description = "아이디와 비밀번호를 입력하여 비회원이라도 댓글 등록이 가능합니다.")
    @PostMapping("/non")
    public ResponseEntity<MessageVO> writingCommentNonMember(@RequestBody CommentNonMemberVO nonMemberVO, @RequestParam Long postNo) {
        MessageVO response = commentService.nonWriting(nonMemberVO, postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "회원 댓글 삭제")
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<MessageVO> deleteComment(Authentication authentication, @PathVariable Long commentNo) {
        MessageVO response = commentService.delete(authentication.getName(), commentNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "비회원 댓글 삭제", description = "비밀번호가 일치하면 댓글을 삭제할 수 있습니다.")
    @DeleteMapping("/non/{commentNo}")
    public ResponseEntity<MessageVO> nonDeleteComment(@PathVariable Long commentNo, @RequestBody DeletePostNonMemberVO data) {
        MessageVO response = commentService.nonDelete(commentNo, data);
        return new ResponseEntity<>(response, response.getCode());
    }
}
