package com.roundG0929.hibike.api.server.dto;


import com.google.gson.annotations.SerializedName;

public class SendReply {

    @SerializedName("id") private String id;
    @SerializedName("contents") private String contents;
    @SerializedName("post_id") private int post_id;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getContents() {
        return contents;
    }
    public void setContents(String contents){
        this.contents = contents;
    }

    public int getPost_id(){
        return post_id;
    }
    public void setPost_id(int post_id){
        this.post_id = post_id;
    }
}

