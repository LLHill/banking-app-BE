package com.bankin.app.api;

import com.bankin.app.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private AuthService userService;

    @PostMapping()
    public ResponseEntity<?> getUser(long userId){
        try {
            log.debug("Getting user request with id: {}", userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.debug("Login fail");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
