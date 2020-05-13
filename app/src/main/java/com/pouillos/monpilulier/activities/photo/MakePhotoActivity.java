package com.pouillos.monpilulier.activities.photo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class MakePhotoActivity extends NavDrawerActivity {

    @BindView(R.id.fabTakePhoto)
    FloatingActionButton fabTakePhoto;
    @BindView(R.id.fabSavePhoto)
    FloatingActionButton fabSavePhoto;
    @BindView(R.id.fabCancelPhoto)
    FloatingActionButton fabCancelPhoto;
    @BindView(R.id.preview_layout)
    FrameLayout previewFL;

    @State
    Photo myPhoto;

    String type;
    Long itemId;

    final Camera camera = Camera.open();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_make_photo);

        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        fabSavePhoto.hide();
        fabCancelPhoto.hide();

        traiterIntent();

        // needs explicit permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

       // final Camera camera = Camera.open();
        CameraPreview cameraPreview = new CameraPreview(this, camera);

        // preview is required. But you can just cover it up in the layout.
        FrameLayout previewFL = findViewById(R.id.preview_layout);
        previewFL.addView(cameraPreview);
        camera.setDisplayOrientation(90);
        camera.startPreview();


    }

    @Override
    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type");
        }
        if (intent.hasExtra("itemId")) {
            itemId = intent.getLongExtra("itemId",0);
        }
    }

    @OnClick(R.id.fabCancelPhoto)
    public void fabCancelPhotoClick() {
        myPhoto.delete();
        recreate();
    }

    @OnClick(R.id.fabSavePhoto)
    public void fabSavePhotoClick() {
        Toast.makeText(MakePhotoActivity.this, "Photo Enregistrée",
                Toast.LENGTH_LONG).show();
        ouvrirActiviteSuivante(MakePhotoActivity.this, AccueilActivity.class);
    }

    @OnClick(R.id.fabTakePhoto)
    public void fabTakePhotoClick() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFileDir = getDir();
                if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                    Toast.makeText(MakePhotoActivity.this, "Can't create directory to save image.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                Date dateNew = new Date();
                String date = dateFormat.format(dateNew);
                String photoFile = "MonPilulierApp_Picture_" + date + ".jpg";
                String filename = pictureFileDir.getPath() + File.separator + photoFile;
                File pictureFile = new File(filename);
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPhoto = new Photo();
                myPhoto.setDate(dateNew);
                myPhoto.setPath(filename);
                myPhoto.setType(type);
                myPhoto.setItemId(itemId);
                myPhoto.setId(myPhoto.save());
                }
            });
        fabSavePhoto.show();
        fabCancelPhoto.show();
        fabTakePhoto.hide();
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "MonPilulierApp");
    }



}
