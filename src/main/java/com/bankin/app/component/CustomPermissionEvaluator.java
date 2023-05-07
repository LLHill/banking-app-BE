package com.bankin.app.component;

import com.bankin.app.entity.Account;
import com.bankin.app.entity.AppUser;
import com.bankin.app.enums.AccountStatus;
import com.bankin.app.exception.ServiceException;
import com.bankin.app.repository.UserRepository;
import com.bankin.app.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component("customPermissionEvaluator")
@Slf4j
public class CustomPermissionEvaluator {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    public boolean isAccountActive(){
        try{
            int userId = Integer.parseInt(request.getHeader("id"));
            Account account = accountService.getAccountByUserId(userId);
            AccountStatus status = account.getStatus();
            if (Objects.equals(status, AccountStatus.ACTIVE)){
                return true;
            }
            log.error("The account status is INACTIVE");
            throw new AccessDeniedException("The account status is INACTIVE");
        } catch (NumberFormatException e){
            log.error("User id does not have the right format");
            throw new AccessDeniedException("User id does not have the right format");
        } catch (ServiceException e){
            log.error("Error occurs {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }
    }

//    public boolean isMatchUserId(){
//        try{
//            long userId = Long.parseLong(request.getHeader("id"));
//            AppUser user = userRepository.findById(userId)
//                    .orElseThrow(() ->new ServiceException("User does not exist ---id: "+ userId));
//            AccountStatus status = account.getStatus();
//            if (Objects.equals(status, AccountStatus.ACTIVE)){
//                return true;
//            }
//            log.error("The account status is INACTIVE");
//            return false;
//        } catch (NumberFormatException e){
//            log.error("User id does not have the right format");
//            return false;
//        } catch (ServiceException e){
//            log.error("Error occurs {}", e.getMessage());
//            return false;
//        }
//    }
}
