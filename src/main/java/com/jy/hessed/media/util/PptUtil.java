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
import java.util.List;
import java.util.Map;

public class PptUtil {

    public static void makePpt(List<Map<String, Object>> albumList) throws IOException {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/Test.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            XSLFSlide slideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout slideLayout = slideTemplate.getSlideLayout();

            List<String> lyricsPairsList = new ArrayList<>();

            for(Map<String, Object> map : albumList) {

                for(Map.Entry<String, Object> album : map.entrySet()) {

                    if(album.getKey().equals("lyrics")) {

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
                    } else {
                        // TODO 가사가 없을 시 처리
                        // return "조회된 가사가 없습니다.";
                    }
                }
            }

            if(lyricsPairsList.size() > 0) {

                int lastIndex = lyricsPairsList.size() - 1;
                int currentIndex = 0;
                XSLFSlide slide = null;

                for(String lyric : lyricsPairsList) {

                    slide = ppt.createSlide(slideLayout);
                    XSLFTextBox textBox = slide.createTextBox();

                    textBox.setAnchor(new Rectangle2D.Double(-4.965354330708662, 2.4859055118110236, 964.9653543307087, 94.51409448818897));
                    textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
                    textBox.setWordWrap(true);

                    XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
                    XSLFTextRun run = paragraph.addNewTextRun();
                    run.setText(lyric);
                    run.setFontFamily(MediaConstants.FONT_FAMILY);
                    run.setFontSize(MediaConstants.FONT_SIZE);

                    paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);

                    if (currentIndex == lastIndex) {
                        slide = ppt.createSlide(slideLayout);
                    }

                    currentIndex++;
                }
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
