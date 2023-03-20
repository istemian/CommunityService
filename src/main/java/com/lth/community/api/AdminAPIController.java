package com.lth.community.api;

import com.lth.community.service.AdminService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.admin.AllMemberInfoVO;
import com.lth.community.vo.admin.MemberBanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminAPIController {
    private final AdminService adminService;
    @GetMapping("/memberList")
    public ResponseEntity<List<AllMemberInfoVO>> getMemberList() {
        return new ResponseEntity<>(adminService.allMemberList(), HttpStatus.OK);
    }

    @PostMapping("/memberBan")
    public ResponseEntity<MessageVO> banMember(@RequestBody MemberBanVO data) {
        MessageVO response = adminService.banMember(data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/board/{postNo}")
    public ResponseEntity<MessageVO> deletePost(@PathVariable Long postNo) {
        MessageVO response = adminService.deletePost(postNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/comment/{commentNo}")
    public ResponseEntity<MessageVO> deleteComment(@PathVariable Long commentNo) {
        MessageVO response = adminService.deleteComment(commentNo);
        return new ResponseEntity<>(response, response.getCode());
    }

    @PostMapping("/dummyMember")
    public ResponseEntity<MessageVO> dummyMember() {
        MessageVO response = adminService.dummyMember();
        return new ResponseEntity<>(response, response.getCode());
    }

    @PostMapping("/dummyPost")
    public ResponseEntity<MessageVO> dummyPost() {
        MessageVO response = adminService.dummyPost();
        return new ResponseEntity<>(response, response.getCode());
    }

    @PostMapping("/dummyComment")
    public ResponseEntity<MessageVO> dummyComment() {
        MessageVO response = adminService.dummyComment();
        return new ResponseEntity<>(response, response.getCode());
    }
}
