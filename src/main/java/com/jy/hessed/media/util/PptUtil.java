package com.jy.hessed.media.util;

import com.jy.hessed.media.constant.MediaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class PptUtil {

    public static void makePpt(Map<String, Object> praise) {

        /** Get date to set file name **/
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MediaConstants.DATE_FORMAT);
        String currentDate = now.format(formatter);

        try (
                FileInputStream fis = new FileInputStream(MediaConstants.FILE_PATH + "Test.pptx");
                FileOutputStream fos = new FileOutputStream(MediaConstants.FILE_PATH + currentDate + MediaConstants.UPPER_DEPT + MediaConstants.EXTENSION);
        ) {

            /** PPT Template File Load **/
            XMLSlideShow ppt = new XMLSlideShow(fis);

            /** PPT Template Slide Load **/
            XSLFSlide slideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout slideLayout = slideTemplate.getSlideLayout();

            /** Create Hessed Praise PPT **/
            List<Map<String, Object>> hessedPpt = (List<Map<String, Object>>) praise.get("hessed");

            hessedPpt.forEach(hessedAlbum -> {

                hessedAlbum.entrySet().stream().filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey())).forEach(albumDetail -> {

                    if ("title".equals(albumDetail.getKey())) {

                        XSLFSlide titleSlide = ppt.createSlide(slideLayout);
                        String hessedPraiseTitle = albumDetail.getValue().toString();

                        createHessedSlide(titleSlide, new Rectangle2D.Double(0.0, 23.053858267716535, 960.0, 50.892204724409446), hessedPraiseTitle, "hessed"); // new Rectangle2D.Double(0.0, 8.0, 960.0, 50.892204724409446)
                    } else {

                        String hessedPraiseLyric = albumDetail.getValue().toString();
                        List<String> lyricsPairsList = lyricsPairs(hessedPraiseLyric);

                        for (String lyrics : lyricsPairsList) {
                            XSLFSlide lyricSlide = ppt.createSlide(slideLayout);
                            createHessedSlide(lyricSlide, new Rectangle2D.Double(0.0, 1.2429133858267716, 960.0, 94.51409448818897), lyrics, "hessed"); // new Rectangle2D.Double(0.0, -10.0, 960.0, 94.51409448818897)
                        }

                        lyricsPairsList.clear();
                    }
                });
            });

            /** Create Dunamis Praise PPT **/
            Map<String, Object> dunamisPpt = (Map<String, Object>) praise.get("dunamis");

            dunamisPpt.entrySet().stream().filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey())).forEach(albumDetail -> {

                if ("title".equals(albumDetail.getKey())) {

                    XSLFSlide titleSlide = ppt.createSlide(slideLayout);
                    String dunamisPraiseTitle = albumDetail.getValue().toString();

                    createHessedSlide(titleSlide, new Rectangle2D.Double(448.0, 489.10779527559055, 512.0, 50.892204724409446), dunamisPraiseTitle, "dunamis");
                } else {

                    String dunamisPraiseLyric = albumDetail.getValue().toString();
                    List<String> lyricsPairsList = lyricsPairs(dunamisPraiseLyric);

                    for (String lyrics : lyricsPairsList) {
                        XSLFSlide lyricSlide = ppt.createSlide(slideLayout);
                        createHessedSlide(lyricSlide, new Rectangle2D.Double(375.0, 445.48590551181104, 585.0, 94.51409448818897), lyrics, "dunamis"); // new Rectangle2D.Double(375.0, 433.0, 650.0, 94.51409448818897)
                    }

                    lyricsPairsList.clear();
                }
            });

            /** Create Final PPT **/
            ppt.write(fos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> lyricsPairs(String parameter) {

        List<String> lyricsPairsList = new ArrayList<>();

        String lyrics = parameter;
        String[] lines = lyrics.split("\\n");

        lines = Arrays.stream(lines).filter(line -> !regexCheck(line)).toArray(String[]::new);

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

    private static void createHessedSlide(XSLFSlide slide, Rectangle2D rectangle, String text, String se) {

        slide.clear();

        if("hessed".equals(se)) {

            XSLFTextBox dimmedBox = slide.createTextBox();

            dimmedBox.setAnchor(new Rectangle2D.Double(0.0, 0.0, 960.0, 97.0)); // new Rectangle2D.Double(0.0, -12.0, 960.0, 105.0)
            dimmedBox.setFillColor(new Color(13, 13, 13));
        }

        XSLFTextBox textBox = slide.createTextBox();

        textBox.setAnchor(rectangle);
        textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
        textBox.setWordWrap(true);

        XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
        XSLFTextRun run = paragraph.addNewTextRun();

        run.setText(text);
        run.setFontFamily(MediaConstants.FONT_FAMILY);
        run.setFontSize(MediaConstants.FONT_SIZE);

        if("hessed".equals(se)) {
            paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        } else {
            paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT);
        }
    }

    private static boolean regexCheck(String sentence) {

        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>x]");

        return pattern.matcher(sentence).find();
    }

    public static void getBox() {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부(Test).pptx");
            // FileInputStream fis = new FileInputStream("/Users/cjy/Templete.pptx");
            // FileInputStream fis = new FileInputStream("/Users/cjy/Test.pptx");
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
