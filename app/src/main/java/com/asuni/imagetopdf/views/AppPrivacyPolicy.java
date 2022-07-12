package com.asuni.imagetopdf.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.asuni.imagetopdf.R;
import com.asuni.imagetopdf.databinding.AppPrivacyPolicyBinding;
import com.asuni.imagetopdf.viewmodels.AppPrivacyPolicyVM;


public class AppPrivacyPolicy extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppPrivacyPolicyBinding actGrievanceBinding = DataBindingUtil.setContentView(this, R.layout.app_privacy_policy);
    actGrievanceBinding.setViewModel(new AppPrivacyPolicyVM(this));
  }
}
