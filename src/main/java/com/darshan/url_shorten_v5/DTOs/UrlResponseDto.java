package com.darshan.url_shorten_v5.DTOs;

public class UrlResponseDto {
    private String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public UrlResponseDto(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
