package com.example.canva.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.canva.R;
import com.example.canva.analyzer.MLKitBarcodeAnalyzer;
import com.example.canva.analyzer.ScanningResultListener;
import com.example.canva.databinding.ActivityMainBinding;
import com.example.canva.databinding.ActivityPointBinding;
import com.example.canva.ui.tools.Tools;
import com.google.common.util.concurrent.ListenableFuture;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PointActivity extends AppCompatActivity {

    ActivityPointBinding binding;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    /**
     * Blocking camera operations are performed using this executor
     */
    private ExecutorService cameraExecutor;
    private boolean flashEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPointBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        initView();
    }

    private void initView() {

        runOnUiThread(() -> new Handler().postDelayed(() -> {
            int[] location = new int[2];
            binding.parentRelative.getLocationOnScreen(location);
            Rect rectf = new Rect();

            //For coordinates location relative to the parent
            binding.parentRelative.getLocalVisibleRect(rectf);

            //For coordinates location relative to the screen/display
            binding.parentRelative.getGlobalVisibleRect(rectf);
            Tools.addDots(217, 217, 1743, 711, this, binding.getRoot());

            Tools.addLine(1743, 711, 217, 217, this, binding.getRoot());

        }, 1000));

        //
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception ignored) {
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void bindPreview(ProcessCameraProvider cameraProvider) {
        if (isDestroyed() || isFinishing()) {
            return;
        }

        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(binding.cameraPreview.getWidth(), binding.cameraPreview.getHeight()))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                // Monitors orientation values to determine the target rotation value
                int rotation;
                if (orientation >= 45 && orientation <= 134) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation <= 224) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation <= 314) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                imageAnalysis.setTargetRotation(rotation);
            }
        };
        orientationEventListener.enable();

        class ScanningListener implements ScanningResultListener {

            @Override
            public void onScanned(@NonNull String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageAnalysis.clearAnalyzer();
                        cameraProvider.unbindAll();
                    }
                });
            }
        }

        MLKitBarcodeAnalyzer analyzer = new MLKitBarcodeAnalyzer(new ScanningListener());
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer);
        preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());

        Camera camera =
                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);

        if (camera.getCameraInfo().hasFlashUnit()) {
            //binding.ivFlashControl.setVisibility(View.VISIBLE);

            binding.ivFlashControl.setOnClickListener(view -> {
                camera.getCameraControl().enableTorch(!flashEnabled);
            });

            camera.getCameraInfo().getTorchState().observe(this, integer -> {
                if (integer != null) {
                    if (integer == TorchState.ON) {
                        flashEnabled = true;
                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_on);
                    } else {
                        flashEnabled = false;
                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_off);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}