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

    public static void makePpt(List<Map<String, Object>> hessedAlbumList) {

        try {

            if(hessedAlbumList == null) {
                // TODO Exception
            }

            FileInputStream fis = new FileInputStream(MediaConstants.FILE_PATH + "Templete.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            /* Template Load */
            XSLFSlide slideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout slideLayout = slideTemplate.getSlideLayout();

            /* Create Hessed Praise PPT */
            hessedAlbumList.forEach(hessedAlbum -> {

                hessedAlbum.entrySet().stream().filter(album -> "title".equals(album.getKey()) || "lyrics".equals(album.getKey())).forEach(albumDetail -> {

                    if ("title".equals(albumDetail.getKey())) {
                        XSLFSlide titleSlide = ppt.createSlide(slideLayout);
                        createHessedSlide(titleSlide, new Rectangle2D.Double(0.0, 10.0, 960.0, 50.892204724409446), albumDetail.getValue().toString());
                    } else {

                        List<String> lyricsPairsList = pairsLyrics(albumDetail.getValue().toString());

                        for (String lyric : lyricsPairsList) {
                            XSLFSlide lyricSlide = ppt.createSlide(slideLayout);
                            createHessedSlide(lyricSlide, new Rectangle2D.Double(0.0, -12.0, 960.0, 94.51409448818897), lyric);
                        }

                        lyricsPairsList.clear();
                    }
                });
            });

            /* TODO Create Dunamis Praise PPT */


            /* Create Final PPT */
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MediaConstants.DATE_FORMAT);
            String currentDate = now.format(formatter);

            FileOutputStream out = new FileOutputStream(MediaConstants.FILE_PATH + currentDate + MediaConstants.UPPER_DEPT + MediaConstants.EXTENSION);

            ppt.write(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> pairsLyrics(String parameter) {

        List<String> lyricsPairsList = new ArrayList<>();

        String lyrics = parameter;
        String[] lines = lyrics.split("\\n");

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

    private static void createHessedSlide(XSLFSlide slide, Rectangle2D rectangle, String text) {

        /*
         * textBox.setAnchor(new Rectangle2D.Double(375.0, 433.0, 585.0, 94.51409448818897)); 두나미스 찬양 제목
         * paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT); 두나미스 찬양 제목
         * */
        XSLFTextBox textBox = slide.createTextBox();

        textBox.setAnchor(rectangle);
        textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
        textBox.setWordWrap(true);

        XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
        XSLFTextRun run = paragraph.addNewTextRun();

        run.setText(text);
        run.setFontFamily(MediaConstants.FONT_FAMILY);
        run.setFontSize(MediaConstants.FONT_SIZE);

        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
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
