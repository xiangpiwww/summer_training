package com.example.filefind;

import com.example.filefind.bean.FileMessage;
import com.example.filefind.service.PDFReadTool;
import com.example.filefind.service.WordReadTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileFindApplicationTests {

    private List<String> fileList = new ArrayList<>();
    @Test
    void contextLoads() {
        String dir = "C:/Users/33280/Desktop/2020212205145_毛修文";
        File files = new File(dir);
        Map<String, FileMessage[]> dir2res = new HashMap<>();
        if(files.isDirectory()) {
            getAllFile(files, dir);
            System.out.println(fileList.toString());
            for (String file : fileList) {
                FileMessage message = new FileMessage();
                String fileContent = null;
                if (file.endsWith(".pdf")) {
                    System.out.println("1");
                    PDFReadTool pdfReadTool = new PDFReadTool();
                    try {
                        FileInputStream input = new FileInputStream(file);
                        fileContent = pdfReadTool.read(input, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("2");
                    WordReadTool wordReadTool = new WordReadTool();
                    try {
                        InputStream input = new FileInputStream(file);
                        fileContent = wordReadTool.read(input, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(fileContent);
            }
        }
        System.out.println();
    }

    private void getAllFile(File parent, String dir) {
        File[] files = parent.listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                getAllFile(file, dir + "/" + file.getName());
            }
            else if(file.getName().endsWith(".doc")||file.getName().endsWith(".docx")||file.getName().endsWith(".pdf")){
                fileList.add(dir + "/" + file.getName());
            }
        }
    }

}
