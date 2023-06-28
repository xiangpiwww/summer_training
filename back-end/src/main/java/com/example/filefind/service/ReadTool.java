package com.example.filefind.service;

import java.io.InputStream;

public interface ReadTool {
    String read(InputStream inputStream, String fileName);
}
