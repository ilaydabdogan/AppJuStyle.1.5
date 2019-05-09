package com.pro.android.justyle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";


    private TextView mTextViewNickname;
    private Button mButtonLogOut;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private EditText mEditName;
    private EditText mEditAddress;
    private Button mButtonAddInfo;
    private Button DeleteProfile;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTextViewNickname = (TextView) findViewById(R.id.Nickname);
        mButtonLogOut = (Button) findViewById(R.id.LogOut);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        DeleteProfile = (Button) findViewById(R.id.DeleteProfile);



        if (mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mEditName = (EditText) findViewById(R.id.EditName);
        mEditAddress = (EditText) findViewById(R.id.EditAddress);
        mButtonAddInfo =(Button) findViewById(R.id.addInfo);
        mFirebaseFirestore = FirebaseFirestore.getInstance();


            mTextViewNickname.setText(mCurrentUser.getEmail());

            DeleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("Deleting this account will result in completely removing of the account ");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Account deleted", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));

                                    } else {
                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    });

                     dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     });
                     AlertDialog alertDialog = dialog.create();
                     alertDialog.show();


                }
            });

            mButtonLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));

            }
        });
        mButtonAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

    }
    private void saveUserInformation(){
        Map<String, Object> user = new HashMap<>();

        String name = mEditName.getText().toString().trim();
        String address = mEditAddress.getText().toString().trim();
        user.put("name", name);
        user.put("address", address);

        mFirebaseFirestore.collection("users").document(mCurrentUser.getUid()).set(user);
    }
}
