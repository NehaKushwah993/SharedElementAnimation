package com.example.neha.transitionanim.sharedElementAnim;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.neha.transitionanim.TransitionListener;
import com.example.neha.transitionanim.ViewAnimator;

public abstract class TransitionFragment extends Fragment{
    ViewAnimator viewAnimator;
    private FragmentManager currentFragmentManager;

    public abstract void setListener(TransitionListener listener);

    public void addFragmentWithAnimation(Context context, FragmentManager fragmentManager, final View view, View containerView) {

        if( !(containerView instanceof RelativeLayout) && !(containerView instanceof FrameLayout)){
            throw new RuntimeException("container should be RelativeLayout or FrameLayout");
        }
        this.currentFragmentManager = fragmentManager;
        viewAnimator = new ViewAnimator();
        try {

//            viewAnimator.setViewToBeHidden(findViewById(R.id.toolbar));

            viewAnimator.setViewAnimatorListener(new ViewAnimator.ViewAnimatorListener() {

                @Override
                public void animationStarting() {

                }

                @Override
                public void animationCompleted() {

                }

                @Override
                public void viewRemoving() {

                }

                @Override
                public void viewRemoved() {

                }
            });

            if(containerView instanceof RelativeLayout){
                viewAnimator.setContainerForNewFragment((RelativeLayout) containerView);
            }else if(containerView instanceof FrameLayout){
                viewAnimator.setContainerForNewFragment((FrameLayout) containerView);
            }
            viewAnimator.setContext(context);
            viewAnimator.setFragmentToAdd(this);
            viewAnimator.setViewToStartFrom(view);
            viewAnimator.setFragmentManager(fragmentManager);
            viewAnimator.startAnimationFromWidthHeight(context, view.getWidth(), view.getHeight());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeMe(final ViewAnimator.RemoveListener listener){

        viewAnimator.removeViewWithAnimation(new ViewAnimator.RemoveListener() {
            @Override
            public void viewRemoved() {
                currentFragmentManager.popBackStack();
                if(listener!=null) listener.viewRemoved();

            }
        });

    }


}
