package com.darshan.url_shorten_v5.Controller;

import com.darshan.url_shorten_v5.DTOs.UrlRequestDto;
import com.darshan.url_shorten_v5.DTOs.UrlResponseDto;
import com.darshan.url_shorten_v5.Model.Urls;
import com.darshan.url_shorten_v5.Service.UrlService;
import com.darshan.url_shorten_v5.Service.UrlServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api")
public class UrlShotnerController {

    private final UrlService urlService;

    UrlShotnerController(UrlServiceImpl urlServiceImpl) {
        this.urlService = urlServiceImpl;
    }


    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDto> shortenUrl(@RequestBody UrlRequestDto req) {

        Duration ttl = Duration.ofDays(7);

        //create short url
        Urls entiry = urlService.createShorUrl(req.getUrl(), ttl);


        //Assign generated short url to shortUrl var
        String shortUrl = "http://localhost:8083/api/" + entiry.getShortenUrl();

        //return status and shortUrl as body
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new  UrlResponseDto(shortUrl));
    }

    @GetMapping("/{code}") //return type is void, since its just redirect and no body is present
    public ResponseEntity<Void> getUrl(@PathVariable String code) {
        //check if short url is present
        Urls entity = urlService.resolveShortUrl(code);

        //if not present than return null
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(entity.getOriginalUrl()));

        return ResponseEntity
                .status(HttpStatus.FOUND)   //302 status code
                .headers(headers)
                .build();
    }
}
