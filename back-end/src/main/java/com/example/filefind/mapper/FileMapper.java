package com.example.filefind.mapper;

import com.example.filefind.bean.FileSaveData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("Select id from filedata where dir=#{dir}")
    public Integer findFromData(String dir);

    @Select("Select dir from filedata where id=#{id}")
    public String findDir(Integer id);

    @Select("Select max(id) from filedata")
    public Integer findMaxId();

    @Insert("insert into fileData values(#{id}, #{dir})")
    public void add(FileSaveData fileSaveData);

}