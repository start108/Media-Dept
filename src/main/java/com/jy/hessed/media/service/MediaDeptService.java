package com.jy.hessed.media.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MediaDeptService {

    private static final String CRAWLING_URL_PATH = "https://search.naver.com/search.naver?query=";

    public String searchLyrics(String query) {

        try {

            String searchUrl = CRAWLING_URL_PATH + URLEncoder.encode(query, "UTF-8");
            Document searchDoc = Jsoup.connect(searchUrl).get();

            Elements musicResultSearch = searchDoc.select("section.sc_new.sp_pmusic._fe_music_collection");

            if (musicResultSearch.size() == 0) {
                return "조회된 결과가 없습니다.";
            }

            Elements albumInfo = searchDoc.select("div.album_info").select("div.link_tit");
            List<Elements> titleList = new ArrayList<>();
            List<Elements> singerList = new ArrayList<>();

            // TODO 입력한 가수와 곡이 맞는지 체크
            for (Element element : albumInfo) {
                titleList.add(element.getElementsByTag("a").select("strong.tit"));
                singerList.add(element.getElementsByTag("div.dsc_area").select("span.name"));
            }

//            Elements lyricsElement = searchDoc.select("div[class=lyrics_txt._lyrics_txt]").select("p.lyrics");
            Elements lyricsElement = searchDoc.select("p[class=lyrics]");

//            lyricsElement.stream().forEach(x -> {
//                System.out.println(x.text());
//            });

//            List<List<String>> pairs = new ArrayList<>();
//            List<String> pair = new ArrayList<>();
//
//            for (int i = 0; i < lyricsElement.size(); i += 2) {
//
//                pair.add(String.valueOf(lyricsElement.get(i).text()));
//
//                if (i + 1 < lyricsElement.size()) {
//                    pair.add(String.valueOf(lyricsElement.get(i + 1).text()));
//                }
//
//                pairs.add(pair);
//            }
//
//            List<String> resultList = new ArrayList<>();
//            StringBuilder result = new StringBuilder();
//
//            for (List<String> innerList : pairs) {
//
//                String joined = String.join("\n", innerList);
//
//                result.append(joined).append("\n");
//
//                resultList.add(String.valueOf(result));
//            }
//
//            for (String test : resultList) {
//                System.out.println(test);
//                System.out.println("@@@@@@@@@@@");
//            }

//            makePpt(resultList);

            getBoxSize();

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
                    textBox.setAnchor(new Rectangle(100, 100, 965, 96));
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

                    System.out.println(width + " x " + height);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
