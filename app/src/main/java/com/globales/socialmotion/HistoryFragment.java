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
import android.widget.ListView;

import com.globales.socialmotion.models.FeedItem;
import com.globales.socialmotion.models.FeedListAdapter;
import com.globales.socialmotion.models.FirebaseHelper;
import com.globales.socialmotion.models.HistoryListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        listView = (ListView) activity.findViewById(R.id.feedList);

        setupFeed(activity);
    }

    private void setupFeed(Activity activity) {
        HistoryListAdapter adapter = new HistoryListAdapter(activity);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
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
                .addToBackStack(null)
                .commit();
        activity.setTitle("Comments");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FeedItem feedItem = (FeedItem) parent.getItemAtPosition(position);
        new FirebaseHelper(getActivity()).deleteMessage(feedItem.getId());
        return false;
    }
}
