package com.example.myapplication;

public class Item {
    private int mImageResource;
    private String mText1, mText2, mText3;

    public Item(int imageResource, String text1, String text2, String text3){
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
    }

    public void changeText2(String text){
        mText2 = text;
    }
    public void changeText3(String text){
        mText3 = text;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    public String getText3(){
        return mText3;
    }
}
