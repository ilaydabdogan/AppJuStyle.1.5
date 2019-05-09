package com.pro.android.justyle;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.ContentResolver;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class WardrobeFragment extends Fragment implements View.OnClickListener {

        private static final String TAG = "WardrobeFragment";
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int TAKE_PIC_REQUEST = 1;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private TextView mTextViewNickname;
    private FirebaseFirestore mFirebaseFirestore;
    private DocumentReference mDocumentReference;
    private Button uploadButton, chooseButton, takePicButton;
    static ImageView mImageView;
     static Uri mImageUri;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_wardrobe, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mTextViewNickname =  v.findViewById(R.id.Nickname);
        uploadButton =  v.findViewById(R.id.uploadButtonId);
        chooseButton =  v.findViewById(R.id.ChooseButtonId);
        takePicButton =  v.findViewById(R.id.takePicId) ;

        mImageView = v.findViewById(R.id.imageViewId);

        chooseButton.setOnClickListener(this);

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(intent, TAKE_PIC_REQUEST);


            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                Intent intent = new Intent(getActivity(), CreateArticleActivity.class);
                startActivity(intent);

            }
        });


        mFirebaseFirestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG, documentSnapshot.getId()+ " => " +documentSnapshot.getData());
                    }
                }else{
                    Log.w(TAG, "Error getting document", task.getException());
                }
            }
        });


        String userID = getString(getId());
        mDocumentReference = mFirebaseFirestore.collection("users").document(userID);

        mTextViewNickname.setText(user.getEmail());


        return v;

    }
    private void showFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select an image"), PICK_IMAGE_REQUEST);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            //imageView.setImageURI(mImageUri);
            Picasso.get()
                    .load(mImageUri)
                    .resize(600,600)
                    .centerCrop()
                    .into(mImageView);

        }
        //if (requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

      //  }
        else {
            Picasso.get()
                    .load(CameraActivity.pathToFile)
                    .into(mImageView);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == chooseButton ) {

            showFileChoose();
            //open camera roll
        }

    }

}
