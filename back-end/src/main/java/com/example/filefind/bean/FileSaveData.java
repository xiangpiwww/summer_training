package com.example.filefind.bean;


import org.springframework.data.relational.core.sql.In;

public class FileSaveData {
    private Integer id;
    private String  dir;

    public FileSaveData() {
    }

    public FileSaveData(Integer id, String dir) {
        this.id = id;
        this.dir = dir;
    }

    public Integer getId() {
        return id;
    }

    public String getDir() {
        return dir;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "id=" + id + ", dir='" + dir + '\'';
    }

    @Override
    public boolean equals(Object o){
        return this.id.equals(((FileSaveData) o).getId());
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}

