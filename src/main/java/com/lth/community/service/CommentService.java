package com.lth.community.service;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.CommentInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.BoardInfoRepository;
import com.lth.community.repository.CommentInfoRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.DeletePostNonMemberVO;
import com.lth.community.vo.comment.CommentMemberVO;
import com.lth.community.vo.comment.CommentNonMemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentInfoRepository commentInfoRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final BoardInfoRepository boardInfoRepository;
    private final PasswordEncoder encoder;

    public MessageVO writing(String memberId, Long postNo, CommentMemberVO comment) {
        MemberInfoEntity memberCheck = memberInfoRepository.findByMemberId(memberId);
        BoardInfoEntity postCheck = boardInfoRepository.findBySeq(postNo);
        if(comment.getContent() == null || comment.getContent().equals("") || postCheck == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("댓글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        CommentInfoEntity memberComment = CommentInfoEntity.builder()
                .content(comment.getContent())
                .member(memberCheck)
                .board(postCheck)
                .creatDt(LocalDateTime.now())
                .build();
        commentInfoRepository.save(memberComment);
        return MessageVO.builder()
                .status(true)
                .message("댓글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();

    }

    public MessageVO nonWriting(CommentNonMemberVO comment, Long postNo) {
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String pwPattern = "^[0-9|a-z|A-Z]*$";
        BoardInfoEntity boardCheck = boardInfoRepository.findBySeq(postNo);
        if(comment.getId() == null || comment.getId().equals("") || comment.getPw() == null || comment.getPw().equals("") || comment.getContent() == null || comment.getContent().equals("") || boardCheck == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("댓글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(namePattern, comment.getId())) {
            return MessageVO.builder()
                    .status(false)
                    .message("아이디를 다시 확인해주세요. (특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(pwPattern, comment.getPw()) || comment.getPw().length() < 4) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 다시 확인해주세요. (영문과 숫자만 가능, 4자리 이상)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        CommentInfoEntity nonMemberComment = CommentInfoEntity.builder()
                .nickname(comment.getId()+"(비회원)")
                .pw(encoder.encode(comment.getPw()))
                .content(comment.getContent())
                .board(boardCheck)
                .creatDt(LocalDateTime.now())
                .build();
        commentInfoRepository.save(nonMemberComment);
        return MessageVO.builder()
                .status(true)
                .message("댓글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO delete(String memberId, Long no) {
        CommentInfoEntity comment = commentInfoRepository.findBySeq(no);
        MessageVO response = null;
        if(comment == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 댓글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Boolean check = checkId(memberId, comment);
        if(check) {
            commentInfoRepository.delete(comment);
            response = MessageVO.builder()
                    .status(true)
                    .message("삭제되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            response = MessageVO.builder()
                    .status(false)
                    .message("삭제 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return response;
    }

    public MessageVO nonDelete(Long no, DeletePostNonMemberVO data) {
        CommentInfoEntity comment = commentInfoRepository.findBySeq(no);;
        String message = null;
        if(comment == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(encoder.matches(data.getPw(), comment.getPw())) {
            commentInfoRepository.delete(comment);
            return MessageVO.builder()
                    .status(true)
                    .message("삭제되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            if(!encoder.matches(data.getPw(), comment.getPw())) {
                message = "비밀번호가 일치하지 않습니다.";
            }
            else {
                message = "삭제 실패했습니다.";
            }
            return MessageVO.builder()
                    .status(false)
                    .message(message)
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public Boolean checkId(String memberId, CommentInfoEntity comment) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        if(member == comment.getMember()) {
            return true;
        }
        else {
            return false;
        }
    }
}
