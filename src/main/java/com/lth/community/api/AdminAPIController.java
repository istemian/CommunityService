package com.lth.community.api;

import com.lth.community.service.AdminService;
import com.lth.community.vo.admin.AllMemberInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminAPIController {
    private final AdminService adminService;
    @GetMapping("")
    public ResponseEntity<List<AllMemberInfoVO>> getMemberList() {
        return new ResponseEntity<>(adminService.allMemberList(), HttpStatus.OK);
    }
}
