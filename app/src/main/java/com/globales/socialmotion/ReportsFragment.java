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
import android.widget.EditText;

import com.globales.socialmotion.models.FeedItem;
import com.globales.socialmotion.models.FirebaseHelper;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;


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

    EditText txtField;
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

        txtField = (EditText) view.findViewById(R.id.txtField);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindButton();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.postReportBtn) {
            postMessage();
        }
    }

    public void uploadPicture(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMG);
    }


    /*
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
    */

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
    }


    private void postMessage() {
        if(validateForm()) {
            String msgTxt = txtField.getText().toString();
            new FirebaseHelper(getActivity()).postMessage(msgTxt);
            txtField.setText("");
        }
    }

    /*
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
    */

    /*
    private String saveImageToFirebase(byte[] imageBytes) {
        String imageUrl = UUID.nameUUIDFromBytes(imageBytes).toString();
        FirebaseStorage.getInstance()
                .getReference()
                .child(Constants.STORAGE_IMAGES)
                .child(imageUrl)
                .putBytes(imageBytes);
        return imageUrl;
    }
    */

    private boolean validateForm() {
        return true;
    }

    /*
    private void sendRandomReport() {
        String randomMessage = new BigInteger(130, new SecureRandom()).toString(32);
        String randomName = new BigInteger(130, new SecureRandom()).toString(16);
        //postMessage(randomMessage, randomName);
        Toast.makeText(getActivity(), "Random report sent", Toast.LENGTH_SHORT).show();
    }
    */

}
