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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbManager {
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final List<String> defaultEmpty = new ArrayList<>();

    public static void createProfile(String userName, String idToken, FirebaseFirestore database,
                                     CallbackForBool cb) {
        final List<String> defaultEmpty = new ArrayList<>();
        Map<String, Object> profileDefaults = new HashMap<>();
        profileDefaults.put("name", userName);
        profileDefaults.put("businessOwner", false);
        profileDefaults.put("posts", defaultEmpty);
        profileDefaults.put("following", defaultEmpty);
        profileDefaults.put("routines", defaultEmpty);
        database.collection("users").document(idToken)
                .set(profileDefaults, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean comp = task.isComplete();
                        boolean success = task.isSuccessful();
                        cb.onCallback(comp && success);
                    }
                });
    }

    public static void createEntry(String userName, String idToken, FirebaseFirestore database,
                                   CallbackForBool cb) {
        Map<String, Object> searchEntry = new HashMap<>();
        searchEntry.put("name", userName);
        searchEntry.put("id", idToken);
        database.collection("usersearch").document("allusers")
                .update("allusers", FieldValue.arrayUnion(searchEntry))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean comp = task.isComplete();
                        boolean success = task.isSuccessful();
                        cb.onCallback(comp && success);
                    }
                });
    }

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
        database.collection("users").document(profileId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("INFO", "===== Details fetched for user " + profileId);
                    Map<String, Object> mp = task.getResult().getData();
                    Log.i("INFO", mp.toString());
                    cb.onCallback(mp);
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
                                int upvotes = (int) (long) docMap.get("upvotes");
                                if (upvotes > maxUpvotes) {
                                    maxUpvotes = upvotes;
                                    postList.add(0, docMap);
                                } else if (upvotes < minUpvotes) {
                                    minUpvotes = upvotes;
                                    postList.add(docMap);
                                } else {
                                    for (int i = 0; i < postList.size(); i++) {
                                        if (((int) (long) postList.get(i).get("upvotes")) > upvotes) {
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
    public static void followPeople(String idToken, List<String> people) {
        database.collection("users").document(idToken).update("following", FieldValue.arrayUnion(people))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("INFO", "===== Followers upload successful: " + task.isSuccessful());
                    }
                });
    }

    public static void unfollowPeople(String idToken, List<String> people) {
        database.collection("users").document(idToken).update("following", FieldValue.arrayRemove(people))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("INFO", "===== Followers removal successful: " + task.isSuccessful());
                    }
                });
    }

    public static void getAllBusinesses(CallbackForList cb) {
        database.collection("businesses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Map<String, Object>> listOfBusinesses = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        listOfBusinesses.add(doc.getData());
                    }
                    if (listOfBusinesses.size() == 0) {
                        Log.e("ERROR", "===== No businesses found!");
                    } else {
                        Log.i("INFO", "===== All businesses list retrieved");
                    }
                    cb.onCallback(listOfBusinesses);
                } else {
                    Log.e("ERROR", "===== Follower retrieval failed: " +
                            task.getException().getMessage());
                }
            }
        });
    }

    public static void getOwnersBusiness(String ownerId, CallbackForMap cb) {
        database.collection("businesses").whereEqualTo("owner", ownerId).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Map<String, Object> businessMap = new HashMap<>();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        businessMap = doc.getData();
                        break;
                    }
                    if (businessMap.size() == 0) {
                        Log.e("ERROR", "===== Business owned by user " + ownerId + " not found!");
                    } else {
                        Log.i("INFO", "===== Business owned by user " + ownerId + " retrieved");
                    }
                    cb.onCallback(businessMap);
                } else {
                    Log.e("ERROR", "===== Businesses retrieval failed: " +
                            task.getException().getMessage());
                }
            }
        });
    }

    // Use if data fields are missing in the documents in the database
    // Adjust and set default value manually
    private static void addMissingFieldsForDocs(String collectionName, String field) {
        database.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        if (doc.get(field) == null) {
                            doc.getReference().update(field, defaultEmpty).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()) {
                                        Log.i("INFO", "===== Field " + field + " updated");
                                    } else {
                                        Log.e("ERROR", "===== Field" + field + " update failed: " +
                                                task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.e("ERROR", "===== " + collectionName + " retrieval failed: " +
                            task.getException().getMessage());
                }
            }
        });
    }
}