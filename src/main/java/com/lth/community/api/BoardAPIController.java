package com.lth.community.api;

import com.lth.community.service.BoardService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardAPIController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestPart(required = false) MultipartFile[] files, WritingMemberVO member) {
        MessageVO response = boardService.writing(authentication.getName(), member, files);
        return new ResponseEntity<>(response, response.getCode());
    }
    @PostMapping("/non")
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestPart(required = false) MultipartFile[] files, WritingNonMemberVO nonMemberVO) {
        MessageVO response = boardService.nonWriting(nonMemberVO, files);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<MessageVO> deletePost(Authentication authentication, @PathVariable Long no) {
        MessageVO response = boardService.delete(authentication.getName(), no);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/non/{no}")
    public ResponseEntity<MessageVO> nonDeletePost(@PathVariable Long no, @RequestBody DeletePostNonMemberVO data) {
        MessageVO response = boardService.nonDelete(no, data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("")
    public ResponseEntity<GetBoardVO> getBoard(@RequestParam @Nullable String keyword, @PageableDefault(size = 10, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoard(keyword, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{no}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody WritingMemberVO data, Authentication authentication, @PathVariable Long no) {
        MessageVO response = boardService.update(data, authentication.getName(), no);
        return new ResponseEntity<>(response, response.getCode());
    }

    @PatchMapping("/non/{no}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody UpdatePostNonMember data, @PathVariable Long no) {
        MessageVO response = boardService.nonUpdate(data, no);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("/file/{file}")
    public ResponseEntity<Resource> getFile(@PathVariable String file ,HttpServletRequest request) throws Exception {
        return boardService.getFile(file, request);
    }

    @GetMapping("/{no}/detail")
    public ResponseEntity<BoardDetailVO> getBoard(@PathVariable Long no) {
        return new ResponseEntity<>(boardService.getDetail(no), HttpStatus.OK);
    }

    @DeleteMapping("/file/{file}")
    public ResponseEntity<MessageVO> deleteFile(@PathVariable String file) throws IOException {
        MessageVO response = boardService.deleteImage(file);
        return new ResponseEntity<>(response, response.getCode());
    }
}
