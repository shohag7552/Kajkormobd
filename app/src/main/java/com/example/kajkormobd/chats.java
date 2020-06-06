package com.example.kajkormobd;

public class chats {
    private String user_image,user_name,work_name;

    public chats() {
    }

    public chats(String user_image, String user_name, String work_name) {
        this.user_image = user_image;
        this.user_name = user_name;
        this.work_name = work_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getWork_name() {
        return work_name;
    }
}
