package com.globales.socialmotion.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globales.socialmotion.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Francisco on 23-May-16.
 */
public class FeedListAdapter extends FirebaseListAdapter<FeedItem> {

    private final FirebaseStorage storage;

    /**
     * @param activity    The activity containing the ListView
     */
    public FeedListAdapter(Activity activity) {
        super(FirebaseDatabase.getInstance().getReference().child(Constants.DB_FEED_NODE),
                FeedItem.class,
                R.layout.feed_item,
                activity,
                true);

        storage = FirebaseStorage.getInstance();
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

    private void sendFound(final FeedItem item) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Constants.DB_FEED_NODE).child(item.getId()).setValue(item);
    }


    private void setImage(final ImageView imageView, String imageUrl) {
        StorageReference ref = FirebaseStorage
                .getInstance().getReference()
                .child(Constants.STORAGE_IMAGES)
                .child(imageUrl);

        OnSuccessfulImageRetrieval listener = new OnSuccessfulImageRetrieval(imageView);

        ref.getBytes(Constants.ONE_MEGABYTE).addOnSuccessListener(listener);
    }

    class OnSuccessfulImageRetrieval implements OnSuccessListener<byte[]> {

        private ImageView imageView;

        public OnSuccessfulImageRetrieval(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onSuccess(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = imageView.getWidth();
            params.height = imageView.getWidth() * bitmap.getHeight() / bitmap.getWidth();
            imageView.setLayoutParams(params);
        }
    }

}
