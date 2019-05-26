package com.bytebala.foodrecipes;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

// Declaring abstract disables the ability for this class to be instanciated as regular classes
// Only way is extending now
public abstract class BaseActivity extends AppCompatActivity {
  public ProgressBar mProgessBar;

  @Override
  public void setContentView(int layoutResID) {

    ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
    FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
    mProgessBar = constraintLayout.findViewById(R.id.progress_bar);

    //Associate FrameLayout with BaseActivity so FrameLayout acts as a container for any of activities that extends this class
    getLayoutInflater().inflate(layoutResID, frameLayout, true);
    super.setContentView(constraintLayout);
  }

  //You can call methods here in any activities that extends base activity
  public void showProgressBar(Boolean visible) {
    mProgessBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
  }
}
