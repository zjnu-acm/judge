package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = APPLICATION_JSON_VALUE)
public class UserprofileController {

    private final AccountService accountService;

    @Secured("ROLE_USER")
    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        final String userId = SecurityUtils.getUserId();
        accountService.updateSelective(userId, user.toBuilder().password(null).build());
        SecurityUtils.getAuthentication().setAuthenticated(false);
    }

}
