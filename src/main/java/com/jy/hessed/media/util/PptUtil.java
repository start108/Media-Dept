package com.jy.hessed.media.util;

import com.jy.hessed.media.model.Album;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PptUtil {

    private static final String FONT_FAMILY = "에스코어 드림 6 Bold";

    private static final double FONT_SIZE = 36.0;

    public static void makePpt(List<Album> albumList) throws IOException {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/Test.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            XSLFSlide slideTemplate = ppt.getSlides().get(0);
            XSLFSlideLayout slideLayout = slideTemplate.getSlideLayout();

            for (Album album : albumList) {

                List<String> lyricsList = album.getLyrics();

                int lastIndex = lyricsList.size() - 1;
                int currentIndex = 0;

                XSLFSlide slide = null;

                for (String lyric : lyricsList) {

                    slide = ppt.createSlide(slideLayout);
                    XSLFTextBox textBox = slide.createTextBox();

                    textBox.setAnchor(new Rectangle2D.Double(-4.965354330708662, 2.4859055118110236, 964.9653543307087, 94.51409448818897));
                    textBox.setTextAutofit(XSLFTextBox.TextAutofit.NONE);
                    textBox.setWordWrap(true);

                    XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
                    XSLFTextRun run = paragraph.addNewTextRun();
                    run.setText(lyric);
                    run.setFontFamily(FONT_FAMILY);
                    run.setFontSize(FONT_SIZE);

                    paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);

                    if(currentIndex == lastIndex) {
                        slide = ppt.createSlide(slideLayout);
                    }

                    currentIndex++;
                }
            }

            FileOutputStream out = new FileOutputStream("/Users/cjy/modified.pptx");

            ppt.write(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getBox() {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부.pptx");
            // FileInputStream fis = new FileInputStream("/Users/cjy/Test.pptx");
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
