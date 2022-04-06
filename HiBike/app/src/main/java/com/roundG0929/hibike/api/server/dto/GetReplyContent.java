package com.roundG0929.hibike.api.server.dto;
import com.google.gson.annotations.SerializedName;
public class GetReplyContent {
    @SerializedName("reply_id") private int reply_id;
    @SerializedName("contents") private String contents;
    @SerializedName("nickname") private String nickname;

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

    public int getReply_id(){
        return reply_id;
    }

    public void setReply_id(int reply_id){
        this.reply_id = reply_id;
    }
}
