package com.example.newstoday;

import java.util.*;
import java.text.*;

public class News {
    /*
     * These are vital properties of News.
     * All fields are private
     * */
    private String title;
    Date date;
    private String content;
    private String person;
    private String organization;
    private String location;
    private String category;

    News(final String title, final String date, final String content, final String category, final String organization, final String person, final String location){
        this.title = title;
        this.content = content;
        this.person = person;
        this.organization = organization;
        this.location = location;
        this.category = category;

        /*
         * Change string into date
         * */
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.date = ft.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getPerson() {
        return person;
    }

    public String getOrganization() {
        return organization;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }
}
