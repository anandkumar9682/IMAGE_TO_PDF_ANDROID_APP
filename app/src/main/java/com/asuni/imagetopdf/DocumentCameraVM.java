package com.asuni.imagetopdf;

import android.os.Build;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Size;
import android.view.MotionEvent;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class DocumentCameraVM extends BaseObservable {

    DocumentCamera myCamera;

    boolean flash=false;
    CameraSelector cameraSelector;
    Camera camera;
    boolean focus=true;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageView imageView;

    public DocumentCameraVM(DocumentCamera myCamera){
        this.myCamera=myCamera;

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        enableCamera();
    }

    public void focus(MotionEvent motionEvent ){

        if(focus){

            focus=false;
            int ab = motionEvent.getAction();

            MeteringPointFactory abc = myCamera.previewView.createMeteringPointFactory(cameraSelector);
            MeteringPoint point = abc.createPoint(motionEvent.getX(), motionEvent.getY());

            FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
            CameraControl cameraa = camera.getCameraControl();
            cameraa.startFocusAndMetering(action);

            if(flash){
                cameraa.enableTorch(true);
            }

            LinearLayout.LayoutParams rel_bottone = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rel_bottone.leftMargin= (int) motionEvent.getX()-30;
            rel_bottone.topMargin= (int) motionEvent.getY();
            rel_bottone.rightMargin=0;
            rel_bottone.bottomMargin=0;

            if(imageView!=null)
                myCamera.previewView.removeView(imageView);

            imageView=new ImageView(myCamera);

            imageView.setImageDrawable(myCamera.getDrawable(R.drawable.icon_focus_camera));
            myCamera.previewView.addView(imageView,rel_bottone);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myCamera.previewView.removeView(imageView);
                    cameraa.enableTorch(false);
                    focus=true;
                }
            },2000);

            CameraInfo abcd = camera.getCameraInfo();
        }
    }

    private void enableCamera(){

        cameraProviderFuture = ProcessCameraProvider.getInstance(myCamera);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider);

                } catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
            }
        }, ContextCompat.getMainExecutor(myCamera));
    }

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(myCamera), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                image.close();
            }
        });

        Preview preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(myCamera.previewView.createSurfaceProvider());
        camera = cameraProvider.bindToLifecycle((LifecycleOwner) myCamera, cameraSelector, imageAnalysis, preview);
    }

    public void flashBTN(){
        flash= myCamera.flash(flash);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void confirm(){

        myCamera.confirm();

    }

    public void imagePreview(){
        myCamera.preview();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void captureImage(){

        myCamera.camptureImage();

    }


}
