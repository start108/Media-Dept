package com.jy.hessed.media.controller;

import com.jy.hessed.media.service.MediaDeptService;
import com.jy.hessed.media.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MediaDeptController {

    private final YouTubeService youTubeService;

    private final MediaDeptService mediaDeptService;

    @GetMapping(value = "/test1")
    public String youTubeCationExtractor() {
        return mediaDeptService.searchLyrics("주를 찾는 모든 자들이");
    }
}
