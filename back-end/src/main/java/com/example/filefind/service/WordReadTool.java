package com.example.filefind.service;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.InputStream;

public class WordReadTool implements ReadTool {
    @Override
    public String read(InputStream inputStream, String fileName) {
        String buffer = "";
        try {
            if (fileName.endsWith(".doc")) {
                WordExtractor ex = new WordExtractor(inputStream);
                buffer = ex.getText();
                inputStream.close();
            } else if (fileName.endsWith("ocx")) {
                XWPFDocument document = new XWPFDocument(inputStream);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(document);
                buffer = extractor.getText();
                inputStream.close();
            }
        } catch (NullPointerException e) {
            System.out.println("不用管");

        } catch (Exception e) {
//            System.out.println(e);
//            throw new RuntimeException(e);
//            e.printStackTrace();
        }
        return buffer;
    }
}
