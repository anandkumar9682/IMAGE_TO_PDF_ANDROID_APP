package com.asuni.imagetopdf;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.view.MotionEvent;

import android.view.View;


import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.view.PreviewView;

import androidx.databinding.DataBindingUtil;

import com.asuni.imagetopdf.CommonUtility;
import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.databinding.ActDocumentCameraBindingImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DocumentCamera extends AppCompatActivity {


    public PreviewView previewView;

    boolean picture=false;


    ActDocumentCameraBindingImpl actMycameraBinding;
    DocumentCameraVM documentCameraVM;

    ArrayList<Bitmap> imageListBitmap=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actMycameraBinding= DataBindingUtil.setContentView(this, R.layout.act_document_camera);
        documentCameraVM=new DocumentCameraVM(this);
        actMycameraBinding.setViewModel(documentCameraVM);

        CommonUtility.setPermition(this);

        previewView = findViewById(R.id.previewView);
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View viewinner, MotionEvent motionEvent) {

                documentCameraVM.focus(motionEvent);

                return true;
            }
        });

    }



    public boolean flash(boolean flash){

        if(flash){
            actMycameraBinding.flashButton.setImageDrawable(getDrawable(R.drawable.icon_flashlight_black));
            flash=false;
        }else{
            actMycameraBinding.flashButton.setImageDrawable(getDrawable(R.drawable.icon_flashlight_green));
            flash=true;
        }

        return flash;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void camptureImage(){

        Bitmap bitmap = previewView.getBitmap();

        actMycameraBinding.imagePreview.setBackground( new BitmapDrawable(getResources(), bitmap));

        imageListBitmap.add(bitmap);

        actMycameraBinding.imageCount.setText(String.valueOf(imageListBitmap.size()));

        if(imageListBitmap.size()==1){
            picture=true;
            actMycameraBinding.confirm.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_done_outline_24));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void confirm(){

        if(picture)
            new CreateCameraTask(this).execute(true);
        else
            backToActivity();

    }

    public void preview(){


    }

    public class CreateCameraTask extends AsyncTask<Boolean , Integer, Integer> {

        DocumentCamera documentCamera;
        ProgressDialog progressDialog;

        public CreateCameraTask(DocumentCamera documentCamera){
            this.documentCamera=documentCamera;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog progressDialog= new ProgressDialog(documentCamera);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            this.progressDialog=progressDialog;

        }



        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Integer doInBackground(Boolean... b) {

            Set<String> set =new HashSet<>();

            for(int i=0 ; i< imageListBitmap.size() ; i++)
                set.add( DocumentCommon.convertBitmapToString(imageListBitmap.get(i)) );

            SharedPreferences sharedPreferences=getSharedPreferences("document_data", Context.MODE_PRIVATE);
            sharedPreferences.edit().putStringSet("dataImages",set).putBoolean("dataImagesFlag",true).commit();


            Intent intent = new Intent();
            intent.putExtra("imageList",new String[]{"a","a"} );
            setResult(RESULT_OK, intent);


            return 1;

        }


        @Override
        protected void onPostExecute(Integer file) {
            super.onPostExecute(file);

            progressDialog.cancel();
            backToActivity();

        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }



    public void backToActivity(){
        super.onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}