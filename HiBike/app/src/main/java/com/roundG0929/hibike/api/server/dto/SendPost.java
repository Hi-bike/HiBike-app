package com.roundG0929.hibike.api.server.dto;


import com.google.gson.annotations.SerializedName;

public class SendPost {

    @SerializedName("id") private String id;
    @SerializedName("title") private String title;
    @SerializedName("contents") private String contents;
    @SerializedName("result") private String result;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents){
        this.contents = contents;
    }
    public String getResult(){
        return result;
    }
}

