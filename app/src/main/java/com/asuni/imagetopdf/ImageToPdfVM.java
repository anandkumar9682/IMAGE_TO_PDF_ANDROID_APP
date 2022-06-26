package com.asuni.imagetopdf;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.provider.MediaStore;

import android.util.Base64;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;

public class ImageToPdfVM {

    ImageToPdf imageToPdf;

    boolean previewFlag=true;
    boolean dataLoadFlag=true;
    Intent data;


    String fileNames[];

    ArrayList<byte[] > imageList=new ArrayList<>();
    ArrayList<Bitmap> bitmapImages=new ArrayList<>();
    SharedPreferences sharedPreferences;


    public ImageToPdfVM(ImageToPdf imageToPdf){
        this.imageToPdf=imageToPdf;
        imageToPdf.activityImageToPdfBinding.list.setAdapter(null);
        sharedPreferences = imageToPdf.getSharedPreferences("document_data", Context.MODE_PRIVATE);
    }

    public void openCamera(){


        imageToPdf.changeLayout(DocumentCamera.class);

    }

    public void openGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageToPdf.startActivityForResult(intent, 2);
    }

    protected void onActivityResult1(int requestCode, int resultCode, Intent data) {

        try{
            imageToPdf.activityImageToPdfBinding.list.setAdapter(null);
        }catch (Exception e){}


        if (requestCode == 1 ) {

            boolean flags = sharedPreferences.getBoolean("dataImagesFlag", false);

            if (flags)
                showConfirmPopup();

        }
        if (requestCode == 2 && resultCode == imageToPdf.RESULT_OK && null != data) {

            if (data.getClipData() != null) {
                this.data=data;
               showConfirmPopup();

            }
        }


    }

    public void showConfirmPopup(){

        Snackbar snackbar = Snackbar.make(imageToPdf.activityImageToPdfBinding.getRoot(),"",Snackbar.LENGTH_INDEFINITE);

        View customSnackView = imageToPdf.getLayoutInflater().inflate(R.layout.confirm_document_tool, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackbarLayout.setPadding(0, 0, 0, 0);

        customSnackView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                dataLoadFlag=true;
                previewFlag=false;

                new CreatePdfTask().execute(true);
            }
        });

        customSnackView.findViewById(R.id.reSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataLoadFlag=true;
                previewFlag=true;

                new CreatePdfTask().execute(true);
            }
        });

        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();
    }

    public class CreatePdfTask extends AsyncTask<Boolean , Integer, Integer> {

        ProgressDialog progressDialog;

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new ProgressDialog(imageToPdf);

            if(previewFlag){
                progressDialog.setTitle("Loading Content.");
                progressDialog.setMessage("Please Wait.");
            }else{
                progressDialog.setTitle("Generating Pdf File");
                progressDialog.setMessage("Please wait.");
            }

            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected Integer doInBackground(Boolean... b) {

            boolean flags=sharedPreferences.getBoolean("dataImagesFlag",false);

            if( flags ){

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("dataImagesFlag",false).apply();

                Set<String> set=sharedPreferences.getStringSet("dataImages",null);
                if(set!=null){
                    editor.putStringSet("dataImages",null).commit();

                    fileNames=new String[set.size()];

                    int i=0;
                    for( String s: set){

                        byte[] bytes=Base64.decode(s,Base64.DEFAULT);
                        fileNames[i]="Temp"+i; i++;
                        imageList.add(bytes);
                        bitmapImages.add(BitmapFactory.decodeByteArray(Base64.decode(s,Base64.DEFAULT), 0, Base64.decode(s,Base64.DEFAULT).length));
                    }

                }

                    dataLoadFlag=false;

            }else{

                if (dataLoadFlag){

                    fileNames=new String[data.getClipData().getItemCount()];

                    try {

                        System.out.println(data.getClipData().getItemCount()+"   count ");

                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {

                            Uri currentUri = data.getClipData().getItemAt(i).getUri();

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageToPdf.getContentResolver(), currentUri);
                            bitmapImages.add(bitmap);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imageList.add(stream.toByteArray());

                            fileNames[i]=DocumentCommon.getFileName(currentUri,imageToPdf);

                            System.out.println(imageToPdf.getContentResolver()+" ffff");

                        }

                        dataLoadFlag=false;

                    } catch (Exception e) { System.out.println("Exception is : " + e); }
                }


            }

           if(!previewFlag){

               Document document = new Document(PageSize.A4, 38.0f, 38.0f, 50.0f, 38.0f);
               try {
                   PdfWriter.getInstance( document , new FileOutputStream(  DocumentCommon.createNewFile() ) );
               } catch (Exception e){   }

               document.open();

               for(int i=0; i< imageList.size(); i++){

                   try {

                       Image image = Image.getInstance(imageList.get(i));

                       float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / image.getWidth()) * 100;
                       image.scalePercent(scaler);
                       image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                       image.setAbsolutePosition((document.getPageSize().getWidth() - image.getScaledWidth()) / 2.0f, (document.getPageSize().getHeight() - image.getScaledHeight()) / 2.0f);

                       boolean g = document.add(image);
                       boolean gg=document.addCreator(String.valueOf(i));
                       boolean ggg=document.newPage();

                       publishProgress(i);

                       System.out.println( "  " +g+gg+ggg );

                   } catch (Exception  e) { System.out.println("EXc : " + e); }
               }
               document.close();

           }


            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            this.progressDialog.setProgress(((values[0] + 1) * 100) /imageList.size());
            StringBuilder sb = new StringBuilder();
            sb.append("Processing images (");
            sb.append(values[0] + 1);
            sb.append("/");
            sb.append(imageList.size());
            sb.append(")");
            progressDialog.setTitle(sb.toString());

        }

        @Override
        protected void onPostExecute(Integer file) {
            super.onPostExecute(file);

            if(previewFlag){

                ImageAndNameModels[] imageAndNameModels = new ImageAndNameModels[bitmapImages.size()];

                for (int i = 0; i < bitmapImages.size()  ; i++)
                    imageAndNameModels[i] = new ImageAndNameModels(fileNames[i] , bitmapImages.get(i));

                DocumentListAdapter documentListAdapter =new DocumentListAdapter(imageToPdf.getApplicationContext(),R.layout.document_list_layout, imageAndNameModels );
                imageToPdf.activityImageToPdfBinding.list.setAdapter(documentListAdapter);


            }else
                imageToPdf.toastMessage("Convert Successful.");


            progressDialog.dismiss();

            previewFlag=true;
            dataLoadFlag=true;

            fileNames=null;

            imageList=new ArrayList<>();
            bitmapImages=new ArrayList<>();

        }
    }



    
}
