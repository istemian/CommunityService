package com.lth.community.security.provider;

import com.lth.community.security.vo.TokenVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final Integer tokenExpireMinutes = 1;
    private final Integer refreshExpireMinutes = 10;
    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public TokenVO generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Date expires = new Date((new Date()).getTime()+tokenExpireMinutes*60*1000);
        Date refreshExpires = new Date((new Date()).getTime()+refreshExpireMinutes*60*1000);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).claim("auth", authorities).setExpiration(expires)
                .signWith(key, SignatureAlgorithm.HS256).compact();
        String refreshToken = Jwts.builder().setExpiration(refreshExpires)
                .signWith(key, SignatureAlgorithm.HS256).compact();
        return TokenVO.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT Token"+e);
        } catch(ExpiredJwtException e) {
            System.out.println("Expired JWT Token"+e);
        } catch(UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token"+e);
        } catch(IllegalArgumentException e) {
            System.out.println("JWT claims string is empty."+e);
        }
        return false;
    }
}






























