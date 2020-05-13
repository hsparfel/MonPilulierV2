package com.pouillos.monpilulier.activities.photo;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.add.AddAnalyseActivity;
import com.pouillos.monpilulier.entities.Contact;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class CameraActivity extends NavDrawerActivity implements SurfaceHolder.Callback {
    private Camera camera = null;
    private String typePhoto;
    private Long itemId;

    @BindView(R.id.fabTakePhoto)
    FloatingActionButton fabTakePhoto;
    @BindView(R.id.fabSavePhoto)
    FloatingActionButton fabSavePhoto;
    @BindView(R.id.fabCancelPhoto)
    FloatingActionButton fabCancelPhoto;
    @BindView(R.id.surface_view)
    SurfaceView surface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_camera);

        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

       // SurfaceView surface = (SurfaceView)findViewById(R.id.surface_view);

        SurfaceHolder holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // On déclare que la classe actuelle gérera les callbacks
        holder.addCallback(this);

        fabCancelPhoto.hide();
        fabSavePhoto.hide();
    }

    @OnClick(R.id.fabTakePhoto)
    public void fabTakePhotoClick() {
        //figer affichage
        //afficher boton ok ou refaire
        camera.takePicture(null, null,
                new PhotoHandler(getApplicationContext()));
        fabCancelPhoto.show();
        fabSavePhoto.show();
        fabTakePhoto.hide();
        /*if (validerPhoto(this)) {
            camera.takePicture(null, null,
                    new PhotoHandler(getApplicationContext()));
           // camera.startPreview();
            //ouvrirActiviteSuivante(this,AccueilActivity.class,"photoId",);
        } else {
            //recreate();
        }*/

        //recreate();
    }

    // Se déclenche quand la surface est créée
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);

            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Se déclenche quand la surface est détruite
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
    }

    // Se déclenche quand la surface change de dimensions ou de format
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }

    private boolean validerPhoto (Context context) {
        final boolean[] bool = {false};
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.dialog_save_title)
                .setMessage(R.string.dialog_save_message)
                .setNegativeButton(R.string.dialog_save_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.dialog_save_negative_toast, Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton(R.string.dialog_save_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.dialog_save_positive_toast, Toast.LENGTH_LONG).show();
                        bool[0] = true;

                    }
                })
                .show();
        //return bool[0];
        return true;

    }
}
