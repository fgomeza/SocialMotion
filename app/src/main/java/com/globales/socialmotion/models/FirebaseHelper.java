package com.globales.socialmotion.models;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Francisco on 03-Nov-16.
 */

public class FirebaseHelper {

    Activity activity;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void postMessage(String msgTxt) {
        if(msgTxt == null || msgTxt.isEmpty()) return;

        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.DB_FEED_NODE);
        post(msgTxt, dbRef, true, "");
    }

    public void postComment(String msgTxt, String parentMessageId) {
        if(msgTxt == null || msgTxt.isEmpty()) return;
        if(parentMessageId == null || parentMessageId.isEmpty()) return;

        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.DB_FEED_NODE)
                .child(parentMessageId)
                .child(Constants.DB_COMMENTS_NODE);
        post(msgTxt, dbRef, false, parentMessageId);

    }

    private void post(final String msgTxt, final DatabaseReference messagesDbRef, final boolean d, final String parent) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(uid);
        final DatabaseReference usersDbRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.DB_USERS_NODE).child(uid);

        usersDbRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String name = user.getFirstName() + " " + user.getLastName();
                        String timeStamp = String.valueOf(System.currentTimeMillis());

                        FeedItem item = new FeedItem(name, msgTxt, timeStamp);
                        DatabaseReference newItem = messagesDbRef.push();
                        item.setId(newItem.getKey());
                        newItem.setValue(item);

                        if(d) {
                            usersDbRef.child(Constants.DB_USER_POSTS_NODE)
                                    .child(newItem.getKey())
                                    .setValue(item);
                        } else {
                            usersDbRef.child(Constants.DB_USER_POSTS_NODE)
                                    .child(parent)
                                    .child(Constants.DB_COMMENTS_NODE)
                                    .child(newItem.getKey())
                                    .setValue(item);
                        }

                        Toast.makeText(activity, "Su comentario ha sido publicado", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void deleteMessage(String id) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child(Constants.DB_USERS_NODE)
                .child(uid)
                .child(Constants.DB_USER_POSTS_NODE)
                .child(id)
                .removeValue();
        dbRef.child(Constants.DB_FEED_NODE)
                .child(id)
                .removeValue();
   }
}