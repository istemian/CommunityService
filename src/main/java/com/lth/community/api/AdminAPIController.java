package com.lth.community.api;

import com.lth.community.service.AdminService;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.admin.AllMemberInfoVO;
import com.lth.community.vo.admin.MemberBanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminAPIController {
    private final AdminService adminService;
    @GetMapping("/userList")
    public ResponseEntity<List<AllMemberInfoVO>> getMemberList() {
        return new ResponseEntity<>(adminService.allMemberList(), HttpStatus.OK);
    }

    @PostMapping("/userBan")
    public  ResponseEntity<MessageVO> banMember(@RequestBody MemberBanVO data) {
        MessageVO response = adminService.banMember(data);
        return new ResponseEntity<>(response, response.getCode());
    }

    @DeleteMapping("/post/{no}")
    public  ResponseEntity<MessageVO> banMember(@PathVariable Long no) throws Exception {
        MessageVO response = adminService.deletePost(no);
        return new ResponseEntity<>(response, response.getCode());
    }
}
