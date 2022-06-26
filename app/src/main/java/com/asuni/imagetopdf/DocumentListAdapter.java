package com.asuni.imagetopdf;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DocumentListAdapter extends BaseAdapter implements ListAdapter {

    View[] myViews;

    Context context;
    int layout;

    ImageAndNameModels imageAndNameModels[];

    public DocumentListAdapter(Context context, int layout , ImageAndNameModels imageAndNameModels[] ) {

        this.imageAndNameModels=imageAndNameModels;
        this.layout=layout;
        this.myViews = new View[ imageAndNameModels.length ];
        this.context=context;
    }

    @Override
    public int getCount() {
        return imageAndNameModels.length;
    }

    @Override
    public Object getItem(int pos) {
        return imageAndNameModels[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.document_list_layout, null);
        }


        ((ImageView) view.findViewById(R.id.image) ).setImageBitmap(imageAndNameModels[position].getBitmap());

        ((TextView) view.findViewById(R.id.name) ).setText(imageAndNameModels[position].getName());

//        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i < myViews.length; i++)
//                    if (myViews[i].findViewById(R.id.btn) == v){
//
//                    }
//
//            }
//        });

        myViews[position] = view;

        return view;
    }
}