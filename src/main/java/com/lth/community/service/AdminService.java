package com.lth.community.service;

import com.lth.community.entity.DeleteMemberInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.entity.BanMemberInfoEntity;
import com.lth.community.repository.DeleteMemberInfoRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.repository.BanMemberInfoRepository;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.admin.AllMemberInfoVO;
import com.lth.community.vo.admin.MemberBanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberInfoRepository memberInfoRepository;
    private final BanMemberInfoRepository banMemberInfoRepository;
    private final DeleteMemberInfoRepository deleteMemberInfoRepository;

    public List<AllMemberInfoVO> allMemberList() {
        List<MemberInfoEntity> create = memberInfoRepository.findAll();
        List<AllMemberInfoVO> allMember = new ArrayList<>();
        for(int i=0; i<create.size(); i++) {
            BanMemberInfoEntity ban = banMemberInfoRepository.findByMember(create.get(i));
            DeleteMemberInfoEntity deleteDt = deleteMemberInfoRepository.findByMember(create.get(i));
            if(ban == null && deleteDt == null) {
                AllMemberInfoVO allMemberCreate = AllMemberInfoVO.builder()
                        .seq(create.get(i).getSeq())
                        .memberId(create.get(i).getMemberId())
                        .name(create.get(i).getName())
                        .nickname(create.get(i).getNickname())
                        .email(create.get(i).getEmail())
                        .status(create.get(i).getStatus())
                        .role(create.get(i).getRole())
                        .createDt(create.get(i).getCreateDt())
                        .build();
                allMember.add(allMemberCreate);
            }
            else if(ban == null) {
                AllMemberInfoVO allMemberCreate = AllMemberInfoVO.builder()
                        .seq(create.get(i).getSeq())
                        .memberId(create.get(i).getMemberId())
                        .name(create.get(i).getName())
                        .nickname(create.get(i).getNickname())
                        .email(create.get(i).getEmail())
                        .status(create.get(i).getStatus())
                        .role(create.get(i).getRole())
                        .createDt(create.get(i).getCreateDt())
                        .deleteDt(deleteDt.getDeleteDt())
                        .build();
                allMember.add(allMemberCreate);
            }
            else if(deleteDt == null) {
                AllMemberInfoVO allMemberCreate = AllMemberInfoVO.builder()
                        .seq(create.get(i).getSeq())
                        .memberId(create.get(i).getMemberId())
                        .name(create.get(i).getName())
                        .nickname(create.get(i).getNickname())
                        .email(create.get(i).getEmail())
                        .status(create.get(i).getStatus())
                        .role(create.get(i).getRole())
                        .createDt(create.get(i).getCreateDt())
                        .banEndDt(ban.getEndDt())
                        .banReason(ban.getReason())
                        .build();
                allMember.add(allMemberCreate);
            }
            else {
                AllMemberInfoVO allMemberCreate = AllMemberInfoVO.builder()
                        .seq(create.get(i).getSeq())
                        .memberId(create.get(i).getMemberId())
                        .name(create.get(i).getName())
                        .nickname(create.get(i).getNickname())
                        .email(create.get(i).getEmail())
                        .status(create.get(i).getStatus())
                        .role(create.get(i).getRole())
                        .createDt(create.get(i).getCreateDt())
                        .banEndDt(ban.getEndDt())
                        .banReason(ban.getReason())
                        .deleteDt(deleteDt.getDeleteDt())
                        .build();
                allMember.add(allMemberCreate);
            }
        }
        return allMember;
    }

    public MessageVO banMember(MemberBanVO data) {
        MemberInfoEntity banMember = memberInfoRepository.findByMemberId(data.getId());
        if(banMember == null) {
            return MessageVO.builder()
                    .key("error")
                    .message("아이디를 다시 확인해주세요.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(banMember.getStatus() == 2 || banMember.getStatus() == 3) {
            return MessageVO.builder()
                    .key("error")
                    .message("이미 정지되었거나 탈퇴한 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BanMemberInfoEntity banReason = BanMemberInfoEntity.builder()
                        .startDt(LocalDate.now())
                        .endDt(LocalDate.now().plusDays(data.getEndDt()))
                        .reason(data.getReason())
                        .member(banMember)
                        .build();
        banMember.setStatus(2);
        banMember.setRefreshToken(null);
        memberInfoRepository.save(banMember);
        banMemberInfoRepository.save(banReason);
        return MessageVO.builder()
                .key(banMember.getMemberId())
                .message(banMember.getMemberId()+"회원이 정지되었습니다."+"( 사유 : "+data.getReason()+" / "+LocalDate.now().plusDays(data.getEndDt())+"까지 )")
                .code(HttpStatus.OK)
                .build();
    }
}
