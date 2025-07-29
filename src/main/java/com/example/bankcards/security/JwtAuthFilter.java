package com.example.bankcards.security;


import com.example.bankcards.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class    JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt;
        final String username;

        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Обработка запроса: {}", request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Убираем "Bearer "
            log.debug("JWT токен из заголовка: {}", jwt);
        } else {
            // Проверяем куки, если заголовок отсутствует
            Cookie[] cookies = request.getCookies();
            jwt = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        log.debug("JWT токен из куки: {}", jwt);
                        break;
                    }
                }
            }

            if (jwt == null) {
                log.warn("Заголовок Authorization отсутствует или имеет неверный формат, токен не найден в куках");
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            username = jwtUtil.extractUsername(jwt);
            log.info("Извлечённое имя пользователя из токена: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Аутентификация пользователя '{}' успешно выполнена", username);
                } else {
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке токена: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}