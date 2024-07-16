package com.open.mocktool.service;

import com.open.mocktool.dto.UserCreateRequest;
import com.open.mocktool.dto.UserUpdateRequest;
import com.open.mocktool.repository.User;
import com.open.mocktool.repository.UserRepository;
import com.open.mocktool.util.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> createUser(UserCreateRequest body) {
        try {

            Optional<User> existingUser = userRepository.findByEmail(body.getEmail().toLowerCase());
            if (!existingUser.isPresent()) {
                User user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setName(body.getName());
                user.setEmail(body.getEmail().toLowerCase());
                user.setPassword(BCrypt.hashpw(body.getPassword(), BCrypt.gensalt()));
                user.setTeam(body.getTeam());
                user.setCreatedBy(body.getEmail());
                user.setCreatedDateTime(LocalDateTime.now());
                user.setLastLogin(LocalDateTime.now());
                user.setRole(body.getRole());
                user = userRepository.save(user);
                user.setPassword("*****************");
                return ResponseEntity.ok(user);
            } else
                throw new CommonException("400", "User already exist", HttpStatus.BAD_REQUEST, null);

        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }


    public ResponseEntity<Object> getAllUsers(Integer pageNumber, Integer pageSize) {
        try {
            Page<User> allMocks = userRepository.findAll(PageRequest.of(pageNumber, pageSize == 0 ? 0 : pageSize + 1));
            return ResponseEntity.ok(allMocks.get().collect(Collectors.toList()));
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> getUserById(String id) {
        try {
            User u = userRepository.findById(id).get();
            u.setPassword("*****************");
            return ResponseEntity.ok(u);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> updateUser(UserUpdateRequest body, String id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                User updated = user.get();
                updated.setName(body.getName());
                updated.setTeam(body.getTeam());
                updated.setRole(body.getRole());
                updated = userRepository.save(updated);
                updated.setPassword("*****************");
                return ResponseEntity.accepted().body(updated);
            } else
                throw new CommonException("404", "User Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }

    public ResponseEntity<Object> deleteUserById(String id) {
        try {
            Optional<User> mock = userRepository.findById(id);
            if (mock.isPresent()) {
                userRepository.delete(mock.get());
                return ResponseEntity.noContent().build();
            } else
                throw new CommonException("404", "User Not Found", HttpStatus.NOT_FOUND, null);
        } catch (CommonException e) {
            throw e;
        } catch (Exception ex) {
            throw new CommonException("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getStackTrace());
        }
    }
}
