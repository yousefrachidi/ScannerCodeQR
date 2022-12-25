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
import com.google.common.util.concurrent.ListenableFuture;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        addDots();

        //
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void addDots() {
        int[] location = new int[2];
        binding.parentRelative.getLocationOnScreen(location);
        Rect rectf = new Rect();

//For coordinates location relative to the parent
        binding.parentRelative.getLocalVisibleRect(rectf);

//For coordinates location relative to the screen/display
        binding.parentRelative.getGlobalVisibleRect(rectf);

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
            binding.parentRelative.addView(viewLine);
        }
    }

    private int random(int left, int right) {
        //Generate random int value from left to right
        System.out.println("Random value in int from " + left + " to " + right + ":");
        int random_int = (int) Math.floor(Math.random() * (right - left + 3) + left);
        System.out.println("Random value  " + random_int);
        return random_int;
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
                int rotation = Surface.ROTATION_180;
//                int rotation  = when (orientation) {
//                    in 45..134 -> Surface.ROTATION_270
//                    in 135..224 -> Surface.ROTATION_180
//                    in 225..314 -> Surface.ROTATION_90
//                    else -> Surface.ROTATION_0
//                }

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

        if (camera.getCameraInfo().hasFlashUnit() == true) {
            binding.ivFlashControl.setVisibility(View.VISIBLE);

            binding.ivFlashControl.setOnClickListener(view -> {
                camera.getCameraControl().enableTorch(!flashEnabled);
            });

            camera.getCameraInfo().getTorchState().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        if (integer == TorchState.ON) {
                            flashEnabled = true;
                            binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_on);
                        } else {
                            flashEnabled = false;
                            binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_off);
                        }
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