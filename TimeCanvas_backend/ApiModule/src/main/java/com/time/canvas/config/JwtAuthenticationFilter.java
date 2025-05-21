package com.time.canvas.config;

import com.time.canvas.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getServletPath();

            // 如果是不需要认证的路径，直接放行
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            log.debug("Received Authorization header: {}", authHeader);

            // 检查认证头
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("缺少认证头或格式不正确: {}", authHeader);
                sendUnauthorizedError(response, "缺少认证头或格式不正确");
                return;
            }

            String token = authHeader.substring(7).trim();
            if (token.isEmpty()) {
                log.warn("空的 token");
                sendUnauthorizedError(response, "空的 token");
                return;
            }

            try {
                String username = jwtUtil.getUsernameFromToken(token);
                log.debug("Extracted username from token: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication set for user: {}", username);
                    filterChain.doFilter(request, response);
                } else {
                    log.warn("无效的 token 或用户名为空");
                    sendUnauthorizedError(response, "无效的 token 或用户名为空");
                }
            } catch (ExpiredJwtException e) {
                log.warn("Token 已过期: {}", e.getMessage());
                sendUnauthorizedError(response, "Token 已过期");
            } catch (JwtException e) {
                log.warn("无效的 token: {}", e.getMessage());
                sendUnauthorizedError(response, "无效的 token");
            }
        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage(), e);
            sendUnauthorizedError(response, "认证失败");
        }
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/") ||
                path.startsWith("/api/user/register") ||
                path.startsWith("/api/user/login") ||
                path.startsWith("/user/register") ||
                path.startsWith("/user/login") ||
                path.startsWith("/doc") ||
                path.startsWith("/webjars/") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/v2/api-docs") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/api/doc.html") ||
                path.startsWith("/api/webjars/") ||
                path.startsWith("/api/swagger-resources") ||
                path.startsWith("/api/v2/api-docs") ||
                path.startsWith("/api/v3/api-docs") ||
                path.startsWith("/knife4j") ||
                path.equals("/test/health");
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(401, message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private static class ErrorResponse {
        private final int code;
        private final String message;

        public ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}