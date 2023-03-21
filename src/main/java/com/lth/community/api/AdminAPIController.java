package com.lth.community.api;

import com.lth.community.service.AdminService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.admin.AllMemberInfoVO;
import com.lth.community.vo.admin.MemberBanVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "관리자 API", description = "관리자용 API 입니다.")
public class AdminAPIController {
    private final AdminService adminService;
    @Operation(summary = "모든 회원 조회")
    @GetMapping("/memberList")
    public ResponseEntity<List<AllMemberInfoVO>> getMemberList() {
        return new ResponseEntity<>(adminService.allMemberList(), HttpStatus.OK);
    }

    @Operation(summary = "회원 정지", description = "원하는 회원을 정지합니다.")
    @PostMapping("/memberBan")
    public ResponseEntity<MessageVO> banMember(@RequestBody MemberBanVO data) {
        MessageVO response = adminService.banMember(data);
        return new ResponseEntity<>(response, response.getCode());
    }
    @Operation(summary = "게시글 삭제", description = "원하는 게시글을 삭제합니다.")
    @DeleteMapping("/board/{postNo}")
    public ResponseEntity<MessageVO> deletePost(@PathVariable Long postNo) {
        MessageVO response = adminService.deletePost(postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @Operation(summary = "댓글 삭제", description = "원하는 댓글을 삭제합니다.")
    @DeleteMapping("/comment/{commentNo}")
    public ResponseEntity<MessageVO> deleteComment(@PathVariable Long commentNo) {
        MessageVO response = adminService.deleteComment(commentNo);
        return new ResponseEntity<>(response, response.getCode());
    }

//    @PostMapping("/dummyMember")
//    public ResponseEntity<MessageVO> dummyMember() {
//        MessageVO response = adminService.dummyMember();
//        return new ResponseEntity<>(response, response.getCode());
//    }
//
//    @PostMapping("/dummyPost")
//    public ResponseEntity<MessageVO> dummyPost() {
//        MessageVO response = adminService.dummyPost();
//        return new ResponseEntity<>(response, response.getCode());
//    }
//
//    @PostMapping("/dummyComment")
//    public ResponseEntity<MessageVO> dummyComment() {
//        MessageVO response = adminService.dummyComment();
//        return new ResponseEntity<>(response, response.getCode());
//    }

    @Operation(summary = "권한 변경", description = "원하는 회원의 권한을 변경합니다. 권한이 ADMIN일 시 USER로 USER일 시 ADMIN으로 변경됩니다.")
    @PostMapping("/authorize/{memberId}")
    public ResponseEntity<MessageVO> authorize(@PathVariable String memberId) {
        MessageVO response = adminService.authorize(memberId);
        return new ResponseEntity<>(response, response.getCode());
    }
}
