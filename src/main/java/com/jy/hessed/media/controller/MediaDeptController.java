package com.jy.hessed.media.controller;

import com.jy.hessed.media.dto.MediaDTO;
import com.jy.hessed.media.service.MediaDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MediaDeptController {

    private final MediaDeptService mediaDeptService;

    @GetMapping(value = "/album")
    public MediaDTO callAlbumList(@RequestParam("title") String title) {
        return mediaDeptService.callAlbumList(title);
    }

    @PostMapping(value = "/download")
    public MediaDTO makePpt(@RequestBody Map<String, Object> hessed) throws IOException {
        return mediaDeptService.makePpt(hessed);
    }
}
