package com.lth.community.service;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.BoardInfoRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.board.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardInfoRepository boardInfoRepository;
    private final MemberInfoRepository memberInfoRepository;

    public MessageVO writing(String memberId, WritingMemberVO member) {
        MemberInfoEntity memberCheck = memberInfoRepository.findByMemberId(memberId);
        if(member.getTitle() == null || member.getTitle().equals("") || member.getContent() == null || member.getContent().equals("")) {
            return MessageVO.builder()
                    .status(false)
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
                .status(true)
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();

    }

    public MessageVO nonWriting(WritingNonMemberVO nonMember) {
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String pwPattern = "^[0-9|a-z|A-Z]*$";

        if(nonMember.getId() == null || nonMember.getId().equals("") || nonMember.getPw() == null || nonMember.getPw().equals("") || nonMember.getTitle() == null || nonMember.getTitle().equals("") || nonMember.getContent() == null || nonMember.getContent().equals("")) {
            return MessageVO.builder()
                    .status(false)
                    .message("게시글 등록에 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(namePattern, nonMember.getId())) {
            return MessageVO.builder()
                    .status(false)
                    .message("아이디를 다시 확인해주세요. (특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(!Pattern.matches(pwPattern, nonMember.getPw()) || nonMember.getPw().length() < 4) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 다시 확인해주세요. (영문과 숫자만 가능, 4자리 이상)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BoardInfoEntity nonMemberPost = BoardInfoEntity.builder()
                .boardId(nonMember.getId()+"(비회원)")
                .pw(nonMember.getPw())
                .title(nonMember.getTitle())
                .content(nonMember.getContent())
                .creatDt(LocalDateTime.now())
                .build();
        boardInfoRepository.save(nonMemberPost);
        return MessageVO.builder()
                .status(true)
                .message("글이 등록되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO delete(String memberId, Long no) {
        BoardInfoEntity board = boardInfoRepository.findBySeq(no);
        Boolean check = checkId(memberId, board);
        MessageVO response = null;
        if(check) {
            boardInfoRepository.delete(board);
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

    public MessageVO nonDelete(Long no, String pw) {
        BoardInfoEntity board = boardInfoRepository.findBySeq(no);
        MessageVO response = null;
        if(board.getPw().equals(pw)) {
            boardInfoRepository.delete(board);
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

    public GetBoardVO getBoard(String keyword, Pageable pageable) {
        if(keyword == null) { keyword = ""; }
        Page<BoardInfoEntity> board = boardInfoRepository.findByTitleContains(keyword, pageable);
        List<BoardInfoVO> info = new ArrayList<>();
        String nickname = null;
        for(int i=0; i<board.getTotalElements(); i++) {
            if (board.getContent().get(i).getBoardId() == null) {
                BoardInfoVO infoMake = BoardInfoVO.builder()
                        .no(board.getContent().get(i).getSeq())
                        .nickname(board.getContent().get(i).getMember().getMemberId())
                        .title(board.getContent().get(i).getTitle())
                        .creatDt(board.getContent().get(i).getCreatDt())
                        .modifiedDt(board.getContent().get(i).getModifiedDt())
                        .build();
                info.add(infoMake);
            }
            else if(board.getContent().get(i).getMember() == null) {
                BoardInfoVO infoMake = BoardInfoVO.builder()
                        .no(board.getContent().get(i).getSeq())
                        .nickname(board.getContent().get(i).getBoardId())
                        .title(board.getContent().get(i).getTitle())
                        .creatDt(board.getContent().get(i).getCreatDt())
                        .modifiedDt(board.getContent().get(i).getModifiedDt())
                        .build();
                info.add(infoMake);
            }
        }
        return GetBoardVO.builder()
                .list(info)
                .total(board.getTotalElements())
                .totalPage(board.getTotalPages())
                .currentPage(board.getNumber())
                .build();

    }

    public MessageVO update(WritingMemberVO data, String memberId, Long no) {
        BoardInfoEntity post = boardInfoRepository.findBySeq(no);
        if(post == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Boolean check = checkId(memberId, post);
        if(check) {
            if(data.getTitle() == null || data.getTitle().equals("")) {
                post.setTitle(post.getTitle());
                post.setContent(data.getContent());
            }
            if(data.getContent() == null || data.getContent().equals("")) {
                post.setTitle(data.getTitle());
                post.setContent(post.getContent());
            }
            post.setModifiedDt(LocalDateTime.now());
            boardInfoRepository.save(post);
            return MessageVO.builder()
                    .status(true)
                    .message("수정되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            String message = null;
            if(!check) {
                message = "본인의 글이 아닙니다.";
            }
            else {
                message = "수정에 실패했습니다.";
            }
            return MessageVO.builder()
                    .status(false)
                    .message(message)
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public MessageVO nonUpdate(UpdatePostNonMember data, Long no) {
        BoardInfoEntity post = boardInfoRepository.findBySeq(no);
        String message = null;
        if(post == null) {
            return MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 글입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getPw() == null || !data.getPw().equals(post.getPw())) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 확인해주세요.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if(data.getPw().equals(post.getPw())) {
            if(data.getTitle() == null || data.getTitle().equals("")) {
                post.setTitle(post.getTitle());
                post.setContent(data.getContent());
            }
            if(data.getContent() == null || data.getContent().equals("")) {
                post.setTitle(data.getTitle());
                post.setContent(post.getContent());
            }
            post.setModifiedDt(LocalDateTime.now());
            boardInfoRepository.save(post);
            return MessageVO.builder()
                    .status(true)
                    .message("수정되었습니다.")
                    .code(HttpStatus.OK)
                    .build();
        }
        else {
            return MessageVO.builder()
                    .status(false)
                    .message("수정 실패했습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public Boolean checkId(String memberId, BoardInfoEntity board) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        if(member == board.getMember()) {
            return true;
        }
        else {
            return false;
        }
    }

}
