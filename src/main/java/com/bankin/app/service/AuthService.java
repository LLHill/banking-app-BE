package com.bankin.app.service;

import com.bankin.app.dto.auth.*;
import com.bankin.app.entity.AppUser;
import com.bankin.app.enums.UserRole;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.bankin.app.enums.UserStatus.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public RegisterResp initUser(InitUserReq initUserReq) throws ServiceException{
        Optional<AppUser> userPresent = userRepository.findByEmailOrPhoneNumber(initUserReq.getEmail(), initUserReq.getPhoneNumber());

        if (userPresent.isPresent()){
            throw new ServiceException("The email or phone number is existed");
        }
        AppUser user = new AppUser(initUserReq);
        log.debug("Saving new user with data:" + user);
        userRepository.save(user);
        return new RegisterResp(user);
    }

    public RegisterResp createUserInfo(UserInfoReq userInfoReq) throws ServiceException{
        AppUser user = userRepository.findById(userInfoReq.getId())
                .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userInfoReq.getId()));
        log.debug("Saving user with new data:" + userInfoReq);
        user.setFirstName(userInfoReq.getFirstName());
        user.setLastName(userInfoReq.getLastName());
        user.setPassword(userInfoReq.getPassword());
        user.setDateOfBirth(userInfoReq.getDateOfBirth());
        user.setStatus(USER_INFO);
        userRepository.save(user);
        return new RegisterResp(user);
    }

    public void updateFaceInfo(long userId) throws ServiceException {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User does not exist ---id: " + userId));
        log.debug("Saving user with new data:" + userId);
        if (user.getStatus().step == 2){
            user.setStatus(FACE_INFO);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<AppUser> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (!userOpt.isPresent()){
            throw new UsernameNotFoundException(phoneNumber);
        }
        AppUser appUser = userOpt.get();
        return new User(phoneNumber, appUser.getPassword(), getAuthorities(appUser.getRole()));
    }

    private Collection<GrantedAuthority> getAuthorities(UserRole role){
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }
}
