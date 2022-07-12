package com.asuni.imagetopdf.aa;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.os.Build;
import android.os.Bundle;


import android.view.MotionEvent;

import android.view.View;


import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.view.PreviewView;

import androidx.databinding.DataBindingUtil;

import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.adapters.DocumentCommon;
import com.asuni.imagetopdf.databinding.ActDocumentCameraBinding;

import java.util.HashSet;
import java.util.Set;


public class DocumentCamera extends AppCompatActivity {

    public PreviewView previewView;

    boolean picture=false;

    ActDocumentCameraBinding actMycameraBinding;
    DocumentCameraVM documentCameraVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actMycameraBinding= DataBindingUtil.setContentView(this, R.layout.act_document_camera);
        documentCameraVM=new DocumentCameraVM(this);
        actMycameraBinding.setViewModel(documentCameraVM);

        DocumentCommon.setPermition(this);

        previewView = findViewById(R.id.previewView);
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View viewinner, MotionEvent motionEvent) {

                documentCameraVM.focus(motionEvent);

                return true;
            }
        });

    }


    public boolean flash( boolean flash ){

        if(flash){
            actMycameraBinding.flashButton.setImageDrawable(getDrawable(R.drawable.ic_tourch_1));
            flash=false;
        }else{
            actMycameraBinding.flashButton.setImageDrawable(getDrawable(R.drawable.ic_tourch_2));
            flash=true;
        }

        return flash;
    }

    int size = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void camptureImage(){

        Bitmap bitmap = previewView.getBitmap();

        actMycameraBinding.imagePreview.setBackground( new BitmapDrawable(getResources(), bitmap));

        actMycameraBinding.imageCount.setText("total : "+ ++size);

        if( size == 1 ){
            picture=true;
            actMycameraBinding.confirm.setImageDrawable(getResources().getDrawable(R.drawable.ic_complete) );
        }

        AnotherThread anotherThread = new AnotherThread( bitmap );
        anotherThread.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void confirm(){

        System.out.println("aaaaaaaaaaaaaaaa");

        if( picture ) {

            getSharedPreferences("LOCAL_DATA",MODE_PRIVATE).edit().putStringSet("data", imageList ).commit();
            Intent intent = new Intent();
            setResult( RESULT_OK , intent );

        }


        onBackPressed();


    }

    Set<String> imageList = new HashSet<>();

    class AnotherThread extends Thread{

        Bitmap bitmap = null;

        public AnotherThread ( Bitmap bitmap ){
            this.bitmap = bitmap;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run(){

            imageList.add( DocumentCommon.convertBitmapToString( bitmap ) );

        }

    }






}