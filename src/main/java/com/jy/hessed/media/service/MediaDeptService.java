package com.jy.hessed.media.service;

import org.apache.poi.xslf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
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
            Document searchDoc = Jsoup.connect(searchUrl).get();

            Elements musicResultSearch = searchDoc.select("section.sc_new.sp_pmusic._fe_music_collection");

            if (musicResultSearch.size() == 0) {
                return "조회된 결과가 없습니다.";
            }

            Elements musicElements = searchDoc.select("ul[class=music_list]");

            Map<String, Object> albumMap = new HashMap<>();

            for(Element musicEl : musicElements) {

                String title = musicEl.select("a[class=tit_area]").text();
                String singer = musicEl.select("span[class=name]").select("a").text();
                String date = musicEl.select("time[class=date]").text();
                String lyrics = musicEl.select("p[class=lyrics]").text();

                albumMap.put("title", title);
                albumMap.put("singer", singer);
                albumMap.put("date", date);

                System.out.println(lyrics);
            }

//            Elements albumInfo = searchDoc.select("div.album_info").select("div.link_tit");
//            List<Elements> titleList = new ArrayList<>();
//            List<Elements> lyricsList = new ArrayList<>();
//
//            for (Element element : albumInfo) {
//                titleList.add(element.select("strong.tit"));
//            }

//            Elements lyricsElement = searchDoc.select("div[class=lyrics_txt._lyrics_txt]").select("p.lyrics");
//            Elements lyricsElement = searchDoc.select("p[class=lyrics]");

//            makePpt(resultList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void makePpt(List<List<String>> lyricList) throws IOException {

        try {
            FileInputStream fis = new FileInputStream("/Users/cjy/test.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            XSLFSlide slideTemplete = ppt.getSlides().get(0);

            for (int i = 1; i <= lyricList.size(); i++) {

                XSLFSlide newSlide = ppt.createSlide();
                XSLFTextBox textBox = newSlide.createTextBox();

                for (XSLFShape shape : slideTemplete.getShapes()) {

                    newSlide.addShape(shape);
                    textBox.setText(lyricList.get(i).get(i));
                    /**
                     * 실제 텍스트 값
                     * Width : 964.9653543307087
                     * Height : 94.51409448818897
                     * X : -4.965354330708662
                     * Y : 2.4859055118110236
                     **/
                    textBox.setAnchor(new Rectangle(-5, 2, 965, 96));
                }
            }

            FileOutputStream out = new FileOutputStream("/Users/cjy/modified.pptx");

            ppt.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getBoxSize() {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);
            fis.close();

            XSLFSlide slide = ppt.getSlides().get(1);

            for (XSLFShape shape : slide.getShapes()) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textBox = (XSLFTextShape) shape;
                    double width = textBox.getAnchor().getWidth();
                    double height = textBox.getAnchor().getHeight();
                    double x = textBox.getAnchor().getX();
                    double y = textBox.getAnchor().getY();

                    System.out.println(width + " x " + height + "\n" + x + ", " + y);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
