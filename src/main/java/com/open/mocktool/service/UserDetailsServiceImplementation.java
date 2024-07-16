package com.open.mocktool.service;


import com.open.mocktool.repository.UserRepository;
import com.open.mocktool.util.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.debug("Loading user by username" + username);
            Optional<com.open.mocktool.repository.User> dbUser = userRepository.findByEmail(username.toLowerCase());
            if (dbUser.isPresent())
                return new User(username, dbUser.get().getPassword(), getAuthorities(Collections.singletonList(dbUser.get().getRole().toString())));
            else
                throw new UsernameNotFoundException("User Not Found : " + username);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }


    private List<GrantedAuthority> getAuthorities(List<String> roles) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {

            authorities.add(new SimpleGrantedAuthority(role));

        }

        return authorities;

    }

}
