package com.asuni.imagetopdf.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asuni.imagetopdf.adapters.DocumentCommon;
import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.adapters.RecyclerViewAdapter;
import com.asuni.imagetopdf.databinding.ActivityImageToPdfBinding;
import com.asuni.imagetopdf.models.ImageAndNameModels;
import com.asuni.imagetopdf.viewmodels.ImageToPdfVM;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class ImageToPdf extends AppCompatActivity{

    ImageToPdfVM imageToPdfVM;
    public ActivityImageToPdfBinding activityImageToPdfBinding;

    private RecyclerViewAdapter imageRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DocumentCommon.setPermition(this);

        activityImageToPdfBinding= DataBindingUtil.setContentView(this, R.layout.activity_image_to_pdf);
        imageToPdfVM=new ImageToPdfVM(this);
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


                }
                return true;
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageToPdfVM.onActivityResult1(requestCode,resultCode,data);
    }

    public void createUI(ArrayList<Bitmap> imagePaths){

        activityImageToPdfBinding.list.setVisibility(View.VISIBLE);

        imageRVAdapter = new RecyclerViewAdapter(this, imagePaths);
        GridLayoutManager manager = new GridLayoutManager( this , 4);
        activityImageToPdfBinding.list.setLayoutManager(manager);
        activityImageToPdfBinding.list.setAdapter(imageRVAdapter);

    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


}