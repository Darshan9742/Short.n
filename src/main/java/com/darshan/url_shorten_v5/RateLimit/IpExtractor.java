package com.darshan.url_shorten_v5.RateLimit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class IpExtractor {

    public String extractIp(HttpServletRequest req){
        String ip = req.getHeader("X-Forwarded-For");

        if(ip != null && !ip.isBlank()){
            return ip.split(",")[0].trim();
        }
        //return same address of the request sent by the user
        return req.getRemoteAddr();
    }
}


