package com.example.neha.transitionanim.detailFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.neha.transitionanim.R;
import com.example.neha.transitionanim.sharedElementAnim.TransitionFragment;
import com.example.neha.transitionanim.TransitionListener;

public class WallDetailFragment extends TransitionFragment {

    private TransitionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wall_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView imageView = view.findViewById(R.id.image);

        //Call this listener to start animation
        listener.startPostponedEnterTransition(getView(), imageView);
    }

    @Override
    public void setListener(TransitionListener listener) {
        this.listener = listener;
    }

}