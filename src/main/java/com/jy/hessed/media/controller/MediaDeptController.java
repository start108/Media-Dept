package com.jy.hessed.media.controller;

import com.jy.hessed.media.service.MediaDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MediaDeptController {

    private final MediaDeptService mediaDeptService;

    @GetMapping(value = "/album")
    public String callAlbumList() {
        return mediaDeptService.callAlbumList("나로부터 시작되리"); // 주를 찾는 모든 자들이 그 사랑
    }
}
