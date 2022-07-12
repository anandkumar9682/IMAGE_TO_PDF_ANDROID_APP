package com.asuni.imagetopdf.viewmodels;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.provider.MediaStore;

import android.util.Base64;
import android.view.View;


import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.aa.DocumentCamera;
import com.asuni.imagetopdf.adapters.DocumentCommon;
import com.asuni.imagetopdf.views.ImageToPdf;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class ImageToPdfVM2 {

    ImageToPdf imageToPdf;

    public Snackbar snackbar;

    public ImageToPdfVM2(ImageToPdf imageToPdf){

        this.imageToPdf=imageToPdf;

        snackbar = Snackbar.make(imageToPdf.activityImageToPdfBinding.getRoot(),"",Snackbar.LENGTH_INDEFINITE);

        imageToPdf.activityImageToPdfBinding.list.setAdapter(null);

    }


    public void openGallery(){

        if( snackbar!=null )
            snackbar.dismiss();

        imageToPdf.changeLayout();

    }

    public void openCamera(){

        imageToPdf.changeLayout1();

    }



    Intent data;

    public ArrayList<byte[]> imageList = new ArrayList<>();

    Uri uri;

    public void onActivityResult1(  int req , int res , Intent data ) {

        if( req == 1 ){

            try{
                imageToPdf.activityImageToPdfBinding.list.setAdapter(null);
            }catch (Exception e){}

            if (data.getClipData() != null) {
                this.data=data;
                new LoadPdfTask().execute(true);
            }


        }else if( req == 2 ){

            Set<String> list = imageToPdf.getSharedPreferences("LOCAL_DATA",MODE_PRIVATE).getStringSet("data",null);

            for( String s : list ){

                byte[] encodeByte = Base64.decode( s , Base64.DEFAULT);
                InputStream inputStream = new ByteArrayInputStream(encodeByte);

                Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);

                imageToPdf.imageList.add( bitmap );

                imageList.add( encodeByte );

            }

            imageToPdf.createUI();
            showConfirmPopup();

        }

    }



    public class LoadPdfTask extends AsyncTask<Boolean , Integer, Integer> {

        ProgressDialog progressDialog;

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(imageToPdf);
            progressDialog.setTitle("Image loading progress");
            progressDialog.setMessage("Please wait.");
            progressDialog.show();


        }

        @Override
        protected Integer doInBackground(Boolean... b) {


            if( b[0] ){


                try {

                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri currentUri = data.getClipData().getItemAt(i).getUri();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageToPdf.getContentResolver(), currentUri);
                        imageToPdf.imageList.add(bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        imageList.add(stream.toByteArray());

                    }
                } catch (Exception e) { System.out.println("Exception is : " + e); }

            }else{


                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageToPdf.getContentResolver(), uri);
                    imageToPdf.imageList.add(bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageList.add(stream.toByteArray());


                    publishProgress(0);

                } catch (Exception e) { System.out.println("Exception is : " + e); }



            }


            return 1;
        }


        @Override
        protected void onPostExecute(Integer file) {
            super.onPostExecute(file);
            progressDialog.dismiss();

            imageToPdf.createUI(  );

            showConfirmPopup();

        }


    }


    public void showConfirmPopup(){

        View customSnackView = imageToPdf.getLayoutInflater().inflate(R.layout.confirm_document_tool, null);

        View view = snackbar.getView();
        view.setBackgroundColor(Color.TRANSPARENT);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackbarLayout.setPadding(0, 0, 0, 100);

        customSnackView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                new CreatePdfTask().execute(true);
            }
        });

        customSnackView.findViewById(R.id.reselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageToPdf.onBackPressed();
            }
        });

        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();

    }

    public class CreatePdfTask extends AsyncTask<Boolean , Integer, Boolean > {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(imageToPdf);
            progressDialog.setTitle("Image To PDF Conversion.");
            progressDialog.setMessage("Please wait.");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Boolean... b) {

            boolean success=false;

            Document document = new Document(PageSize.A4, 10, 10, 10, 10);

            try {
                PdfWriter.getInstance( document , new FileOutputStream(  DocumentCommon.createNewFile() ) );
            } catch (Exception e){   }

            document.open();

            for(int i=0; i< imageList.size(); i++){

                try {
                    publishProgress(i);

                    Image image = Image.getInstance(imageList.get(i));

                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / image.getWidth()) * 100;
                    image.scalePercent(scaler);
                    image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                    image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2.0f, (document.getPageSize().getHeight() - image.getScaledHeight()) / 2.0f);

                    boolean g = document.add(image);
                    boolean gg=document.addCreator(String.valueOf(i));
                    boolean ggg=document.newPage();

                } catch (Exception  e) {
                    success=false;
                }

            }

            document.close();
            success=true;

            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            this.progressDialog.setProgress(  ((values[0] + 1) * 100) /  imageList.size() );

            StringBuilder sb = new StringBuilder();
            sb.append( "Processing images (" );
            sb.append( values[0] + 1   );
            sb.append(  "/"  );
            sb.append(  imageList.size()  );
            sb.append(  ")"   );

            progressDialog.setTitle(sb.toString());

        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            progressDialog.dismiss();

            if(b)
                imageToPdf.toastMessage("pdf creation successfully");
            else
                imageToPdf.toastMessage("pdf creation Failed.");

            imageToPdf.showAlert1();


        }
    }

}
