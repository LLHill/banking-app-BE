package com.bankin.app.service;

import com.bankin.app.dto.resp.GeneralInfoRes;
import com.bankin.app.entity.User;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public GeneralInfoRes getUserGeneralInfo(long userId)throws ServiceException {
        log.info("Get general info for user with id" + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
        GeneralInfoRes generalInfo = new GeneralInfoRes();
        generalInfo.setFirstName(user.getFirstName());
        generalInfo.setLastName(user.getLastName());

        return null;
    }

}
