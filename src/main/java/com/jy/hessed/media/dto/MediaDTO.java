package com.jy.hessed.media.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MediaDTO {
    private List<Map<String, Object>> albumList;
}
