package com.globales.socialmotion;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.globales.socialmotion.models.Constants;
import com.globales.socialmotion.models.FeedItem;
import com.globales.socialmotion.models.FeedListAdapter;
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
 * Activities that contain this fragment must implement the
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = FeedFragment.class.getSimpleName();

    private Button postBtn;
    private EditText postText;
    private ListView listView;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        postBtn = (Button) activity.findViewById(R.id.postBtn);
        postText = (EditText) activity.findViewById(R.id.postText);
        listView = (ListView) activity.findViewById(R.id.feedList);

        setupFeed(activity);

        postBtn.setOnClickListener(this);
        postText.clearFocus();
        postBtn.requestFocusFromTouch();
    }

    private void setupFeed(Activity activity) {
        FeedListAdapter adapter = new FeedListAdapter(activity);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.postBtn) {
            String msgTxt = postText.getText().toString();
            new FirebaseHelper(getActivity()).postMessage(msgTxt);
            postText.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FeedItem feedItem = (FeedItem) parent.getItemAtPosition(position);

        CommentsFragment fragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseFragment.ARG_PARAM1, feedItem);
        fragment.setArguments(bundle);

        FragmentActivity activity = getActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        activity.setTitle("Comments");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FeedItem feedItem = (FeedItem) parent.getItemAtPosition(position);
        return false;
    }
}
