package com.example.newsapi.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Source implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
