package com.jy.hessed.media.controller;

import com.jy.hessed.media.service.MediaDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MediaDeptController {

    private final MediaDeptService mediaDeptService;

    @GetMapping(value = "/search")
    public String searchLyrics() {
        return mediaDeptService.searchLyrics("주를 찾는 모든 자들이");
    }
}
