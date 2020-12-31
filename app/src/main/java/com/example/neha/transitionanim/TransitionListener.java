package com.example.neha.transitionanim;

import android.view.View;
import android.widget.ImageView;

public interface TransitionListener {
    void startPostponedEnterTransition(View rootView, ImageView imageView);
}