package com.bankin.app.api;

import com.bankin.app.config.JwtTokenUtil;
import com.bankin.app.dto.auth.JwtResp;
import com.bankin.app.dto.auth.LoginReq;
import com.bankin.app.dto.auth.InitUserReq;
import com.bankin.app.dto.auth.UserInfoReq;
import com.bankin.app.entity.AppUser;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.UserRepository;
import com.bankin.app.service.AccountService;
import com.bankin.app.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> jwtLogin(@RequestBody LoginReq loginReq) throws Exception{
        authenticate(loginReq.getPhone(), loginReq.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginReq.getPhone());

        final String token = jwtTokenUtil.generateToken(userDetails);
        AppUser user = userRepository.findByPhoneNumber(loginReq.getPhone())
                .orElseThrow(() ->new ServiceException("User does not exist ---phone number: "+ loginReq.getPhone()));
        return ResponseEntity.ok(new JwtResp(user.getId(), token));
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

    @GetMapping("/create")
    public ResponseEntity<?> createAccount(@RequestHeader("id") long userId){
        try{
            String phone = accountService.createAccount(userId);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResp(token));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/face_login")
    public ResponseEntity<?> faceLogin(@RequestHeader("id") long userId){
        try{
            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhoneNumber());

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResp(token));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
