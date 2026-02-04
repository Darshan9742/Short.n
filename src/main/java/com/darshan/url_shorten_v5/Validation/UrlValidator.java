package com.darshan.url_shorten_v5.Validation;

import org.springframework.stereotype.Component;
import org.springframework.web.util.InvalidUrlException;

import java.net.URI;
import java.util.regex.Pattern;

@Component
public class UrlValidator {

        private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/.*)?$"
        );

        public String validateUrl(String input) {

            if (input == null || input.isBlank()) {
                throw new RuntimeException("Url is blank");
            }

            String url = input.trim();

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            if (!DOMAIN_PATTERN.matcher(url).matches()) {
                throw new InvalidUrlException("Invalid URL format");
            }

            try{
                URI uri = new URI(url);

                if (uri.getHost() == null) {
                    throw new InvalidUrlException("Invalid URL host");
                }
                if (!uri.getScheme().equals("http") && !uri.getScheme().equals("https")){
                    throw new InvalidUrlException("Only HTTP/HTTPS is supported");
                }

            } catch (Exception e) {
                throw new InvalidUrlException("Malformed URL");
            }
        return url;
    }
}
