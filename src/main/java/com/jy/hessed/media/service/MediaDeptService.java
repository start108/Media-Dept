package com.jy.hessed.media.service;

import com.jy.hessed.media.constant.MediaConstants;
import com.jy.hessed.media.dto.MediaDTO;
import com.jy.hessed.media.util.PptUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MediaDeptService {

    @Value("${naver.crawling.url}")
    private String crawlingUrl;

    @Cacheable(MediaConstants.ALBUM_INFORMATION)
    public MediaDTO callAlbum(String query) {

        try {

            String searchUrl = crawlingUrl + URLEncoder.encode(query, "UTF-8");
            Document searchDocument = Jsoup.connect(searchUrl).timeout(30000).get();

            Elements musicResultSearch = searchDocument.select("section.sc_new.sp_pmusic._fe_music_collection");

            if (musicResultSearch.size() == 0) {
            }

            Elements listEl = musicResultSearch.select("li.list_item._sap_item");

            List<Map<String, Object>> albumList = new ArrayList<>();
            Map<String, Object> album = null;

            for (Element list : listEl) {

                album = new HashMap<>();

                String title = list.select("a[class=tit_area]").text();
                String singer = list.select("span[class=name]").select("a").text();
                String albumName = list.select("a[class=album]").text();
                String date = list.select("time[class=date]").text();
                String lyrics = list.select("p[class=lyrics]").html().equals("") ? "가사가 존재하지 않습니다." : list.select("p[class=lyrics]").html();
                // String base64Image = list.select("a.jacket_area.music_thumb._sap_trigger img").first().attr("src");

                album.put("title", title);
                album.put("singer", singer);
                album.put("albumName", albumName);
                album.put("date", date);
                album.put("lyrics", lyrics);

                albumList.add(album);
            }

            if (albumList.size() == 0) {
            }

            return MediaDTO.builder().albumList(albumList).build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return MediaDTO.builder().build();
    }

    public MediaDTO makePpt(List<Map<String, Object>> hessed) {

        // PptUtil.getBox();
        PptUtil.makePpt(hessed);

        return MediaDTO.builder().build();
    }
}
