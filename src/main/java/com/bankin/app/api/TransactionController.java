package com.bankin.app.api;

import com.bankin.app.dto.req.TransferReq;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.service.TransactionService;
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
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransfer(@RequestHeader("id") long userId,@RequestBody TransferReq transferReq){
        try{
            return ResponseEntity.ok(transactionService.createTransfer(userId, transferReq));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTransfer(@RequestHeader("id") long userId){
        try {
            return ResponseEntity.ok(transactionService.getBalanceChange(userId));
        } catch (ServiceException e) {
            log.error("Error occurs: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
