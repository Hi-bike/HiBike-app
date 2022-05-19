package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostMyBoard {
    @Expose
    @SerializedName("result")
    ArrayList<PostMyBoard.Result> results = new ArrayList<>();

    public ArrayList<PostMyBoard.Result> getResults() {
        return results;
    }

    public class Result {
        @Expose
        @SerializedName("nickname")
        String nickname;

        @Expose
        @SerializedName("title")
        String title;

        @Expose
        @SerializedName("contents")
        String contents;

        @Expose
        @SerializedName("time")
        String time;

        @Expose
        @SerializedName("board_id")
        int boardId;

        @Expose
        @SerializedName("count")
        int count;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getBoardId() {
            return boardId;
        }

        public void setBoardId(int boardId) {
            this.boardId = boardId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }
    }
}
