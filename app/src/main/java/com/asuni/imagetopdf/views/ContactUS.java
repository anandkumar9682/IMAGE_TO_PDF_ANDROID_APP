package com.asuni.imagetopdf.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.databinding.ActContactUsBinding;
import com.asuni.imagetopdf.viewmodels.ContactUsVM;

public class ContactUS extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActContactUsBinding actContactUsBinding = DataBindingUtil.setContentView( this , R.layout.act_contact_us);
    actContactUsBinding.setViewModel( new ContactUsVM(this) );
  }



}
