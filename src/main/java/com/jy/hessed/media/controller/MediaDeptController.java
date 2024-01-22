package com.jy.hessed.media.controller;

import com.jy.hessed.media.dto.MediaDTO;
import com.jy.hessed.media.service.MediaDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MediaDeptController {

    private final MediaDeptService mediaDeptService;

    @GetMapping(value = "/album")
    public MediaDTO callAlbumList(@RequestParam("title") String title) {
        return mediaDeptService.callAlbumList(title);
    }
}
