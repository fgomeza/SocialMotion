package com.globales.socialmotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globales.socialmotion.models.Constants;
import com.globales.socialmotion.models.FeedItem;
import com.globales.socialmotion.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends BaseFragment implements View.OnClickListener {

    private static int RESULT_LOAD_IMG = 1;
    private static final String TAG = ReportsFragment.class.getSimpleName();
    LatLng latLng = null;

    private ImageView mSelectedImage;
    EditText msgTxtField;
    EditText nameField;
    TextView locationField;
    Address address;
    FeedItem.MyAddress myAddress;


    public ReportsFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportsFragment newInstance(String param1, String param2) {
        ReportsFragment fragment = new ReportsFragment();
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
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        mSelectedImage = (ImageView)view.findViewById(R.id.imageView3);
        msgTxtField = (EditText) view.findViewById(R.id.msgTxtField);
        nameField = (EditText) view.findViewById(R.id.nameField);
        locationField = (TextView) view.findViewById(R.id.location);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindButton();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postReportBtn:
                sendReport();
                break;
            case R.id.btnUpload:
                uploadPicture();
                break;
            case R.id.btnMap:
                Intent i = new Intent(getActivity(), MapLocation.class);
                startActivityForResult(i,2);
                break;
        }
    }

    public void uploadPicture(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMG);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG  && resultCode == Activity.RESULT_OK) {
            HomeActivity activity = (HomeActivity) getActivity();
            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromIntentData(data, activity);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            mSelectedImage.setImageBitmap(bitmap);
        }

        if (requestCode == 2) {
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getParcelableExtra("bundle");
                    address = bundle.getParcelable("ad");

                    String locality = address.getLocality();
                    String adminArea = address.getAdminArea();
                    String country = address.getCountryName();
                    Double lat = address.getLatitude();
                    Double lng = address.getLongitude();
                    String locationData = locality + " " + adminArea + " " + country;
                    myAddress = new FeedItem.MyAddress(locationData,lat,lng);

                    locationField.setText(locationData);
                }

        }
        hideBtn();
    }

    public static Bitmap getBitmapFromIntentData(Intent data, Context context) throws IOException {
        /*
        Uri imageUri = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
        */
        Uri imageUri = data.getData();
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    }

    private void bindButton() {
        final Activity activity = getActivity();
        activity.findViewById(R.id.postReportBtn).setOnClickListener(this);
        activity.findViewById(R.id.btnUpload).setOnClickListener(this);
        activity.findViewById(R.id.btnMap).setOnClickListener(this);

    }

    private void hideBtn(){
        Button btn = (Button) getActivity().findViewById(R.id.btnMap);
        if(locationField.getText().toString() !=  "") {
            btn.setVisibility(View.INVISIBLE);
        }
    }


    private void sendReport() {
        if(validateForm()) {
            String msgTxt = msgTxtField.getText().toString();
            String name = nameField.getText().toString();
            String addressField = locationField.getText().toString();


          sendReport(msgTxt, name, address);
        }
    }

    private void sendReport(final String msgTxt, final String petName, final Address ad) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Constants.DB_USERS_NODE).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String name = user.getFirstName() + " " + user.getLastName();
                        String timeStamp = String.valueOf(System.currentTimeMillis());

                        // imagen
                        byte[] imageBytes = getSelectedImageBytes();
                        String imageUrl = null;
                        if (imageBytes != null) {
                            imageUrl = saveImageToFirebase(imageBytes);
                        }

                        FeedItem item = new FeedItem(name, msgTxt, petName, timeStamp, imageUrl, myAddress, false);
                        DatabaseReference newItem = dbRef.child(Constants.DB_FEED_NODE).push();
                        item.setId(newItem.getKey());

                        newItem.setValue(item);
                        Toast.makeText(getActivity(), "Su reporte ha sido enviado", Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private byte[] getSelectedImageBytes() {
        if(mSelectedImage.getDrawable() == null) {
            return null;
        }
        mSelectedImage.setDrawingCacheEnabled(true);
        mSelectedImage.buildDrawingCache();
        Bitmap bitmap = mSelectedImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private String saveImageToFirebase(byte[] imageBytes) {
        String imageUrl = UUID.nameUUIDFromBytes(imageBytes).toString();
        FirebaseStorage.getInstance()
                .getReference()
                .child(Constants.STORAGE_IMAGES)
                .child(imageUrl)
                .putBytes(imageBytes);
        return imageUrl;
    }

    private boolean validateForm() {
        return true;
    }

    private void sendRandomReport() {
        String randomMessage = new BigInteger(130, new SecureRandom()).toString(32);
        String randomName = new BigInteger(130, new SecureRandom()).toString(16);
        //sendReport(randomMessage, randomName);
        Toast.makeText(getActivity(), "Random report sent", Toast.LENGTH_SHORT).show();
    }

}
