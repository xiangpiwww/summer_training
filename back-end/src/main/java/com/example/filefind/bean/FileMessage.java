package com.example.filefind.bean;

public class FileMessage {
    private int line;
    private String content;

    public FileMessage() {
    }

    public FileMessage(int line, String content) {
        this.line = line;
        this.content = content;
    }

    @Override
    public String toString() {
        return "行号：" + line +
                "内容：" + content + '\'';
    }

    public int getLine() {
        return line;
    }

    public String getContent() {
        return content;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
