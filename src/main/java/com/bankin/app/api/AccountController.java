package com.bankin.app.api;

import com.bankin.app.exception.ServiceException;
import com.bankin.app.service.AccountService;
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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/create")
    public ResponseEntity<?> createAccount(@RequestHeader("id") long userId){
        try{
            accountService.createAccount(userId);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/general")
    public ResponseEntity<?> getGeneralInfo(@RequestHeader("id") long userId){
        try{
            return ResponseEntity.ok(accountService.getGeneralAccountInfo(userId));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
