package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetReply {
    @SerializedName("page") private int page;
    @SerializedName("post_id") private int post_id;
    @SerializedName("result") private Object result;
    @SerializedName("is_last") private String is_last;

    public int getPage(){
        return page;
    }
    public void setPage(int page){
        this.page = page;
    }
    public int getPost_id(){
        return post_id;
    }
    public void setPost_id(int page){
        this.post_id = post_id;
    }
    public Object getResult(){
        return result;
    }
    public String getLast(){
        return is_last;
    }
}