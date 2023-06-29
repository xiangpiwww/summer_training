package com.example.filefind.controller;

import com.example.filefind.bean.FileMessage;
import com.example.filefind.bean.FileSaveData;
import com.example.filefind.mapper.FileMapper;
import com.example.filefind.service.PDFReadTool;
import com.example.filefind.service.WordReadTool;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class FileSentController {
    @Autowired
    private FileMapper fileMapper;

    @GetMapping("/download")
    public void download(String key, String ids, HttpServletResponse response) throws IOException {

        String filename = URLEncoder.encode("选中部分下载.txt", "UTF-8");

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);//浏览器上提示下载时默认的文件名

        Map<String, List<FileMessage>> dir2res = new HashMap<>();
        String[] allId = ids.split("-");
        for(String id : allId) {
            Integer integerId = Integer.parseInt(id);
            String dir = fileMapper.findDir(integerId);
            readLine(getWordPdfText(dir), key, dir2res, dir);
        }

        String txtResult = "";
        for(Map.Entry<String, List<FileMessage>> entry:dir2res.entrySet()){
            List<FileMessage> fileMessages = entry.getValue();
            txtResult = txtResult + entry.getKey() + ":\n";
            for (FileMessage fileMessage : fileMessages) {
                txtResult = txtResult + "行号：" + fileMessage.getLine() + "  " + fileMessage.getContent() + "\n";
            }
        };
        System.out.println(txtResult);

        try {//读取服务器上的文件
            ServletOutputStream out = response.getOutputStream();

            InputStream stream = new ByteArrayInputStream(txtResult.getBytes(Charset.forName("utf8")));
//            InputStream stream = new FileInputStream("D:/新建文本文档.txt")
            byte buff[] = new byte[1024];
            int length = 0;
            while ((length = stream.read(buff)) > 0) {
                out.write(buff,0,length);
            }
            stream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getWordPdfText(String dir) {
        String fileContent = null;
        if (dir.endsWith(".pdf")) {
            PDFReadTool pdfReadTool = new PDFReadTool();
            try {
                InputStream input = new FileInputStream(dir);
                fileContent = pdfReadTool.read(input, dir);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            WordReadTool wordReadTool = new WordReadTool();
            try {
                InputStream input = new FileInputStream(dir);
                fileContent = wordReadTool.read(input, dir);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fileContent;
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
