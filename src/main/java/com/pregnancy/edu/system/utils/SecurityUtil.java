package com.pregnancy.edu.system.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtil {
    @NotNull
    public static String getRemoteAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getRemoteAddr();
        }
        throw new IllegalStateException("Request attributes are not available");
    }
}
