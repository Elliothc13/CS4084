package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    FeedAdapter feedAdapter;
    private List<Map<String, Object>> posts;
    private AppUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = AppUser.getInstance();
        user.refreshGenericPosts();
        Context ctx = getContext();
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.i("INFO", "===== Posts refreshed in generic feed");
        posts = user.getPosts();
        Log.i("INFO", "===== Posts loaded");
        feedAdapter = new FeedAdapter(ctx, posts);
        Log.i("INFO", "===== FeedAdapter launched");
        recyclerView.setAdapter(feedAdapter);
        Log.i("INFO", "===== FeedAdapter set");


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("INFO", "===== Refreshing posts now");
                user.refreshGenericPosts();
                user.getPosts();
                FeedAdapter feedAdapter1 = new FeedAdapter(ctx, posts);
                recyclerView.setAdapter(feedAdapter1);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }
}