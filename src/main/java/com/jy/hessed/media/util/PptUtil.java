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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class PptUtil {

    public static void makePpt(List<Map<String, Object>> praise) {

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

            /** PPT Template Slide(Default) Load **/
            XSLFSlide defaultSlideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout defaultSlideLayout = defaultSlideTemplate.getSlideLayout();

            /** Create Hessed Praise PPT **/
            List<Map<String, Object>> hessedPpt = praise.stream().filter(hessedAlbum -> "N".equals(hessedAlbum.get("dunamisYn"))).collect(Collectors.toList());

            hessedPpt.forEach(hessedAlbum -> {
                hessedAlbum.entrySet().stream()
                        .filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey()))
                        .forEach(albumDetail -> {

                            if ("title".equals(albumDetail.getKey())) {

                                XSLFSlide titleSlide = ppt.createSlide(defaultSlideLayout);
                                Rectangle2D titleTextBox = new Rectangle2D.Double(0.0, 23.053858267716535, 960.0, 50.892204724409446); // new Rectangle2D.Double(0.0, 8.0, 960.0, 50.892204724409446)
                                String hessedPraiseTitle = albumDetail.getValue().toString();

                                createHessedSlide(titleSlide, titleTextBox, hessedPraiseTitle, "hessed");
                            } else {

                                String hessedPraiseLyric = albumDetail.getValue().toString();
                                List<String> lyricsPairsList = lyricsPairs(hessedPraiseLyric);

                                for (String lyrics : lyricsPairsList) {
                                    XSLFSlide lyricSlide = ppt.createSlide(defaultSlideLayout);
                                    Rectangle2D lyricsTextBox = new Rectangle2D.Double(0.0, 1.2429133858267716, 960.0, 94.51409448818897); // new Rectangle2D.Double(0.0, -10.0, 960.0, 94.51409448818897)

                                    createHessedSlide(lyricSlide, lyricsTextBox, lyrics, "hessed");
                                }

                                lyricsPairsList.clear();
                            }
                        });
            });

            /** PPT Template Slide(Offering) Load **/
            XSLFSlide offeringSlideTemplate = ppt.getSlides().get(1);
            XSLFSlideLayout offeringSlideLayout = offeringSlideTemplate.getSlideLayout();

            /** Create Dunamis Praise PPT **/
            List<Map<String, Object>> dunamisPpt = praise.stream().filter(dunamisAlbum -> "Y".equals(dunamisAlbum.get("dunamisYn"))).collect(Collectors.toList());

            dunamisPpt.forEach(dunamisAlbum -> {
                dunamisAlbum.entrySet().stream()
                        .filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey()))
                        .forEach(albumDetail -> {

                            if ("title".equals(albumDetail.getKey())) {

                                XSLFSlide titleSlide = ppt.createSlide(offeringSlideLayout);
                                Rectangle2D titleTextBox = new Rectangle2D.Double(448.0, 489.10779527559055, 512.0, 50.892204724409446);
                                String dunamisPraiseTitle = albumDetail.getValue().toString();

                                createHessedSlide(titleSlide, titleTextBox, dunamisPraiseTitle, "dunamis");
                            } else {

                                String dunamisPraiseLyric = albumDetail.getValue().toString();
                                List<String> lyricsPairsList = lyricsPairs(dunamisPraiseLyric);

                                for (String lyrics : lyricsPairsList) {
                                    XSLFSlide lyricSlide = ppt.createSlide(offeringSlideLayout);
                                    Rectangle2D lyricsTextBox = new Rectangle2D.Double(375.0, 445.48590551181104, 585.0, 94.51409448818897); // new Rectangle2D.Double(375.0, 433.0, 650.0, 94.51409448818897)

                                    createHessedSlide(lyricSlide, lyricsTextBox, lyrics, "dunamis");
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
    }

    private static List<String> lyricsPairs(String parameter) {

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

    private static void createHessedSlide(XSLFSlide slide, Rectangle2D rectangle, String text, String se) {

        slide.clear();

        if ("hessed".equals(se)) {

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

        if ("hessed".equals(se)) {
            paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        } else {
            paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT);
        }
    }

    private static String removeSpecial(String sentence) {
        return sentence.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    private static boolean regexCheck(String sentence) {

        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

        return pattern.matcher(sentence).find();
    }

    public static void getBox() {

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
