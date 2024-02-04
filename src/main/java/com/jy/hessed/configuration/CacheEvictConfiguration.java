package com.jy.hessed.configuration;

import com.jy.hessed.media.constant.MediaConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CacheEvictConfiguration {

    @CacheEvict(value = {MediaConstants.ALBUM_INFORMATION}, allEntries = true)
    /* 1 hour */
    @Scheduled(fixedRateString = "${cache.ttl.default}")
    public void emptyAlbumInformationCache() {
    }
}
