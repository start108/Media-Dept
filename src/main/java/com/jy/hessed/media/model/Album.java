package com.jy.hessed.media.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Album {
    private String title;
    private String singer;
    private String date;
    private String lyric;
    private List<String> lyrics;
}
