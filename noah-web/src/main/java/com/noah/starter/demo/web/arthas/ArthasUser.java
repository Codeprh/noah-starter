package com.noah.starter.demo.web.arthas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ArthasUser implements Serializable {

    private String name;
    private int age;
    private List<String> hobbies;
}
