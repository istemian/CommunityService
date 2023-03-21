package com.lth.community.api;

import com.lth.community.service.BoardService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "게시판 API", description = "게시판 CRUD API 입니다.")
public class BoardAPIController {
    private final BoardService boardService;

    @Operation(summary = "회원 게시글 등록")
    @PostMapping(value ="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageVO> writingPost(Authentication authentication, @RequestPart(required = false) MultipartFile[] files, WritingMemberVO member) {
        MessageVO response = boardService.writing(authentication.getName(), member, files);
        return new ResponseEntity<>(response, response.getCode());
    }
    @Operation(summary = "비회원 게시글 등록", description = "아이디와 비밀번호를 입력하여 비회원이라도 게시글 등록이 가능합니다.")
    @PostMapping(value = "/non", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageVO> writingPostNonMember(@RequestPart(required = false) MultipartFile[] files, WritingNonMemberVO nonMemberVO) {
        MessageVO response = boardService.nonWriting(nonMemberVO, files);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "회원 게시글 삭제", description = "본인이 작성한 게시글 중 원하는 게시글을 삭제합니다.")
    @DeleteMapping("/{postNo}")
    public ResponseEntity<MessageVO> deletePost(Authentication authentication, @PathVariable Long postNo) {
        MessageVO response = boardService.delete(authentication.getName(), postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "비회원 게시글 삭제", description = "비밀번호가 일치하면 게시글을 삭제할 수 있습니다.")
    @DeleteMapping("/non/{postNo}")
    public ResponseEntity<MessageVO> nonDeletePost(@PathVariable Long postNo, @RequestBody DeletePostNonMemberVO data) {
        MessageVO response = boardService.nonDelete(postNo, data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "게시판 전체 조회", description = "게시판 전체 조회가 가능합니다. 기본값은 게시글 10개씩 1페이지부터 최신순으로 조회됩니다.")
    @GetMapping("")
    public ResponseEntity<GetBoardVO> getBoard(@Parameter(description = "검색어") @RequestParam @Nullable String keyword, @RequestParam @Nullable Integer page, @Parameter(hidden = true) @PageableDefault(size = 10, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoard(keyword, pageable), HttpStatus.OK);
    }

    @Operation(summary = "회원 게시글 수정")
    @PatchMapping("/{postNo}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody WritingMemberVO data, Authentication authentication, @PathVariable Long postNo) {
        MessageVO response = boardService.update(data, authentication.getName(), postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "비회원 게시글 수정", description = "비밀번호가 일치하면 게시글을 수정할 수 있습니다.")
    @PatchMapping("/non/{postNo}")
    public ResponseEntity<MessageVO> updatePost(@RequestBody UpdatePostNonMember data, @PathVariable Long postNo) {
        MessageVO response = boardService.nonUpdate(data, postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "파일 다운로드" , description = "파일을 다운로드 할 수 있습니다. 파일의 uuid를 입력하면 원본 파일명으로 다운로드됩니다.")
    @GetMapping("/file/{file}")
    public ResponseEntity<Resource> getFile(@Parameter(description = "파일의 uuid") @PathVariable String file) throws Exception {
        return boardService.getFile(file);
    }

    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다. 내용과 댓글, 첨부파일을 확인할 수 있습니다.")
    @GetMapping("/{postNo}/detail")
    public ResponseEntity<BoardDetailVO> getBoard(@PathVariable Long postNo) {
        return new ResponseEntity<>(boardService.getDetail(postNo), HttpStatus.OK);
    }

}
