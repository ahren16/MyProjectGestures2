package com.example.myprojectgestures2;

import java.util.List;

public class CategoryInfo {
    public final String name;
    public final String shortName;
    public final String color;
    public final List<String> videos;

    public CategoryInfo(String name, String shortName, String color, List<String> videos) {
        this.name = name;
        this.shortName = shortName;
        this.color = color;
        this.videos = videos;
    }
}
