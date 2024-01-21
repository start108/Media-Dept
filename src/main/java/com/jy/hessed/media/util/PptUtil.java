package com.jy.hessed.media.util;

import com.jy.hessed.media.model.Album;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PptUtil {

    private static final String FONT_FAMILY = "에스코어 드림 6 Bold";

    private static final double FONT_SIZE = 36.0;

    public static void makePpt(List<Album> albumList) throws IOException {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/Test.pptx");
            XMLSlideShow ppt = new XMLSlideShow(fis);

            fis.close();

            XSLFSlide slideTemplete = ppt.getSlides().get(0);
            XSLFTextBox textBox = slideTemplete.createTextBox();

            textBox.setText("Text in master slide text box");
            //textBox.setFillColor(new Color(13, 13, 13));

            /* 폰트 세팅 */
            for (XSLFTextParagraph paragraph : textBox.getTextParagraphs()) {
                for (XSLFTextRun run : paragraph.getTextRuns()) {
                    run.setFontFamily(FONT_FAMILY);
                    run.setFontSize(FONT_SIZE);
                }
            }
            /**
             * 실제 텍스트 값
             * Width : 964.9653543307087
             * Height : 94.51409448818897
             * X : -4.965354330708662
             * Y : 2.4859055118110236
             **/
            textBox.setAnchor(new Rectangle(-5, 2, 965, 96));
            textBox.setVerticalAlignment(VerticalAlignment.MIDDLE);
            textBox.setHorizontalCentered(true);

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

    public static void getBox() {

        try {

            FileInputStream fis = new FileInputStream("/Users/cjy/2024 청년부.pptx");
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
