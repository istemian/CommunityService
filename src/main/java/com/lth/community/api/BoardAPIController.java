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
import org.springframework.http.MediaType;
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

    @PostMapping(value ="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestPart(required = false) MultipartFile[] files, WritingMemberVO member) {
        MessageVO response = boardService.writing(authentication.getName(), member, files);
        return new ResponseEntity<>(response, response.getCode());
    }
    @PostMapping(value = "/non", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestPart(required = false) MultipartFile[] files, WritingNonMemberVO nonMemberVO) {
        MessageVO response = boardService.nonWriting(nonMemberVO, files);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/{postNo}")
    public ResponseEntity<MessageVO> deletePost(Authentication authentication, @PathVariable Long postNo) {
        MessageVO response = boardService.delete(authentication.getName(), postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/non/{postNo}")
    public ResponseEntity<MessageVO> nonDeletePost(@PathVariable Long postNo, @RequestBody DeletePostNonMemberVO data) {
        MessageVO response = boardService.nonDelete(postNo, data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("")
    public ResponseEntity<GetBoardVO> getBoard(@RequestParam @Nullable String keyword, @PageableDefault(size = 10, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoard(keyword, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{postNo}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody WritingMemberVO data, Authentication authentication, @PathVariable Long postNo) {
        MessageVO response = boardService.update(data, authentication.getName(), postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @PatchMapping("/non/{postNo}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody UpdatePostNonMember data, @PathVariable Long postNo) {
        MessageVO response = boardService.nonUpdate(data, postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("/file/{file}")
    public ResponseEntity<Resource> getFile(@PathVariable String file ,HttpServletRequest request) throws Exception {
        return boardService.getFile(file, request);
    }

    @GetMapping("/{postNo}/detail")
    public ResponseEntity<BoardDetailVO> getBoard(@PathVariable Long postNo) {
        return new ResponseEntity<>(boardService.getDetail(postNo), HttpStatus.OK);
    }

}
