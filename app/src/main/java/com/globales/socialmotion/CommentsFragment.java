package com.globales.socialmotion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.globales.socialmotion.models.CommentListAdapter;
import com.globales.socialmotion.models.Constants;
import com.globales.socialmotion.models.FeedItem;
import com.globales.socialmotion.models.FirebaseHelper;
import com.globales.socialmotion.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends BaseFragment implements View.OnClickListener {

    FeedItem model;

    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (FeedItem) mParam1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        View v = view.findViewById(R.id.parentComment);

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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button = (Button) getActivity().findViewById(R.id.buttonPost);
        button.setOnClickListener(this);

        ListView comments = (ListView) getActivity().findViewById(R.id.commentsList);
        comments.setAdapter(new CommentListAdapter(getActivity(), model));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPost) {
            EditText editText = (EditText) getActivity().findViewById(R.id.postText);
            String comment = editText.getText().toString();
            new FirebaseHelper(getActivity()).postComment(comment, model.getId());
            editText.setText("");
        }
    }

}
