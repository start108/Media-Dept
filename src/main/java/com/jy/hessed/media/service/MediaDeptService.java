package com.jy.hessed.media.service;

import com.jy.hessed.media.model.Album;
import com.jy.hessed.media.util.PptUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MediaDeptService {

    @Value("${naver.crawling.url}")
    private String crawlingUrl;

    public String searchLyrics(String query) {

        try {

            String searchUrl = crawlingUrl + URLEncoder.encode(query, "UTF-8");
            Document searchDocument = Jsoup.connect(searchUrl).get();

            Elements musicResultSearch = searchDocument.select("section.sc_new.sp_pmusic._fe_music_collection");

            // TODO Exception
            if (musicResultSearch.size() == 0) {
                return "조회된 결과가 없습니다.";
            }

            Elements listEl = musicResultSearch.select("li.list_item._sap_item");

            List<Album> albumList = new ArrayList<>();

            for(Element list : listEl) {

                String title = list.select("a[class=tit_area]").text();
                String singer = list.select("span[class=name]").select("a").text();
                String date = list.select("time[class=date]").text();
                String lyrics = list.select("p[class=lyrics]").html();

                List<String> lyricsPairsList = new ArrayList<>();

                if(!lyrics.equals("") && lyrics.contains("\n")) {

                    String[] lines = lyrics.split("\\n");

                    for (int i = 0; i < lines.length; i += 2) {
                        if (i + 1 < lines.length) {
                            String pair = lines[i] + "\n" + lines[i + 1];
                            lyricsPairsList.add(pair);
                        } else {
                            lyricsPairsList.add(lines[i]);
                        }
                    }
                } else {
                    // TODO \\n 없을 경우 처리!
                }

                Album album = Album.builder()
                        .title(title)
                        .singer(singer)
                        .date(date)
                        .lyrics(lyricsPairsList)
                        .build();

                albumList.add(album);
            }

            PptUtil.makePpt(albumList);
            // PptUtil.getBox();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
