package com.example.project_team7;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 주현 on 2017-11-28.
 */

public class list_item {//투표방 글 목록 listview에 나타낼 항목
    private String nickname;
    private String title;
    private String time_limit;

    public list_item(String title, String nickname, String time_limit) {
        this.nickname = nickname;
        this.title = title;
        this.time_limit = time_limit;
    }

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

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }
}
