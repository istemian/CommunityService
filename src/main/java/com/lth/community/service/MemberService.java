package com.lth.community.service;

import com.lth.community.entity.BoardInfoEntity;
import com.lth.community.entity.CommentInfoEntity;
import com.lth.community.entity.DeleteMemberInfoEntity;
import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.BoardInfoRepository;
import com.lth.community.repository.CommentInfoRepository;
import com.lth.community.repository.DeleteMemberInfoRepository;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.security.filter.JwtAuthenticationFilter;
import com.lth.community.security.provider.JwtTokenProvider;
import com.lth.community.security.service.CustomUserDetailService;
import com.lth.community.security.vo.TokenVO;
import com.lth.community.vo.*;
import com.lth.community.vo.board.BoardInfoVO;
import com.lth.community.vo.board.GetBoardVO;
import com.lth.community.vo.comment.GetMyCommentVO;
import com.lth.community.vo.member.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberInfoRepository memberInfoRepository;
    private final AuthenticationManagerBuilder authBuilder;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final DeleteMemberInfoRepository deleteMemberInfoRepository;
    private final BoardInfoRepository boardInfoRepository;
    private final CommentInfoRepository commentInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberLoginResponseVO login(LoginVO login) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(login.getId());
        if(member == null) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("존재하지 않는 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if (!encoder.matches(login.getPw(), member.getPassword())) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("비밀번호가 일치하지 않습니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if (member.getStatus() == 2) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("이용 정지된 회원입니다.")
                    .code(HttpStatus.FORBIDDEN)
                    .build();
        }
        else if (member.getStatus() == 3) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("탈퇴한 회원입니다.")
                    .code(HttpStatus.FORBIDDEN)
                    .build();
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPw());
        Authentication authentication = authBuilder.getObject().authenticate(authenticationToken);
        MemberLoginResponseVO response = MemberLoginResponseVO.builder()
                .status(true)
                .message("정상 로그인")
                .token(tokenProvider.generateToken(authentication))
                .code(HttpStatus.OK)
                .build();

        if(member.getRefreshToken() != null) {
            member.setRefreshToken(null);
        }
        member.setRefreshToken(tokenProvider.generateToken(authentication).getRefreshToken());
        memberInfoRepository.save(member);

        return response;
    }

    public MemberInfoVO getMemberDetailInfo(String id) {
        try {
            userDetailService.loadUserByUsername(id);
            MemberInfoEntity entity = memberInfoRepository.findByMemberId(id);
            MemberInfoVO vo = new MemberInfoVO(entity);
            return vo;
        }
        catch (UsernameNotFoundException e) {
            return null;
        }
    }

    public MessageVO memberJoin(MemberJoinVO data) {
        String idPattern ="^[0-9|a-z|A-Z]*$";
        String pwPattern ="^[0-9|a-z|A-Z|!@#$%^&*()]*$";
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String emailPattern = "^[0-9|a-z|A-Z]+(.[_a-z|0-9-]+)*@(?:\\w+\\.)+\\w+$";

        if(memberInfoRepository.countByMemberId(data.getId()) == 1) {
            return MessageVO.builder()
                    .status(false)
                    .message("중복 아이디입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getId() == null || data.getId().equals("") || !Pattern.matches(idPattern, data.getId())) {
            return MessageVO.builder()
                    .status(false)
                    .message("아이디를 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getPw() == null || data.getPw().equals("") || !Pattern.matches(pwPattern, data.getPw()) || data.getPw().length() < 8) {
            return MessageVO.builder()
                    .status(false)
                    .message("비밀번호를 다시 확인해주세요. (8자리 이상, 공백 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getName() == null || data.getName().equals("") || !Pattern.matches(namePattern, data.getName())) {
            return MessageVO.builder()
                    .status(false)
                    .message("이름을 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(memberInfoRepository.countByNickname(data.getNickname()) == 1) {
            return MessageVO.builder()
                    .status(false)
                    .message("중복 닉네임입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getNickname() == null || data.getNickname().equals("") || !Pattern.matches(namePattern, data.getNickname())) {
            return MessageVO.builder()
                    .status(false)
                    .message("닉네임을 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(memberInfoRepository.countByEmail(data.getEmail()) == 1) {
            return MessageVO.builder()
                    .status(false)
                    .message("중복 이메일입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getEmail() == null || data.getEmail().equals("") || !Pattern.matches(emailPattern, data.getEmail())) {
            return MessageVO.builder()
                    .status(false)
                    .message("이메일을 다시 확인해주세요.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }

        MemberInfoEntity entity = MemberInfoEntity.builder()
                .memberId(data.getId())
                .pw(encoder.encode(data.getPw()))
                .name(data.getName())
                .nickname(data.getNickname())
                .email(data.getEmail())
                .createDt(LocalDateTime.now())
                .build();
        memberInfoRepository.save(entity);
        return MessageVO.builder()
                .status(true)
                .message("가입이 완료되었습니다.")
                .code(HttpStatus.CREATED)
                .build();
    }

    public MessageVO deleteMember(String id) {
        MemberInfoEntity entity = memberInfoRepository.findByMemberId(id);
        entity.setStatus(3);
        entity.setRefreshToken(null);
        DeleteMemberInfoEntity delete = new DeleteMemberInfoEntity(null, LocalDateTime.now().plusDays(7), entity);
        memberInfoRepository.save(entity);
        deleteMemberInfoRepository.save(delete);
        return MessageVO.builder()
                .status(true)
                .message("회원 탈퇴가 완료되었습니다. (작성한 게시글 및 댓글은 일주일 후 사라집니다.)")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO updateMember(String id, MemberUpdateVO data) {
        String pwPattern ="^[0-9|a-z|A-Z|!@#$%^&*()]*$";
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String emailPattern = "^[0-9|a-z|A-Z]+(.[_a-z|0-9-]+)*@(?:\\w+\\.)+\\w+$";
        MemberInfoEntity entity = memberInfoRepository.findByMemberId(id);
        if(data.getPw() != null) {
            if(data.getPw().equals("") || !Pattern.matches(pwPattern, data.getPw()) || data.getPw().length() < 8) {
                return MessageVO.builder()
                        .status(false)
                        .message("비밀번호를 다시 확인해주세요. (8자리 이상, 공백 불가)")
                        .code(HttpStatus.BAD_REQUEST)
                        .build();
            }
            else {
                entity.setPw(encoder.encode(data.getPw()));
            }
        }
        if(data.getNickname() != null) {
            if(memberInfoRepository.countByNickname(data.getNickname()) == 1) {
                return MessageVO.builder()
                        .status(false)
                        .message("중복 닉네임입니다.")
                        .code(HttpStatus.BAD_REQUEST)
                        .build();
            }
            else if(data.getNickname().equals("") || !Pattern.matches(namePattern, data.getNickname())) {
                return MessageVO.builder()
                        .status(false)
                        .message("닉네임을 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                        .code(HttpStatus.BAD_REQUEST)
                        .build();
            }
            else {
                entity.setNickname(data.getNickname());
            }
        }
        if(data.getEmail() != null) {
            if(memberInfoRepository.countByEmail(data.getEmail()) == 1) {
                return MessageVO.builder()
                        .status(false)
                        .message("중복 이메일입니다.")
                        .code(HttpStatus.BAD_REQUEST)
                        .build();
            }
            else if(data.getEmail().equals("") || !Pattern.matches(emailPattern, data.getEmail())) {
                return MessageVO.builder()
                        .status(false)
                        .message("이메일을 다시 확인해주세요.")
                        .code(HttpStatus.BAD_REQUEST)
                        .build();
            }
            else {
                entity.setEmail(data.getEmail());
            }
        }
        memberInfoRepository.save(entity);
        return MessageVO.builder()
                .status(true)
                .message("수정되었습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO findMemberId(MemberFindIdVO email) {
        MemberInfoEntity entity = memberInfoRepository.findByEmail(email.getEmail());
        if(entity == null) {
            return  MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return MessageVO.builder()
                .status(true)
                .message("회원님의 아이디는 "+entity.getMemberId()+" 입니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public MessageVO findMemberPw(MemberFindPwVO data) {
        Long entity = memberInfoRepository.countByMemberIdAndNameAndEmail(data.getId(), data.getName(), data.getEmail());
        if(entity == 0) {
            return  MessageVO.builder()
                    .status(false)
                    .message("존재하지 않는 회원입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        String password = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        SimpleMailMessage message = new SimpleMailMessage();
        MemberInfoEntity member = memberInfoRepository.findByMemberId(data.getId());
        message.setTo(data.getEmail());
        message.setSubject("임시 비밀번호 입니다.");
        message.setText("회원님의 임시 비밀번호는 "+password+" 입니다.");
        javaMailSender.send(message);
        member.setPw(encoder.encode(password));
        memberInfoRepository.save(member);
        return MessageVO.builder()
                .status(true)
                .message("임시 비밀번호를 메일로 보냈습니다.")
                .code(HttpStatus.OK)
                .build();
    }

    public List<BoardInfoVO> getMyPost(String memberId) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        List<BoardInfoEntity> board = boardInfoRepository.findByMember(member);
        if(board == null) {
            return null;
        }
        List<BoardInfoVO> boardList = new ArrayList<>();
        for(int i=0; i<board.size(); i++) {
            BoardInfoVO myPost = BoardInfoVO.builder()
                    .no(board.get(i).getSeq())
                    .nickname(board.get(i).getMember().getNickname())
                    .title(board.get(i).getTitle())
                    .creatDt(board.get(i).getCreatDt())
                    .modifiedDt(board.get(i).getModifiedDt())
                    .build();
            boardList.add(myPost);
        }
        return boardList;
    }

    public List<GetMyCommentVO> getMyComment(String memberId) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        List<CommentInfoEntity> comment = commentInfoRepository.findByMember(member);
        if(comment == null) {
            return null;
        }
        List<GetMyCommentVO> commentList = new ArrayList<>();
        for(int i=0; i<comment.size(); i++) {
            GetMyCommentVO myComment = GetMyCommentVO.builder()
                    .postNo(comment.get(i).getBoard().getSeq())
                    .postTitle(comment.get(i).getBoard().getTitle())
                    .no(comment.get(i).getSeq())
                    .nickname(comment.get(i).getMember().getNickname())
                    .comment(comment.get(i).getContent())
                    .createDt(comment.get(i).getCreatDt())
                    .build();
            commentList.add(myComment);
        }
        return commentList;
    }

    public MemberReissueAccessTokenVO reissueToken(String memberId) {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(memberId);
        if(member.getRefreshToken() != null) {
            if(jwtTokenProvider.validateToken(member.getRefreshToken())) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPw());
                Authentication authentication = authBuilder.getObject().authenticate(authenticationToken);
                return MemberReissueAccessTokenVO.builder()
                        .status(true)
                        .message("Access Token 재발급 완료")
                        .grantType("Bearer")
                        .accessToken(tokenProvider.generateToken(authentication).getAccessToken())
                        .code(HttpStatus.OK)
                        .build();
            }
            else {
                return MemberReissueAccessTokenVO.builder()
                        .status(false)
                        .message("Refresh Token 오류")
                        .grantType(null)
                        .accessToken(null)
                        .code(HttpStatus.FORBIDDEN)
                        .build();
            }
        }
        return MemberReissueAccessTokenVO.builder()
                .status(false)
                .message("Refresh Token이 존재하지 않습니다.")
                .grantType(null)
                .accessToken(null)
                .code(HttpStatus.BAD_REQUEST)
                .build();
    }
}