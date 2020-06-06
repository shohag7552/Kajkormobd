package com.example.kajkormobd;

public class Posts {


    public String user_name,user_image, post_time, work_name, work_experience, work_description, work_location, work_amount, negotiable_condition, work_phone, user_id;


    public Posts() {

    }

    public Posts(String name, String image, String post_time, String work_name, String work_experience, String work_description, String work_location, String work_amount, String negotiable_condition, String work_phone, String user_id) {
        this.user_name = name;
        this.user_image = image;
        this.post_time = post_time;
        this.work_name = work_name;
        this.work_experience = work_experience;
        this.work_description = work_description;
        this.work_location = work_location;
        this.work_amount = work_amount;
        this.negotiable_condition = negotiable_condition;
        this.work_phone = work_phone;
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getPost_time() {
        return post_time;
    }

    public String getWork_name() {
        return work_name;
    }

    public String getWork_experience() {
        return work_experience;
    }

    public String getWork_description() {
        return work_description;
    }

    public String getWork_location() {
        return work_location;
    }

    public String getWork_amount() {
        return work_amount;
    }

    public String getNegotiable_condition() {
        return negotiable_condition;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public String getUser_id() {
        return user_id;
    }
}
