package com.lth.community.security.service;

import com.lth.community.entity.MemberInfoEntity;
import com.lth.community.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberInfoRepository memberInfoRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return createUserDetails(memberInfoRepository.findByMemberId(username));
    }
    public UserDetails createUserDetails(MemberInfoEntity member) {
        return User.builder().username(member.getMemberId())
                .password(passwordEncoder.encode(member.getPw()))
                .roles(member.getRole())
                .build();
    }
}

