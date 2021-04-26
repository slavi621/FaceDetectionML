package com.example.facedetectionml;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.facedetectionml.Helper.GraphicOverlay;
import com.example.facedetectionml.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private Button faceDetectButton;
    private GraphicOverlay graphicOverlay;
    private CameraView cameraView;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void processTakenImage(int requestCode, Intent data, ImageView imageView) {
        Log.e(TAG, msg: "taken image is being processed");
        Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        startFaceDetection(photo, requestCode);
        imageView.setImageBitmap(photo);
    }

    private void processFacedetection(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

    }

    private void createCameraSource() {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .build();
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        CameraSource mCameraSource = new CameraSource(this, graphicOverlay);
        mCameraSource. setFacing(CameraSource.CAMERA_FACING_FRONT);

        faceDetectionProcessor = new FaceDetectionProcessor(detector);
        faceDetectionProcessor.setFaceDetectionResultListener(getFaceDetectionListener());

        mCameraSource.setMachineLearningFrameProcessor(faceDetectionProcessor);
        startCameraSource();

    }

}


