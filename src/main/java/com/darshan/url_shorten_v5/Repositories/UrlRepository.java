package com.darshan.url_shorten_v5.Repositories;

import com.darshan.url_shorten_v5.Constants.UrlStatus;
import com.darshan.url_shorten_v5.Model.Urls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Urls, Long> {

    Optional<Urls> findByOriginalUrl(String originalUrl);

    Optional<Urls> findByShortenUrl(String shortenUrl);

    boolean existsByShortenUrl(String shortenUrl);

    List<Urls> findByExpiresAtBeforeAndStatus(LocalDateTime expiresAtBefore, UrlStatus status);
}
