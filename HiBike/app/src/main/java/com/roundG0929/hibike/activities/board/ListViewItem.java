package com.roundG0929.hibike.activities.board;

public class ListViewItem {
    private int iconDrawable;
    private String contentStr;
    private String titleStr;
    private String nicknameStr;

    public void setTitle(String title) {
        titleStr = title;
    }
    public void setIcon(int icon) {
        iconDrawable = icon;
    }
    public void setContent(String content) {
        contentStr = content;
    }
    public void setId(String nickname) { nicknameStr = nickname; }

    public int getIcon() {
        return this.iconDrawable;
    }
    public String getContent() {
        return this.contentStr;
    }
    public String getTitle() { return this.titleStr; }
    public String getNickname() { return this.nicknameStr; }
}
