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

}