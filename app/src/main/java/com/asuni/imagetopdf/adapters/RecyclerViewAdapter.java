package com.asuni.imagetopdf.adapters;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.views.ImageToPdf;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final ImageToPdf imageToPdf;
    private final ArrayList<Bitmap> imagePathArrayList;

    public RecyclerViewAdapter(ImageToPdf imageToPdf, ArrayList<Bitmap> imagePathArrayList) {

        this.imageToPdf = imageToPdf;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.imageIV.setBackground( new BitmapDrawable( imagePathArrayList.get(position) ));

        holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageToPdf.upDateUI( position );
            }
        });



    }

    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        private final ImageButton deleteBTN;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.image);
            deleteBTN = itemView.findViewById(R.id.deleteBTN);
        }

    }
}
