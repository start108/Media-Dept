package com.jy.hessed.media.util;

import com.jy.hessed.media.constant.MediaConstants;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PptUtil {

    public static void makePpt(List<Map<String, Object>> albumList) throws IOException {

        try {

//            FileInputStream fis = new FileInputStream("/Users/cjy/Templete.pptx");
            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부(Test).pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            List<String> lyricsPairsList = new ArrayList<>();

            for(Map<String, Object> albumMap : albumList) {
                for(Map.Entry<String, Object> album : albumMap.entrySet()) {

                    if(album.getKey().equals("title")) {

                        lyricsPairsList.add(album.getValue().toString());

                    } else if(album.getKey().equals("lyrics")) {

                        String lyrics = album.getValue().toString();
                        String[] lines = lyrics.split("\\n");

                        for (int i = 0; i < lines.length; i += 2) {
                            if (i + 1 < lines.length) {
                                String pair = lines[i] + "\n" + lines[i + 1];
                                lyricsPairsList.add(pair);
                            } else {
                                lyricsPairsList.add(lines[i]);
                            }
                        }
                    }
                }
            }

            XSLFSlide slideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout slideLayout = slideTemplate.getSlideLayout();

            int lastIndex = lyricsPairsList.size() - 1;
            int currentIndex = 0;

            for(String lyric : lyricsPairsList) {

                XSLFSlide slide = ppt.createSlide(slideLayout);
                XSLFTextBox textBox = slide.createTextBox();

                /*
                * 실제 사이즈
                * 헤세드 : 1.2429133858267716
                * 두나미스 : 445.48590551181104
                * */
                textBox.setAnchor(new Rectangle2D.Double(0.0, -11.0, 960.0, 94.51409448818897));
                //textBox.setAnchor(new Rectangle2D.Double(375.0, 433.0, 585.0, 94.51409448818897));
                textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
                textBox.setWordWrap(true);

                XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
                XSLFTextRun run = paragraph.addNewTextRun();
                run.setText(lyric);
                run.setFontFamily(MediaConstants.FONT_FAMILY);
                run.setFontSize(MediaConstants.FONT_SIZE);

                /*
                * paragraph.setTextAlign(TextParagraph.TextAlign.CENTER); // 헤세드
                * paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT); // 두나미스
                * */
                paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                //paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT);

                if (currentIndex == lastIndex) {
                    ppt.createSlide(slideLayout);
                }

                currentIndex++;
            }

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String currentDate = now.format(formatter);

            FileOutputStream out = new FileOutputStream("/Users/cjy/" + currentDate + MediaConstants.UPPER_DEPT + MediaConstants.EXTENSION);

            ppt.write(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getBox() {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부(Test).pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);
            fis.close();

            XSLFSlide slide = ppt.getSlides().get(0);

            for (XSLFShape shape : slide.getShapes()) {

                if (shape instanceof XSLFTextShape) {

                    XSLFTextShape textBox = (XSLFTextShape) shape;
                    double width = textBox.getAnchor().getWidth();
                    double height = textBox.getAnchor().getHeight();
                    double x = textBox.getAnchor().getX();
                    double y = textBox.getAnchor().getY();

                    System.out.println(width + " x " + height + "\n" + x + ", " + y);

                    Color fillColor = textBox.getFillColor();
                    System.out.println("텍스트 박스의 도형 채우기 색상: " + fillColor);

                    for (XSLFTextParagraph paragraph : textBox.getTextParagraphs()) {

                        for (XSLFTextRun run : paragraph.getTextRuns()) {

                            String fontFamily = run.getFontFamily();
                            double fontSize = run.getFontSize();

                            System.out.println("폰트: " + fontFamily);
                            System.out.println("폰트 크기: " + fontSize);
                        }
                    }
                }

                System.out.println("----------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
