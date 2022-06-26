package com.asuni.imagetopdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;

import android.os.Bundle;

import android.widget.Toast;

import com.asuni.imagetopdf.databinding.ActivityImageToPdfBinding;


public class ImageToPdf extends AppCompatActivity{

    ImageToPdfVM imageToPdfVM;
    ActivityImageToPdfBinding activityImageToPdfBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityImageToPdfBinding= DataBindingUtil.setContentView(this,R.layout.activity_image_to_pdf);
        imageToPdfVM=new ImageToPdfVM(this);
        activityImageToPdfBinding.setViewModel(imageToPdfVM);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("b");
        imageToPdfVM.onActivityResult1(requestCode,resultCode,data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changeLayout( Class myclass){

        Intent myIntent=new Intent(this, myclass);
        setResult(RESULT_OK, myIntent);
        startActivityForResult(myIntent,1);

    }



    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

}