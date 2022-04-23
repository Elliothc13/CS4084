package com.example.myapplication;

public class ModelFeed {
    int id, upvote, comment,propic,postpic;
    String time, status, name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getPropic() {
        return propic;
    }

    public void setPropic(int propic) {
        this.propic = propic;
    }

    public int getPostpic() {
        return postpic;
    }

    public void setPostpic(int postpic) {
        this.postpic = postpic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelFeed(int id, int upvote, int comment, int propic, int postpic, String time, String status, String name) {
        this.id = id;
        this.upvote = upvote;
        this.comment = comment;
        this.propic = propic;
        this.postpic = postpic;
        this.time = time;
        this.status = status;
        this.name = name;


    }
}
