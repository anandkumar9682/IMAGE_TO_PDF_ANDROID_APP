package com.asuni.imagetopdf.views;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.adapters.DocumentCommon;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ImageToPdf.class));
            }
        });


        if (!checkPermission())
            showAlert();

    }




    private boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            System.out.println(" full acess ");

            return Environment.isExternalStorageManager();

        } else {

            System.out.println(" per grant false ceche ");

            int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
            int result2 = ContextCompat.checkSelfPermission(MainActivity.this, CAMERA);

            int granted = PackageManager.PERMISSION_GRANTED;

            return result == granted && result1 == granted && result2 == granted;

        }

    }


    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {

                System.out.println("full. req.");

                Intent intent = new Intent( Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION );
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 1);


            } catch (Exception e) {

                System.out.println( ".ful. .req. .exe." );

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult( intent , 1);

            }

            DocumentCommon.setPermition( this );

        } else {

            System.out.println( "short req." );

            DocumentCommon.setPermition( this );

        }

    }


    public void showAlert( ){

        AlertDialog.Builder alertBuiler= new AlertDialog.Builder(MainActivity.this);
        alertBuiler.setTitle("Permission is mandatory.");
        alertBuiler.setMessage("without Permission you can't access the app functionality.");

        alertBuiler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (!checkPermission())
                    requestPermission();

                AlertDialog alertDialog = alertBuiler.create();

                alertDialog.cancel();


            }
        });

        alertBuiler.show();

    }



}