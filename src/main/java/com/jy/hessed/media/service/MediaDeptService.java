package com.jy.hessed.media.service;

import com.jy.hessed.media.constant.MediaConstants;
import com.jy.hessed.media.dto.MediaDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public MediaDTO makePpt(List<Map<String, Object>> praise) {

        /** Get date to set file name **/
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MediaConstants.DATE_FORMAT);
        String currentDate = now.format(formatter);

        try (
                FileInputStream fis = new FileInputStream(MediaConstants.FILE_PATH + "Templete" + MediaConstants.EXTENSION);
                FileOutputStream fos = new FileOutputStream(MediaConstants.FILE_PATH + currentDate + MediaConstants.UPPER_DEPT + MediaConstants.EXTENSION);
        ) {

            /** PPT Template File Load **/
            XMLSlideShow ppt = new XMLSlideShow(fis);

            /** Create Hessed Praise PPT **/
            List<Map<String, Object>> hessedPpt = praise.stream().filter(hessedAlbum -> "N".equals(hessedAlbum.get("dunamisYn"))).collect(Collectors.toList());

            hessedPpt.forEach(hessedAlbum -> {
                hessedAlbum.entrySet().stream()
                        .filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey()))
                        .forEach(albumDetail -> {

                            String value = albumDetail.getValue().toString();

                            if ("title".equals(albumDetail.getKey())) {
                                createSlide(ppt,"hessed", "hessedTitle", value);
                            } else {

                                List<String> lyricsPairsList = lyricsPairs(value);

                                for (String lyrics : lyricsPairsList) {
                                    createSlide(ppt, "hessed", "hessedContent", lyrics);
                                }

                                lyricsPairsList.clear();
                            }
                        });
            });

            /** Create Dunamis Praise PPT **/
            List<Map<String, Object>> dunamisPpt = praise.stream().filter(dunamisAlbum -> "Y".equals(dunamisAlbum.get("dunamisYn"))).collect(Collectors.toList());

            dunamisPpt.forEach(dunamisAlbum -> {
                dunamisAlbum.entrySet().stream()
                        .filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey()))
                        .forEach(albumDetail -> {

                            String value = albumDetail.getValue().toString();

                            if ("title".equals(albumDetail.getKey())) {
                                createSlide(ppt,"dunamis", "dunamisTitle", value);
                            } else {

                                List<String> lyricsPairsList = lyricsPairs(value);

                                for (String lyrics : lyricsPairsList) {
                                    createSlide(ppt,"dunamis", "dunamisContent", lyrics);
                                }

                                lyricsPairsList.clear();
                            }
                        });
            });

            /** Create Final PPT **/
            ppt.write(fos);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return MediaDTO.builder().build();
    }

    private List<String> lyricsPairs(String parameter) {

        List<String> lyricsPairsList = new ArrayList<>();

        String lyrics = parameter;
        String[] lines = lyrics.split("\\n");

//        lines = Arrays.stream(lines)
//                .map(line -> removeSpecial(line))
//                .toArray(String[]::new);

        for (int i = 0; i < lines.length; i += 2) {
            if (i + 1 < lines.length) {
                String pair = lines[i] + "\n" + lines[i + 1];
                lyricsPairsList.add(pair);
            } else {
                lyricsPairsList.add(lines[i]);
            }
        }

        return lyricsPairsList;
    }

    private void createSlide(XMLSlideShow ppt, String dept, String contentDiv, String value) {

        if(contentDiv.contains("hessed")) {
            System.out.println("문자열 포함됨");
        } else {
            System.out.println("문자열 포함되지 않음");
        }

//        /** PPT Template Slide Load **/
//        XSLFSlide templete = "hessed".equals(dept) ? ppt.getSlides().get(0) : ppt.getSlides().get(1);
//        XSLFSlideLayout slideLayout = templete.getSlideLayout();
//        XSLFSlide slide = ppt.createSlide(slideLayout);
//
//        slide.clear();
//
//        /** Create Dimmed Box **/
//        if ("hessed".equals(dept)) {
//
//            XSLFTextBox dimmedBox = slide.createTextBox();
//
//            dimmedBox.setAnchor(new Rectangle2D.Double(0.0, 0.0, 960.0, 97.0)); // new Rectangle2D.Double(0.0, -12.0, 960.0, 105.0)
//            dimmedBox.setFillColor(new Color(13, 13, 13));
//        }
//
//        /** Create Text Box **/
//        XSLFTextBox textBox = slide.createTextBox();
//        Rectangle2D rectangle = null;
//
//        switch(contentDiv) {
//            case "hessedTitle":
//
//                rectangle = new Rectangle2D.Double(0.0, 23.053858267716535, 960.0, 50.892204724409446); // new Rectangle2D.Double(0.0, 8.0, 960.0, 50.892204724409446)
//                break;
//            case "hessedContent":
//
//                rectangle = new Rectangle2D.Double(0.0, 1.2429133858267716, 960.0, 94.51409448818897); // new Rectangle2D.Double(0.0, -10.0, 960.0, 94.51409448818897)
//                break;
//            case "dunamisTitle":
//
//                rectangle = new Rectangle2D.Double(448.0, 489.10779527559055, 512.0, 50.892204724409446);
//                break;
//            case "dunamisContent":
//
//                rectangle = new Rectangle2D.Double(375.0, 445.48590551181104, 585.0, 94.51409448818897); // new Rectangle2D.Double(375.0, 433.0, 650.0, 94.51409448818897)
//                break;
//            default:
//                break;
//        }
//
//        textBox.setAnchor(rectangle);
//        textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
//        textBox.setWordWrap(true);
//
//        XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
//        XSLFTextRun run = paragraph.addNewTextRun();
//
//        run.setText(value);
//        run.setFontFamily(MediaConstants.FONT_FAMILY);
//        run.setFontSize(MediaConstants.FONT_SIZE);
//
//        if ("hessed".equals(dept)) {
//            paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
//        } else {
//            paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT);
//        }
    }

    private boolean regexCheck(String sentence) {

        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

        return pattern.matcher(sentence).find();
    }

    private void getBox() {

        try (FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부(Test).pptx")) {

            XMLSlideShow ppt = new XMLSlideShow(fis);

            XSLFSlide slide = ppt.getSlides().get(0);

            for (XSLFShape shape : slide.getShapes()) {

                if (shape instanceof XSLFTextShape) {

                    XSLFTextShape textBox = (XSLFTextShape) shape;
                    double width = textBox.getAnchor().getWidth();
                    double height = textBox.getAnchor().getHeight();
                    double x = textBox.getAnchor().getX();
                    double y = textBox.getAnchor().getY();

                    Color fillColor = textBox.getFillColor();

                    System.out.println("Width : " + width + "\n" + "Height : " + height + "\n" + "x : " + x + "\n" + "y : " + y);
                    System.out.println("TextBox Fill Color : " + fillColor);

                    for (XSLFTextParagraph paragraph : textBox.getTextParagraphs()) {
                        for (XSLFTextRun run : paragraph.getTextRuns()) {

                            String fontFamily = run.getFontFamily();
                            double fontSize = run.getFontSize();

                            System.out.println("Font : " + fontFamily);
                            System.out.println("Font Size : " + fontSize);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
