package com.example.dateui;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Detail_ReadActivity extends AppCompatActivity implements View.OnClickListener {
    Animation fb_open, fb_close;
    FloatingActionButton fab_main, fab_sub1, fab_sub2, fab_sub3;
    LinearLayout fabLayout_main, fabLayout_sub1, fabLayout_sub2, fabLayout_sub3;
    Boolean isFabOnOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__read);
        fb_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fb_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) findViewById(R.id.fab_sub2);
        fab_sub3 = (FloatingActionButton) findViewById(R.id.fab_sub3);
        fabLayout_main = (LinearLayout) findViewById(R.id.fabLayout_main);
        fabLayout_sub1 = (LinearLayout) findViewById(R.id.fabLayout_sub1);
        fabLayout_sub2 = (LinearLayout) findViewById(R.id.fabLayout_sub2);
        fabLayout_sub3 = (LinearLayout) findViewById(R.id.fabLayout_sub3);

        fab_main.setOnClickListener(this);
        fab_sub1.setOnClickListener(this);
        fab_sub2.setOnClickListener(this);
        fab_sub3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_main:
                if (!isFabOnOff) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;
        }
    }

    private void closeFABMenu() {
        isFabOnOff = false;
        fabLayout_sub1.setVisibility(View.GONE);
        fabLayout_sub2.setVisibility(View.GONE);
        fabLayout_sub3.setVisibility(View.GONE);
        fab_main.animate().rotationBy(180);
        fabLayout_sub1.animate().translationY(-55);
        fabLayout_sub2.animate().translationY(-100);
        fabLayout_sub3.animate().translationY(-145);
    }

    private void showFABMenu() {
        isFabOnOff = true;
        fab_main.animate().rotation(0);
        fabLayout_sub1.animate().translationY(0);
        fabLayout_sub2.animate().translationY(0);
        fabLayout_sub3.animate().translationY(0);
        fabLayout_sub3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isFabOnOff) {
                    fabLayout_sub1.setVisibility(View.VISIBLE);
                    fabLayout_sub2.setVisibility(View.VISIBLE);
                    fabLayout_sub3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
