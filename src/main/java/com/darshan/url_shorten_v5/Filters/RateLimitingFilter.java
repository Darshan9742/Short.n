package com.darshan.url_shorten_v5.Filters;

import com.darshan.url_shorten_v5.RateLimit.IpExtractor;
import com.darshan.url_shorten_v5.Service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;
    private final IpExtractor ipExtractor;

    public RateLimitingFilter(RateLimitService rateLimitService, IpExtractor ipExtractor) {
        this.rateLimitService = rateLimitService;
        this.ipExtractor = ipExtractor;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String ip = ipExtractor.extractIp(request);
        String path = request.getRequestURI();

        boolean allowed = rateLimitService.allowRequest(ip, path);

        if (!allowed){
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = "{\"error\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"Too many requests. Please try again later.\"}";
            response.getWriter().print(json);
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
