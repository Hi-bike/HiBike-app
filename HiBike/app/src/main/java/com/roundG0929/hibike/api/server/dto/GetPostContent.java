package com.roundG0929.hibike.api.server.dto;
import com.google.gson.annotations.SerializedName;
public class GetPostContent {
    @SerializedName("post_id") private int post_id;
    @SerializedName("title") private String title;
    @SerializedName("contents") private String contents;
    @SerializedName("nickname") private String nickname;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContents(){
        return contents;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public int getPost_id(){
        return post_id;
    }

    public void setPost_id(int post_id){
        this.post_id = post_id;
    }
}
