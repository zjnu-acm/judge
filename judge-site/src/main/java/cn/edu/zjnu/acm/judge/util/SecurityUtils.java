package cn.edu.zjnu.acm.judge.util;

import java.util.Optional;
import javax.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Nullable
    public static String getUserId() {
        return Optional.ofNullable(getAuthentication()).map(Authentication::getName).orElse(null);
    }

}
