package com.lth.community.service;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.BoardInfoRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.WritingMemberVO;
import com.lth.community.vo.board.WritingNonMemberVO;
import com.lth.community.vo.member.MemberInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardInfoRepository boardInfoRepository;
    private final MemberInfoRepository memberInfoRepository;

    public MessageVO memberWriting(String memberId, WritingMemberVO member) {
        MemberInfoEntity memberCheck = memberInfoRepository.findByMemberId(memberId);
        if(member.getTitle() == null || member.getTitle().equals("") || member.getContent() == null || member.getContent().equals("")) {
            return MessageVO.builder()
                    .key("error")
                    .message("글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BoardInfoEntity nonMemberPost = BoardInfoEntity.builder()
                .title(member.getTitle())
                .content(member.getContent())
                .member(memberCheck)
                .creatDt(LocalDateTime.now())
                .build();
        boardInfoRepository.save(nonMemberPost);
        return MessageVO.builder()
                .key(memberCheck.getMemberId())
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();

    }

    public MessageVO nonMemberWriting(WritingNonMemberVO nonMember) {
        if(nonMember.getId() == null || nonMember.getId().equals("") || nonMember.getPw() == null || nonMember.getPw().equals("") || nonMember.getTitle() == null || nonMember.getTitle().equals("") || nonMember.getContent() == null || nonMember.getContent().equals("")) {
            return MessageVO.builder()
                    .key("error")
                    .message("게시글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BoardInfoEntity nonMemberPost = BoardInfoEntity.builder()
                .boardId(nonMember.getId())
                .pw(nonMember.getPw())
                .title(nonMember.getTitle())
                .content(nonMember.getContent())
                .creatDt(LocalDateTime.now())
                .build();
        boardInfoRepository.save(nonMemberPost);
        return MessageVO.builder()
                .key("NonMember")
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }
}
