package com.lth.community.service;

import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.vo.admin.AllMemberInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberInfoRepository memberInfoRepository;

    public List<AllMemberInfoVO> allMemberList() {
        List<MemberInfoEntity> create = memberInfoRepository.findAll();
        List<AllMemberInfoVO> allMember = new ArrayList<>();
        for(int i=0; i<create.size(); i++) {
            AllMemberInfoVO allMemberCreate = AllMemberInfoVO.builder()
                    .seq(create.get(i).getSeq())
                    .memberId(create.get(i).getMemberId())
                    .name(create.get(i).getName())
                    .nickname(create.get(i).getNickname())
                    .email(create.get(i).getEmail())
                    .status(create.get(i).getStatus())
                    .role(create.get(i).getRole())
                    .createDt(create.get(i).getCreateDt())
                    .suspensionDt(create.get(i).getSuspension())
                    .deleteDt(create.get(i).getDeleteDt())
                    .build();
            allMember.add(allMemberCreate);
        }
        return allMember;
    }
}
