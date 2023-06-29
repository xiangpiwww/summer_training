package com.example.filefind.controller;

import com.sun.jndi.toolkit.url.Uri;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;

@RestController
@CrossOrigin
public class FileSentController {

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws UnsupportedEncodingException {

        String filename = URLEncoder.encode("文件名.txt", "UTF-8");

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);//浏览器上提示下载时默认的文件名

        try (ServletOutputStream out = response.getOutputStream();
             InputStream stream = new FileInputStream("D:/新建文本文档.txt")){//读取服务器上的文件

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
}
