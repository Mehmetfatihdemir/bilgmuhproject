package com.example.btmp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    ImageView img;
    Bitmap bitmap;

    DatabaseReference databaseurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.iMageview);



        // Databasee yazma
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseurl = database.getReference("message");

        databaseurl.setValue("https://images.pexels.com/photos/4709480/pexels-photo-4709480.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");


        //Databaseten okuma
        databaseurl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                String URLIMAGE = value;
                new GetImageFromURL(img).execute(URLIMAGE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    
    //Databaseten gelen imageviewi bitmap'a çevirme
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap>{
        ImageView imgV;
        Bitmap bitmap;

        public GetImageFromURL(ImageView imgV){
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url [0];
            bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }
}