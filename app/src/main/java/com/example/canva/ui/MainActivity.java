package com.example.canva.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.canva.R;
import com.example.canva.databinding.ActivityBarcodeScanningBinding;
import com.example.canva.databinding.ActivityMainBinding;
import com.example.canva.ui.custom.DrawLine;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {


    RelativeLayout relativeLayout;
    MaterialButton materialButton;
    ActivityMainBinding binding;

    boolean isBarcode = false;


    private int cameraPermissionRequestCode = 1;
    private BarcodeScanningActivity.ScannerSDK selectedScanningSDK = BarcodeScanningActivity.ScannerSDK.MLKIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));


        binding.cardMlKit.setOnClickListener(view -> {
            isBarcode = true;
            selectedScanningSDK = BarcodeScanningActivity.ScannerSDK.MLKIT;
            startScanning();
        });

        binding.cardZxing.setOnClickListener(view -> {
            isBarcode = false;
            startScanning();
        });


        /*
        relativeLayout = findViewById(R.id.parent);
        RelativeLayout relativeLayout3 = findViewById(R.id.parentt);

        View viewLine = LayoutInflater.from(this).inflate(R.layout.line, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                50
        );
        params.setMargins(30, 0, 30, 0);

        // background
        viewLine.setBackground(getDrawable(R.drawable.ic_line));

        //animation
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
        viewLine.startAnimation(animation);

        //  relativeLayout.addView(view, params);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //line
                        relativeLayout.addView(viewLine, params);
                        ///button
                        addLayoutCircle(relativeLayout);
                    }
                }, 1000);
            }
        });



        *///
    }

    private void startScanning() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraWithScanner();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    cameraPermissionRequestCode
            );
        }
    }

    private void openCameraWithScanner() {
        if (isBarcode) {
            BarcodeScanningActivity.start(this, selectedScanningSDK);
        } else {
            startActivity(new Intent(this, PointActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == cameraPermissionRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
            )
            ) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, cameraPermissionRequestCode);
            }
        }
    }

    private void addLayoutCircle(RelativeLayout parentLayout) {
        int[] location = new int[2];
        parentLayout.getLocationOnScreen(location);
        Rect rectf = new Rect();

        //For coordinates location relative to the parent
        parentLayout.getLocalVisibleRect(rectf);

        //For coordinates location relative to the screen/display
        parentLayout.getGlobalVisibleRect(rectf);

        Log.d("*->WIDTH        :", String.valueOf(rectf.width()));
        Log.d("*->HEIGHT       :", String.valueOf(rectf.height()));
        Log.d("*->left         :", String.valueOf(rectf.left));
        Log.d("*->right        :", String.valueOf(rectf.right));
        Log.d("*->top          :", String.valueOf(rectf.top));
        Log.d("*->bottom       :", String.valueOf(rectf.bottom));

        /////
        for (int i = 1; i <= 6; i++) {
            int posX = random(rectf.left, rectf.right - 30);
            View viewLine = LayoutInflater.from(this).inflate(R.layout.circle_layout, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
            viewLine.setLayoutParams(params);

            viewLine.setX(posX);
            viewLine.setPivotX(posX + 50);
            //animation
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animate_zoom);
            viewLine.startAnimation(animation);
            parentLayout.addView(viewLine);
        }

    }

    private int random(int left, int right) {
        //Generate random int value from left to right
        System.out.println("Random value in int from " + left + " to " + right + ":");
        int random_int = (int) Math.floor(Math.random() * (right - left + 3) + left);
        System.out.println("Random value  " + random_int);
        return random_int;
    }

    private void test1(RelativeLayout parentLayout) {
        int[] location = new int[2];
        parentLayout.getLocationOnScreen(location);
        Rect rectf = new Rect();

//For coordinates location relative to the parent
        parentLayout.getLocalVisibleRect(rectf);

//For coordinates location relative to the screen/display
        parentLayout.getGlobalVisibleRect(rectf);

        Log.d("*->WIDTH        :", String.valueOf(rectf.width()));
        Log.d("*->HEIGHT       :", String.valueOf(rectf.height()));
        Log.d("*->left         :", String.valueOf(rectf.left));
        Log.d("*->right        :", String.valueOf(rectf.right));
        Log.d("*->top          :", String.valueOf(rectf.top));
        Log.d("*->bottom       :", String.valueOf(rectf.bottom));

        /////
        for (int i = 1; i <= 6; i++) {
            int posX = random(rectf.left, rectf.right - 30);
            DrawLine drawLine = new DrawLine(this, 30, 30);
            drawLine.setX(posX);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50 + (i * 10));
            drawLine.setLayoutParams(params);

            ///add animation
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.animate_zoom);
            drawLine.startAnimation(animation1);

            parentLayout.addView(drawLine);
        }
    }

    private void test(RelativeLayout parentLayout) {
        DrawLine drawLine = new DrawLine(this, parentLayout.getWidth() + 30, parentLayout.getHeight() + 30);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);

        drawLine.setLayoutParams(params);

        ///add animation
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.animate_zoom);
        drawLine.startAnimation(animation1);

        parentLayout.addView(drawLine);
    }

}