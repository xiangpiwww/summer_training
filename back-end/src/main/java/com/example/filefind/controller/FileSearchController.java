package com.example.filefind.controller;

import com.example.filefind.bean.FileMessage;
import com.example.filefind.service.PDFReadTool;
import com.example.filefind.service.WordReadTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class FileSearchController {

    private List<String> fileList = new ArrayList<>();
    @GetMapping("/fileSearch")
    public Map<String, List<FileMessage>> search(String dir, String key) throws IOException {
        fileList.clear();
        File files = new File(dir);
        Map<String, List<FileMessage>> dir2res = new HashMap<>();
        if(files.isDirectory()) {
            getAllFile(files, dir);
            for (String file : fileList) {
                FileMessage message = new FileMessage();
                String fileContent = null;
                if (file.endsWith(".pdf")) {
                    PDFReadTool pdfReadTool = new PDFReadTool();
                    try {
                        InputStream input = new FileInputStream(file);
                        fileContent = pdfReadTool.read(input, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    WordReadTool wordReadTool = new WordReadTool();
                    try {
                        InputStream input = new FileInputStream(file);
                        fileContent = wordReadTool.read(input, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if(fileContent.contains(key)) {
                    dir2res.put(file, new ArrayList<>());
                    readLine(fileContent, key, dir2res, file);
                }
            }
        }
        else {
            return null;
        }

        return dir2res;
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

    private void readLine(String content, String key, Map<String, List<FileMessage>> dir2res, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        StringBuffer strbuf = new StringBuffer();
        List<FileMessage> fileMessages = new ArrayList<>();
        int i = 0;
        while ((line = br.readLine()) != null) {
            i++;
            if (line.contains(key)) {
                FileMessage fileMessage = new FileMessage(i, line);
                fileMessages.add(fileMessage);
            }

        }
        dir2res.put(fileName, fileMessages);
    }

}
