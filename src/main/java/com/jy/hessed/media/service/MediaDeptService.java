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
            Document searchDoc = Jsoup.connect(searchUrl).get();

            Elements musicResultSearch = searchDoc.select("section.sc_new.sp_pmusic._fe_music_collection");

            if (musicResultSearch.size() == 0) {
                return "조회된 결과가 없습니다.";
            }

            Elements musicElements = searchDoc.select("ul[class=music_list]");


            Map<String, Object> albumMap = new HashMap<>();
            List<Map<String, Object>> albumList = new ArrayList<>();
            List<String> lyricsList = new ArrayList<>();

            for(Element musicEl : musicElements) {

                Elements listEl = musicEl.select("li.list_item._sap_item");

                for(Element list : listEl) {

                    String title = list.select("a[class=tit_area]").text();
                    String singer = list.select("span[class=name]").select("a").text();
                    String date = list.select("time[class=date]").text();
                    String lyrics = list.select("p[class=lyrics]").html();

                    albumMap.put("title", title);
                    albumMap.put("singer", singer);
                    albumMap.put("date", date);
                    albumMap.put("lyrics", lyrics);

                    albumList.add(albumMap);
                }
            }

            for(Map<String, Object> map : albumList) {

                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println("[key]:" + entry.getKey() + ", [value]:" + entry.getValue());
                }
            }

            makePpt(albumList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void makePpt(List<Map<String, Object>> albumList) throws IOException {

        try {
            FileInputStream fis = new FileInputStream("/Users/cjy/test.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            XSLFSlide slideTemplete = ppt.getSlides().get(0);

            // Create a text box
            XSLFTextBox textBox = slideTemplete.createTextBox();
            textBox.setText("Text at custom position");

            // Set the position of the text box
            textBox.setAnchor(new java.awt.Rectangle(100, 100, 200, 50));

//            for (int i = 1; i <= albumList.size(); i++) {
//
//                for (XSLFShape shape : slideTemplete.getShapes()) {
//
//                    XSLFSlide newSlide = ppt.createSlide();
//                    XSLFTextBox textBox = newSlide.createTextBox();
//
////                    newSlide.addShape(shape);
//////                    textBox.setText((String) albumList.get(i).get("lyrics"));
////                    textBox.setText("test");
////                    /**
////                     * 실제 텍스트 값
////                     * Width : 964.9653543307087
////                     * Height : 94.51409448818897
////                     * X : -4.965354330708662
////                     * Y : 2.4859055118110236
////                     **/
////                    textBox.setAnchor(new Rectangle(-5, 2, 965, 96));
//
//                    textBox.setText("Text at custom position");
//
//                    // Set the position of the text box
//                    textBox.setAnchor(new java.awt.Rectangle(100, 100, 200, 50));
//                }
//            }

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
