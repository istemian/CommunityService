package com.lth.community.service;

import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.MemberInfoRepository;
import com.lth.community.security.provider.JwtTokenProvider;
import com.lth.community.security.service.CustomUserDetailService;
import com.lth.community.vo.LoginVO;
import com.lth.community.vo.MemberInfoVO;
import com.lth.community.vo.MemberJoinVO;
import com.lth.community.vo.MessageVO;
import com.lth.community.vo.MemberLoginResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor // 클래스 내부에 있는 private final 지정된 객체들의 의존성 주입 (Autowired)
public class MemberService {
    private final MemberInfoRepository memberInfoRepository;
    private final AuthenticationManagerBuilder authBuilder;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder encoder;

    public MemberLoginResponseVO login(LoginVO login) throws Exception {
        MemberInfoEntity member = memberInfoRepository.findByMemberId(login.getId());
        if(member == null) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("존재하지 않는 회원입니다.")
                    .code(HttpStatus.FORBIDDEN)
                    .build();
        }
        else if (!encoder.matches(login.getPw(), member.getPassword())) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("비밀번호가 일치하지 않습니다.")
                    .code(HttpStatus.FORBIDDEN)
                    .build();
        }
        else if (member.getStatus() == 2) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("이용 정지된 사용자입니다.")
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

        if(!member.getRefreshToken().isEmpty()) {
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

    public MessageVO memberJoin(MemberJoinVO data) throws Exception {
        String idPattern ="^[0-9|a-z|A-Z]*$";
        String pwPattern ="^[0-9|a-z|A-Z|!@#$%^&*()]*$";
        String namePattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String emailPattern = "^[0-9|a-z|A-Z]+(.[_a-z|0-9-]+)*@(?:\\w+\\.)+\\w+$";

        if(memberInfoRepository.countByMemberId(data.getId()) == 1) {
            return MessageVO.builder()
                    .key("error_id")
                    .message("중복 아이디입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getId() == null || data.getId().equals("") || !Pattern.matches(idPattern, data.getId())) {
            return MessageVO.builder()
                    .key("error_id")
                    .message("아이디를 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getPw() == null || data.getPw().equals("") || !Pattern.matches(pwPattern, data.getPw()) || data.getPw().length() < 8) {
            return MessageVO.builder()
                    .key("error_password")
                    .message("비밀번호를 다시 확인해주세요. (8자리 이상, 공백 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getName() == null || data.getName().equals("") || !Pattern.matches(namePattern, data.getName())) {
            return MessageVO.builder()
                    .key("error_name")
                    .message("이름을 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(memberInfoRepository.countByNickname(data.getNickname()) == 1) {
            return MessageVO.builder()
                    .key("error_nickname")
                    .message("중복 닉네임입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getNickname() == null || data.getNickname().equals("") || !Pattern.matches(namePattern, data.getNickname())) {
            return MessageVO.builder()
                    .key("error_nickname")
                    .message("닉네임을 다시 확인해주세요. (공백 혹은 특수문자 사용 불가)")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(memberInfoRepository.countByEmail(data.getEmail()) == 1) {
            return MessageVO.builder()
                    .key("error_email")
                    .message("중복 이메일입니다.")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
        }
        else if(data.getEmail() == null || data.getEmail().equals("") || !Pattern.matches(emailPattern, data.getEmail())) {
            return MessageVO.builder()
                    .key("error_email")
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
                .role("USER")
                .build();
        memberInfoRepository.save(entity);
        return MessageVO.builder()
                .key(data.getId())
                .message("가입이 완료되었습니다.")
                .code(HttpStatus.CREATED)
                .build();
    }
}