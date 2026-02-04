package com.darshan.url_shorten_v5.Service;

import com.darshan.url_shorten_v5.Cache.RedisUrlCacheService;
import com.darshan.url_shorten_v5.Constants.UrlStatus;
import com.darshan.url_shorten_v5.Core.Base62Encoder;
import com.darshan.url_shorten_v5.Core.CollisionException;
import com.darshan.url_shorten_v5.Model.Urls;
import com.darshan.url_shorten_v5.Repositories.UrlRepository;
import com.darshan.url_shorten_v5.Validation.UrlValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UrlServiceImpl implements UrlService{
    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final UrlValidator urlValidator;
    private final RedisUrlCacheService redisUrlCacheService;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, Base62Encoder base62Encoder,
                          UrlValidator urlValidator, RedisUrlCacheService redisUrlCacheService) {
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.urlValidator = urlValidator;
        this.redisUrlCacheService = redisUrlCacheService;
    }

    @Override
    public Urls createShorUrl(String originalUrl, Duration ttl) {

        //validating url
        String normalizedUrl = urlValidator.validateUrl(originalUrl);

        //finding if url is already present
        return urlRepository.findByOriginalUrl(normalizedUrl)

                //generate new short link
                .orElseGet(()->{
                    String shortUrl = generateUniqueHash();

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime expiry = now.plus(ttl);

                    Urls url = Urls.builder()
                          .originalUrl(normalizedUrl)
                          .shortenUrl(shortUrl)
                          .createdAt(now)
                          .expiresAt(expiry)
                          .status(UrlStatus.ACTIVE)
                          .build();
                    return urlRepository.save(url);
                });
    }

    @Override
    public Urls resolveShortUrl(String shortUrl) {

        //Check cache, if present return url, else db lookup
        String cacheUrl = redisUrlCacheService.getUrl(shortUrl);
        if (cacheUrl != null) {
            return Urls.builder()
                    .originalUrl(cacheUrl)
                    .shortenUrl(shortUrl)
                    .status(UrlStatus.ACTIVE)
                    .build();
        }

        //Db lookup
        Urls url = urlRepository.findByShortenUrl(shortUrl).orElse(null);

        if (url == null){
            return null;
        }

        if (url.getStatus() == UrlStatus.EXPIRED)
            return null;

        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())){
            url.setStatus(UrlStatus.EXPIRED);
            urlRepository.save(url);
            redisUrlCacheService.evict(shortUrl); // clear from cache as well
            return null;
        }

        //Store in cache
        Duration ttl = Duration.between(LocalDateTime.now(), url.getExpiresAt());
        redisUrlCacheService.putUrl(shortUrl, url.getOriginalUrl(), ttl);

        return url;
    }

    //Collision control
    private String generateUniqueHash(){
        String hash;
        int attempts = 0;

        do {
            if (attempts++ > 10) {
                throw new CollisionException("Hash collision limit exceeded");
            }
            hash = base62Encoder.generateHash();
        }while (urlRepository.existsByShortenUrl(hash));

        return hash;
    }
}
