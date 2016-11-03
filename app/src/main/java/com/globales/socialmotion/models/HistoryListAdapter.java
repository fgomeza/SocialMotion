package com.globales.socialmotion.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globales.socialmotion.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Francisco on 03-Nov-16.
 */

public class HistoryListAdapter extends FirebaseListAdapter<FeedItem> {

    /**
     * @param activity    The activity containing the ListView
     */
    public HistoryListAdapter(Activity activity) {
        super(FirebaseDatabase.getInstance().getReference()
                .child(Constants.DB_USERS_NODE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.DB_USER_POSTS_NODE),
                FeedItem.class,
                R.layout.feed_item,
                activity);
    }

    @Override
    protected void populateView(View v, FeedItem model) {

        final TextView nameField = (TextView) v.findViewById(R.id.name);
        final TextView msgTxtField = (TextView) v.findViewById(R.id.msgTxt);
        final TextView timestamp = (TextView) v.findViewById(R.id.timestamp);

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                model.getTimestamp().equals("")?System.currentTimeMillis():Long.parseLong(model.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
        );

        nameField.setText(model.getName());
        msgTxtField.setText(model.getMsgTxt());
        timestamp.setText(timeAgo);
    }

}
