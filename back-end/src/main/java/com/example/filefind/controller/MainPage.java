package com.example.filefind.controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class MainPage {
    @GetMapping("/getDir")
    public List<String> getDir(String dir) {
        List<String> fileList = new ArrayList<String>();
        if(dir.equals("getInit")) {
            File[] files = File.listRoots();
            for (File fileName : files) {
                if(fileName.isDirectory()) {
                    fileList.add(fileName.toString());
                }
            }
        }
        else {
            File file = new File(dir);
            if(file.exists()) {
                if(file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File fileName : files) {
                        if(fileName.isDirectory()) {
                            fileList.add(fileName.getName());
                        }
                    }
                }
            }
            else {
                return null;
            }
        }
        return fileList;
    }
}
