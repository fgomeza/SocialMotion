package com.globales.socialmotion.models;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.globales.socialmotion.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Francisco on 02-Nov-16.
 */

public class CommentListAdapter extends FirebaseListAdapter<FeedItem> {

    FeedItem model;

    /**
     * @param activity    The activity containing the ListView
     */
    public CommentListAdapter(Activity activity, FeedItem model) {
        super(FirebaseDatabase.getInstance().getReference().child(Constants.DB_FEED_NODE)
                .child(model.getId()).child(Constants.DB_COMMENTS_NODE),
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
