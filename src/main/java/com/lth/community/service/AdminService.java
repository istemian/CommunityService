package com.lth.community.service;

import com.lth.community.entity.*;
import com.lth.community.repository.*;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.admin.AllMemberInfoVO;
import com.lth.community.vo.admin.MemberBanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberInfoRepository memberInfoRepository;
    private final BanMemberInfoRepository banMemberInfoRepository;
    private final DeleteMemberInfoRepository deleteMemberInfoRepository;
    private final BoardInfoRepository boardInfoRepository;
    private final CommentInfoRepository commentInfoRepository;
    private final PasswordEncoder encoder;

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
                    .status(false)
                    .message("아이디를 다시 확인해주세요.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(banMember.getStatus() == 2 || banMember.getStatus() == 3) {
            return MessageVO.builder()
                    .status(false)
                    .message("이미 정지되었거나 탈퇴한 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BanMemberInfoEntity banReason = BanMemberInfoEntity.builder()
                        .startDt(LocalDateTime.now())
                        .endDt(LocalDateTime.now().plusDays(data.getEndDt()))
                        .reason(data.getReason())
                        .member(banMember)
                        .build();
        banMember.setStatus(2);
        banMember.setRefreshToken(null);
        memberInfoRepository.save(banMember);
        banMemberInfoRepository.save(banReason);
        return MessageVO.builder()
                .status(true)
                .message(banMember.getMemberId()+"회원이 정지되었습니다."+"( 사유 : "+data.getReason()+" / "+LocalDate.now().plusDays(data.getEndDt())+"까지 )")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO deletePost(Long no) {
        BoardInfoEntity delete = boardInfoRepository.findBySeq(no);
        if(delete == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("글이 존재하지 않습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        boardInfoRepository.delete(delete);
        return MessageVO.builder()
                .status(true)
                .message(delete.getSeq()+"번 글이 삭제되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO deleteComment(Long no) {
        CommentInfoEntity delete = commentInfoRepository.findBySeq(no);
        if(delete == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("댓글이 존재하지 않습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        commentInfoRepository.delete(delete);
        return MessageVO.builder()
                .status(true)
                .message(delete.getSeq()+"번 댓글이 삭제되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO dummyMember() {
        for(int i=0; i<50; i++) {
            List<String> a = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                    "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                    "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                    "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                    "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
            List<String> b = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                    "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                    "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                    "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                    "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                    "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                    "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                    "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                    "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                    "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
            Collections.shuffle(a);
            Collections.shuffle(b);
            MemberInfoEntity entity = MemberInfoEntity.builder()
                    .memberId(String.valueOf(UUID.randomUUID()).substring(0, 8))
                    .pw(encoder.encode("1234"))
                    .name(a.get(0) + b.get(0) + b.get(1))
                    .nickname(a.get(0) + b.get(0) + b.get(1))
                    .email(String.valueOf(UUID.randomUUID()).substring(0, 8)+"@gmail.com")
                    .createDt(LocalDateTime.now())
                    .build();
            memberInfoRepository.save(entity);
        }
        return MessageVO.builder()
                .status(true)
                .message("Member Dummy")
                .code(HttpStatus.CREATED)
                .build();
    }

    public MessageVO dummyPost() {
        for(int i=0; i<100; i++) {
            int random = (int) ((Math.random())*10000%50)+1;
            BoardInfoEntity memberPost = BoardInfoEntity.builder()
                    .title(String.valueOf(UUID.randomUUID()))
                    .content(String.valueOf(UUID.randomUUID()))
                    .member(memberInfoRepository.findById((long) random).orElseThrow())
                    .creatDt(LocalDateTime.now())
                    .build();

            BoardInfoEntity nonMemberPost = BoardInfoEntity.builder()
                    .boardId(String.valueOf(UUID.randomUUID()).substring(0, 8)+"(비회원)")
                    .pw(encoder.encode("1234"))
                    .title(String.valueOf(UUID.randomUUID()))
                    .content(String.valueOf(UUID.randomUUID()))
                    .creatDt(LocalDateTime.now())
                    .build();

            boardInfoRepository.save(nonMemberPost);
            boardInfoRepository.save(memberPost);
        }
        return MessageVO.builder()
                .status(true)
                .message("Post Dummy")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO dummyComment() {
        for(int i=0; i<200; i++) {
            int random = (int) ((Math.random())*10000%50)+1;
            int randomPost = (int) ((Math.random())*10000%200)+1;
            CommentInfoEntity memberComment = CommentInfoEntity.builder()
                    .content(String.valueOf(UUID.randomUUID()))
                    .member(memberInfoRepository.findById((long) random).orElseThrow())
                    .board(boardInfoRepository.findBySeq((long) randomPost))
                    .creatDt(LocalDateTime.now())
                    .build();

            CommentInfoEntity nonMemberComment = CommentInfoEntity.builder()
                    .nickname(String.valueOf(UUID.randomUUID()).substring(0, 8)+"(비회원)")
                    .pw(encoder.encode("1234"))
                    .content(String.valueOf(UUID.randomUUID()))
                    .board(boardInfoRepository.findBySeq((long) randomPost))
                    .creatDt(LocalDateTime.now())
                    .build();

            commentInfoRepository.save(nonMemberComment);
            commentInfoRepository.save(memberComment);
        }
        return MessageVO.builder()
                .status(true)
                .message("Comment Dummy")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO authorize(String memberId) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        String beforeAuthorize = member.getRole();
        if(member == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if(member.getRole().equals("USER")) {
            member.setRole("ADMIN");
        }
        else if(member.getRole().equals("ADMIN")) {
            member.setRole("USER");
        }
        memberInfoRepository.save(member);
        return MessageVO.builder()
                .status(true)
                .message(member.getMemberId()+" 권한 설정 완료 ( "+beforeAuthorize+" -> "+ member.getRole()+" )")
                .code(HttpStatus.OK)
                .build();
    }
}
