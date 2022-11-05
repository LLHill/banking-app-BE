package com.bankin.app.service;

import com.bankin.app.dto.auth.AuthResp;
import com.bankin.app.dto.auth.InitUserReq;
import com.bankin.app.dto.auth.LoginReq;
import com.bankin.app.dto.auth.UserInfoReq;
import com.bankin.app.entity.User;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.FaceRepository;
import com.bankin.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.bankin.app.enums.UserStatus.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private AccountService accountService;

    public AuthResp userLogin(LoginReq userReq) throws ServiceException {
        User user = userRepository.findByEmail(userReq.getEmail())
                .orElseThrow(() ->new ServiceException("Wrong email or password"));
        return new AuthResp(user);
    }

    public AuthResp initUser(InitUserReq initUserReq) throws ServiceException{
        Optional<User> userPresent = userRepository.findByEmailOrPhoneNumber(initUserReq.getEmail(), initUserReq.getPhoneNumber());

        if (userPresent.isPresent()){
            throw new ServiceException("The email or phone number is existed");
        }
        User user = new User(initUserReq);
        user.setStatus(EMAIL_PHONE);
        log.debug("Saving new user with data:" + user);
        userRepository.save(user);
        return new AuthResp(user);
    }

    public AuthResp createUserInfo(UserInfoReq userInfoReq) throws ServiceException{
        User user = userRepository.findById(userInfoReq.getId())
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userInfoReq.getId()));
        log.debug("Saving user with new data:" + userInfoReq);
        user.setFirstName(userInfoReq.getFirstName());
        user.setLastName(userInfoReq.getLastName());
        user.setPassword(userInfoReq.getPassword());
        user.setDateOfBirth(userInfoReq.getDateOfBirth());
        user.setStatus(USER_INFO);
        userRepository.save(user);
        return new AuthResp(user);
    }

    public void updateFaceInfo(long userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User does not exist ---id: " + userId));
        log.debug("Saving user with new data:" + userId);
        if (user.getStatus().step == 2){
            user.setStatus(FACE_INFO);
            userRepository.save(user);
        }
    }

}
