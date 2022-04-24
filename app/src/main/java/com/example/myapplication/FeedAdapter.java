package com.example.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Map<String, Object>> posts;
    private int feedSize;

    public FeedAdapter(Context context, List<Map<String, Object>> posts) {
        this.inflater = LayoutInflater.from(context);
        this.posts = posts;
        feedSize = posts.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_feed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for (Map<String, Object> post : posts) {
            String userIdReference = ((String) post.get("creator")).split("/")[1];
            DbManager.getProfileDetails(userIdReference, new CallbackForMap() {
                @Override
                public void onCallback(Map<String, Object> profile) {
                    String name = (String) profile.get("name");
                    if (name.isEmpty()) {
                        Log.e("ERROR", "===== Name for user " + userIdReference + " is missing");
                    }
                    holder.dispName.setText(name);
                    DbManager.getReferencedImage("uploads/" + userIdReference + "/pics/profile.jpg", new CallbackForBytes() {
                        @Override
                        public void onCallback(byte[] s) {
                            holder.imgView_proPic.setImageBitmap(BitmapFactory.decodeByteArray(s, 0, s.length));
                        }
                    });
                    holder.upvoteZone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // find document id
                            String docId = (String) post.get("_docId");
                            DbManager.upvotePost(docId, AppUser.getInstance().getUpvotedPosts().contains(docId));
                        }
                    });
                }
            });
            holder.postContentView.setText((String) post.get("content"));
            holder.upvote.setText(String.valueOf(post.get("upvotes")));
            if (post.get("imagePath") != null) {
                CallbackForBytes cb2 = new CallbackForBytes() {
                    @Override
                    public void onCallback(byte[] s) {
                        holder.imgView_postPic.setImageBitmap(BitmapFactory.decodeByteArray(s, 0, s.length));
                    }
                };
                DbManager.getReferencedImage("uploads/" + userIdReference + "/posts/" + post.get("imageName"), cb2);
            }

        }
    }

    @Override
    public int getItemCount() {
        return feedSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_proPic, imgView_postPic;
        TextView dispName, postContentView, upvote;
        RelativeLayout upvoteZone;

        public ViewHolder(@NonNull View v) {
            super(v);
            dispName = (TextView) v.findViewById(R.id.dispName);
            imgView_postPic = (ImageView) v.findViewById(R.id.imgView_postPic);
            imgView_proPic = (ImageView) v.findViewById(R.id.imgView_proPic);
            postContentView = (TextView) v.findViewById(R.id.postContentView);
            upvote = (TextView) v.findViewById(R.id.upvote);
            upvoteZone = (RelativeLayout) v.findViewById(R.id.upvoteZone);
        }
    }
}