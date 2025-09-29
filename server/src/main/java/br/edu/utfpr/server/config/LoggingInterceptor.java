package br.edu.utfpr.server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = getClientIpAddress(request);
        
        String fullUrl = uri + (queryString != null ? "?" + queryString : "");
        
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("requestId", java.util.UUID.randomUUID().toString().substring(0, 8));
        
        String requestId = (String) request.getAttribute("requestId");
        
        log.info("[{}] [{}] {} {} - IP: {} - User-Agent: {}", 
                requestId, timestamp, method, fullUrl, remoteAddr, userAgent);
        
        if (isWriteOperation(method)) {
            log.warn("[{}] WRITE OPERATION: {} {}", requestId, method, fullUrl);
        } else {
            log.info("[{}] READ OPERATION: {} {}", requestId, method, fullUrl);
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        long startTime = (Long) request.getAttribute("startTime");
        String requestId = (String) request.getAttribute("requestId");
        long duration = System.currentTimeMillis() - startTime;
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        
        String operationType = isWriteOperation(method) ? "WRITE" : "READ";
        
        log.info("[{}] [{}] {} {} - Status: {} - Time: {}ms - Operation: {}", 
                requestId, timestamp, method, uri, status, duration, operationType);
        
        if (ex != null) {
            log.error("[{}] ERROR in request {} {}: {}", requestId, method, uri, ex.getMessage());
        }
        
        if (status >= 400) {
            log.error("[{}] HTTP ERROR {} for {} {}", requestId, status, method, uri);
        }
    }

    private boolean isWriteOperation(String method) {
        return "POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method);
    }


    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
