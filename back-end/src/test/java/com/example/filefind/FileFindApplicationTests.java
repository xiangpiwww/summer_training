package com.example.filefind;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

class FileFindApplicationTests {

    @Test
    void contextLoads() {
        File[] files = File.listRoots();
        System.out.println(files[0].toString());
    }

}
