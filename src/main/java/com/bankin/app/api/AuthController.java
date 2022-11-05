package com.bankin.app.api;

import com.bankin.app.dto.auth.LoginReq;
import com.bankin.app.dto.auth.InitUserReq;
import com.bankin.app.dto.auth.UserInfoReq;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.service.AccountService;
import com.bankin.app.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginReq loginReq){
        try {
            log.debug("Getting login request with body: {}", loginReq);
            return ResponseEntity.ok(authService.userLogin(loginReq));
        } catch (ServiceException e) {
            log.error("Login fail with error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/init")
    public ResponseEntity<?> initUser(@RequestBody InitUserReq registerReq){
        try{
            log.debug("Init user request with body: {}", registerReq);
            return ResponseEntity.ok(authService.initUser(registerReq));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/info")
    public ResponseEntity<?> createUserInfo(@RequestBody UserInfoReq userInfoReq){
        try{
            log.debug("Create user info with body: {}", userInfoReq);
            return ResponseEntity.ok(authService.createUserInfo(userInfoReq));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/face")
    public ResponseEntity<?> createFaceInfo(@RequestHeader("id") long userId){
        try{
            log.debug("Update face checking stage for user: {}", userId);
            authService.updateFaceInfo(userId);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
