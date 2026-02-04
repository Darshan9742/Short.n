package com.darshan.url_shorten_v5.Service;

import com.darshan.url_shorten_v5.Model.Urls;

import java.time.Duration;

public interface UrlService {

    Urls createShorUrl(String originalUrl, Duration ttl);

    Urls resolveShortUrl(String shortUrl);
}
