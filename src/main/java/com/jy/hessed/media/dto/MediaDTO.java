package com.jy.hessed.media.dto;

import com.jy.hessed.media.model.Album;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MediaDTO {
    private List<Album> albumList;
}
