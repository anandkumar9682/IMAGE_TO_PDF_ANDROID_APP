package com.asuni.imagetopdf;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class DocumentCommon {

    public static File createNewFile(){


        File storageDir = Environment.getExternalStoragePublicDirectory("Image to Pdf Converter/image to pdf");

        if (!storageDir.exists())
            new File(storageDir.getPath()).mkdirs();


        File file=null;
        try {
            file = File.createTempFile("Pdf_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" , ".pdf", storageDir);

        }catch (Exception e){  System.out.println("EXc : "+e); }

        return file;
    }


    @SuppressLint("Range")
    public static String getFileName(Uri uri, ImageToPdf imageToPdf) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = imageToPdf.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public static File saveImages( String folderName, Bitmap bitmap){
        String imageFileName = "Image" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory("Image to Pdf Converter/pdf to image/"+folderName);

        if (!storageDir.exists())
            new File(storageDir.getPath()).mkdirs();


        File image=null;
        try {
            image= File.createTempFile(imageFileName, ".jpeg", storageDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(image));
        } catch (Exception e1) {  }

        return image;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.getEncoder().encodeToString(byte_arr);
        return imageStr;
    }








}
