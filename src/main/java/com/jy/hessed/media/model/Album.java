package com.jy.hessed.media.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Album {
    private String title;
    private String singer;
    private String date;
    private List<String> lyrics;
}
