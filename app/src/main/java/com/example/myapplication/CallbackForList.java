package com.example.myapplication;

import java.util.List;

interface CallbackForList<T> {
    void onCallback(List<T> s);
}