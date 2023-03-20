package com.example.imagedownloadsave;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        /**
         * Image directory details
         * */

        String path = String.valueOf(getDir("imageDir", MODE_PRIVATE));
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
            Log.d("Files", "FileName:" + files[i]);
        }


        findViewById(R.id.preview).setOnClickListener(v -> {
            File path1 = getDir("imageDir", MODE_PRIVATE);
            String name = "DemoPicture.jpg";
            File myDir = new File(path1, name);
            Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        });
        findViewById(R.id.download).setOnClickListener(v -> loadBitmap("https://arlacompassblob.blob.core.windows.net/arlacompassblob/SKUImages/7517.png"));
    }


    private void loadBitmap(String url) {
        Target bitmapTarget = new BitmapTarget();
        protectedFromGarbageCollectorTargets.add(bitmapTarget);
        Picasso.get().load(url).into(bitmapTarget);
    }

    class BitmapTarget implements Target {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            try {
                //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File path = getDir("imageDir", MODE_PRIVATE);
                //File myDir = new File(path, "DemoPicture.jpg");

//                if (!myDir.exists()) {
//                    myDir.mkdirs();
//                }

                String name = "ovi2.jpg";
                File myDir = new File(path, name);
                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();
                out.close();
            } catch (Exception e) {
                Log.d("dfgdg", "onBitmapLoaded: " + e.getLocalizedMessage());
                // some action
            }
            //handle bitmap
            protectedFromGarbageCollectorTargets.remove(this);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable drawable) {
            protectedFromGarbageCollectorTargets.remove(this);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {

        }
    }
}