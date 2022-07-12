package com.asuni.imagetopdf.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.aa.DocumentCamera;
import com.asuni.imagetopdf.adapters.DocumentCommon;
import com.asuni.imagetopdf.adapters.RecyclerViewAdapter;
import com.asuni.imagetopdf.databinding.ActivityImageToPdfBinding;
import com.asuni.imagetopdf.viewmodels.ImageToPdfVM2;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class ImageToPdf extends AppCompatActivity{

    ImageToPdfVM2 imageToPdfVM;
    public ActivityImageToPdfBinding activityImageToPdfBinding;

    public ArrayList<Bitmap> imageList = new ArrayList<>();

    private RecyclerViewAdapter imageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DocumentCommon.setPermition(this);

        activityImageToPdfBinding= DataBindingUtil.setContentView(this, R.layout.activity_image_to_pdf);
        imageToPdfVM=new ImageToPdfVM2(this);
        activityImageToPdfBinding.setViewModel(imageToPdfVM);


        DrawerLayout drawerLayout = findViewById( R.id.drawer );
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout , R.string.nav_open , R.string.nav_close );

        drawerLayout.addDrawerListener( actionBarDrawerToggle ) ;
        actionBarDrawerToggle.syncState() ;

        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar ) ;

        drawerLayout.close();

        findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });



        NavigationView navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.close();

                switch ( item.getItemId() ) {

                    case R.id.contact_us: {
                        startActivity(new Intent(getApplicationContext(), ContactUS.class));
                        return true;
                    }

                    case R.id.show_us_love: {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.asuni.imagetopdf")));
                        return true;
                    }


                    case R.id.privacy_policy: {
                        startActivity(new Intent(getApplicationContext(), AppPrivacyPolicy.class));
                        return true;
                    }


                }
                return true;
            }
        });



    }


    public void changeLayout( ){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    public void changeLayout1(){

        Intent myIntent=new Intent(this,  DocumentCamera.class);
        startActivityForResult(myIntent,2);

    }

    public void showAlert1( ){

        AlertDialog.Builder alertBuiler= new AlertDialog.Builder(ImageToPdf.this);
        alertBuiler.setTitle("Where my pdf save?");
        alertBuiler.setMessage("Internal storage --> IMAGE_TO_PDF folder");

        alertBuiler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog alertDialog = alertBuiler.create();
                alertDialog.cancel();
                onBackPressed();

            }
        });

        alertBuiler.show();

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageToPdfVM.onActivityResult1( requestCode , resultCode , data );

    }

    public void createUI( ){

        activityImageToPdfBinding.cardview.setVisibility(View.GONE);

        activityImageToPdfBinding.list.setVisibility(View.VISIBLE);

        imageRVAdapter = new RecyclerViewAdapter(this, imageList );
        GridLayoutManager manager = new GridLayoutManager( this , 4);
        activityImageToPdfBinding.list.setLayoutManager(manager);
        activityImageToPdfBinding.list.setAdapter(imageRVAdapter);

    }

    public void upDateUI( int position ){
        imageList.remove( position );
        imageRVAdapter.notifyDataSetChanged();
        imageToPdfVM.imageList.remove( position );

        if( imageList.isEmpty() ) {
            imageToPdfVM.snackbar.dismiss();
            activityImageToPdfBinding.cardview.setVisibility(View.VISIBLE);
            activityImageToPdfBinding.list.setVisibility(View.INVISIBLE);
        }

    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


}