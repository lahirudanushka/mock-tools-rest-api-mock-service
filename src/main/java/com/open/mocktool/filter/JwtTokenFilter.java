package com.open.mocktool.filter;


import com.open.mocktool.service.JwtTokenProvider;
import com.open.mocktool.util.CommonException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = null;

            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                jwtToken = authorizationHeader.substring(7);
            }
            if (jwtToken != null) {
                if (!jwtTokenProvider.validateToken(jwtToken)) {
                    throw new CommonException("403", "Invalid JWT Token", HttpStatus.FORBIDDEN, null);
                }

            }
            if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String userName = jwtTokenProvider.extractUsername(jwtToken);
                UserDetails userDetails = new User(userName, "", Collections.singleton(new SimpleGrantedAuthority(jwtTokenProvider.extractClaimWithType(jwtToken, "role", String.class))));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CommonException("401", "Unauthorized", HttpStatus.FORBIDDEN, ex.getStackTrace());
        }
    }
}
