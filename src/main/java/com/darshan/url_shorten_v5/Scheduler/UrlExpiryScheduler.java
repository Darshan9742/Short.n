package com.darshan.url_shorten_v5.Scheduler;

import com.darshan.url_shorten_v5.Constants.UrlStatus;
import com.darshan.url_shorten_v5.Model.Urls;
import com.darshan.url_shorten_v5.Repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UrlExpiryScheduler {
    private final UrlRepository urlRepository;

    @Autowired
    public UrlExpiryScheduler(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedRate = 60000) //every 60 seconds
    public void expireUrls(){
        LocalDateTime now = LocalDateTime.now();

        List<Urls> expired = urlRepository.findByExpiresAtBeforeAndStatus(now, UrlStatus.ACTIVE);

        for (Urls url : expired){
            url.setStatus(UrlStatus.EXPIRED);
        }

        urlRepository.saveAll(expired);
    }
}
