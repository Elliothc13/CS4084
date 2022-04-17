package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbManager {
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final List<String> defaultEmpty = new ArrayList<>();

    public static void uploadPost(String idToken, String content, Routine workout, String picturePath,
                           String videoPath) {
        Map<String, Object> post = new HashMap<>();
        post.put("content", content);
        if (workout != null) {
            post.put("workout", content);
        }
        if (picturePath != null) {
            post.put("picturePath", picturePath);
        }
        if (videoPath != null) {
            post.put("videoPath", videoPath);
        }
        post.put("creator", "/users/" + idToken);
        post.put("timePosted", FieldValue.serverTimestamp());
        post.put("upvotes", 0);

        database.collection("posts").add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("INFO", "===== Post upload ok");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERROR", "===== Uploading the post has failed: " + e.getMessage());
                    }
                });
    }

    public static void getProfileDetails(String profileId, CallbackForMap cb) {
        database.collection("users").document(profileId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("INFO", "===== Details fetched for user " + profileId);
                    cb.onCallback(task.getResult().getData());
                } else {
                    Log.e("ERROR", "===== Profile details for user " + profileId + " failed: " + task.getException().getMessage());
                }
            }
        });
    }


    // 50 newest posts are shown
    // ordered by the upvotes
    public static void getPostsForGenericFeed(CallbackForList cb) {
        database.collection("posts").orderBy("timePosted").limit(50).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> postList = new ArrayList<>();
                            int maxUpvotes = -1, minUpvotes = Integer.MAX_VALUE;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Map<String, Object> docMap = doc.getData();
                                int upvotes = (int) docMap.get("upvotes");
                                if (upvotes > maxUpvotes) {
                                    maxUpvotes = upvotes;
                                    postList.add(0, docMap);
                                } else if (upvotes < minUpvotes) {
                                    minUpvotes = upvotes;
                                    postList.add(docMap);
                                } else {
                                    for (int i = 0; i < postList.size(); i++) {
                                        if ((int) postList.get(i).get("upvotes") > upvotes) {
                                            postList.add(i, docMap);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (postList.size() == 0) {
                                Log.e("ERROR", "===== No recent posts found!");
                            } else {
                                Log.i("INFO", "===== Posts for generic feed retrieved");
                                cb.onCallback(postList);
                            }
                        } else {
                            Log.e("ERROR", "===== Post retrieval failed: " +
                                    task.getException().getMessage());
                        }
                    }
                });
    }

    public static void getFollowers(String idToken, CallbackForList cb) {
        database.collection("users").document(idToken).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<String> data = (ArrayList<String>) task.getResult().get("following");
                    cb.onCallback(data);
                    if (data == null) {
                        Log.e("ERROR", "===== Followers data for user " + idToken + " not found!");
                    } else {
                        Log.i("INFO", "===== Followers for " + idToken + " retrieved");
                    }
                } else {
                    Log.e("ERROR", "===== Follower retrieval failed: " +
                            task.getException().getMessage());
                }

            }
        });
    }
    public static void followPeople(String idToken, ArrayList<String> people) {
        database.collection("users").document(idToken).update("following", FieldValue.arrayUnion(people))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("INFO", "===== Followers upload successful: " + task.isSuccessful());
                    }
                });
    }

    public static void unfollowPeople(String idToken, ArrayList<String> people) {
        database.collection("users").document(idToken).update("following", FieldValue.arrayRemove(people))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("INFO", "===== Followers removal successful: " + task.isSuccessful());
                    }
                });
    }

    // Use if data fields are missing in the documents in the database
    private void addMissingFieldsForDocs(String collectionName, String field) {
//        database.collection(collectionName).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if (task.isSuccessful()) {
//                    DocumentReference docRef = task.getResult();
//                    if (docRef.get(field) == null) {
//                        docRef.update(field, defaultEmpty);
//                    }
//                }
//            }
//        });
    }
}