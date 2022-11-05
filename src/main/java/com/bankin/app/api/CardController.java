package com.bankin.app.api;

import com.bankin.app.dto.req.CardReq;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.service.CardService;
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
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<?> createCard(@RequestHeader("id") long userId,@RequestBody CardReq cardReq){
        try{
            log.info("Creating card with info: {}", cardReq);
            return ResponseEntity.ok(cardService.createCard(cardReq, userId));
        } catch (ServiceException e) {
            log.error("Get error while creating card: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
