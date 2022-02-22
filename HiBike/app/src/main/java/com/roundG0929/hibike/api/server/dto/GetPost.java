package com.roundG0929.hibike.api.server.dto;
import com.google.gson.annotations.SerializedName;
public class GetPost {
    @SerializedName("page") private int page;
    @SerializedName("result") private Object result;

    public int getPage(){
        return page;
    }

    public void setPage(int page){
        this.page = page;
    }

    public Object getResult(){
        return result;
    }
}
