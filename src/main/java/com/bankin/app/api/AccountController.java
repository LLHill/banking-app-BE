package com.bankin.app.api;

import com.bankin.app.dto.req.AccountReq;
import com.bankin.app.entity.Account;
import com.bankin.app.enums.UserRole;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.AccountRepository;
import com.bankin.app.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/general")
    public ResponseEntity<?> getGeneralInfo(@RequestHeader("id") long userId){
        try{
            return ResponseEntity.ok(accountService.getGeneralAccountInfo(userId));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getAccountInfo(@RequestHeader("id") long userId){
        try{
            Account account = accountService.getAccountByUserId(userId);
            return ResponseEntity.ok(accountService.getAccountInfo(account));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/activate")
    public ResponseEntity<?> activateAcc(@RequestBody AccountReq accountReq){
        try{
            accountService.activateAccount(accountReq.getAccountNumber());
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateAcc(@RequestBody AccountReq accountReq){
        try{
            accountService.deactivateAccount(accountReq.getAccountNumber());
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
