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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 클래스 내부에 있는 private final 지정된 객체들의 의존성 주입 (Autowired)
public class MemberService {
    private final MemberInfoRepository memberInfoRepository;
    private final AuthenticationManagerBuilder authBuilder;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;

    public MemberLoginResponseVO login(LoginVO login) {
        MemberInfoEntity member = memberInfoRepository.findByMemberIdAndPw(login.getId(), login.getPw());
        if (member == null) {
            return  MemberLoginResponseVO.builder()
                    .status(false)
                    .message("아이디 또는 비밀번호 오류입니다.")
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
        MemberInfoEntity entity = memberInfoRepository.findByMemberId(login.getId());
        if(!entity.getRefreshToken().isEmpty()) {
            entity.setRefreshToken(null);
            entity.setRefreshToken(tokenProvider.generateToken(authentication).getRefreshToken());
            memberInfoRepository.save(entity);
        }
        else {
            entity.setRefreshToken(tokenProvider.generateToken(authentication).getRefreshToken());
            memberInfoRepository.save(entity);
        }
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
        MemberInfoEntity entity = MemberInfoEntity.builder()
                .memberId(data.getId())
                .pw(data.getPw())
                .name(data.getName())
                .nickname(data.getNickname())
                .email(data.getEmail())
                .role("USER")
                .build();
        memberInfoRepository.save(entity);
        return new MessageVO(data.getId(), "가입되었습니다.");
    }
}