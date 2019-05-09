package com.pro.android.justyle;

import com.google.firebase.database.Exclude;

public class Upload{
private String mName;
private String mImageUrl;
private String mDescription;
private String mKey;



        /**
         * empty constructor needed
         */
public Upload(){

        }


Upload(String name, String imageUrl, String description) {

        if (name.trim().equals("")){
        name = "No name";
        }

        if (description.trim().equals("")){
                description = "No description";
        }

        mName = name;
        mImageUrl = imageUrl;
        mDescription = description;


        }
public String getName(){
        return mName;
        }

public void setName (String name){
        mName = name;
        }

String getImageUrl(){
        return mImageUrl;
        }

public void setImageUrl (String imageUrl){
        mImageUrl = imageUrl;
        }

        String getDescription(){
                return mDescription;
        }

        public void setDescription (String description){
                mDescription = description;
        }
@Exclude // we don't want this in firebase database because we already have a key
String getKey(){
        return mKey;
}
        @Exclude
        void setKey(String key){ mKey = key; }

}