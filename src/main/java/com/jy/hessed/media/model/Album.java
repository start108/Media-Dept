package com.jy.hessed.media.model;

import lombok.Builder;

import java.util.List;

@Builder
public class Album {
    private String title;
    private String singer;
    private String date;
    private List<String> lyrics;
}
