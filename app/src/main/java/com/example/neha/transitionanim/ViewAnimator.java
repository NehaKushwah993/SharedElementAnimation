package com.example.neha.transitionanim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.neha.transitionanim.sharedElementAnim.TransitionFragment;

//import android.transition.TransitionManager;

/**
 * Author - Neha Kushwah
 */
public class ViewAnimator {

    private View headerToDecreaseHeight;
    private ViewGroup containerForNewFragment;
    private View oldView;
    private ImageView animatingImageview;
    private int newViewX;
    private int newViewY;
    private int space = 243;
    private View newRootView;
    private ImageView newimageView;
    private Context context;
    private int oldViewX;
    private int oldViewY;
    private int oldViewWidth;
    private int oldViewHeight;
    private int newViewWidth;
    private int newViewHeight;
    private int difference;
    private FrameLayout containerForAnimationImageView;
    private FragmentManager fragmentManager;
    private TransitionFragment fragment;
    private ViewAnimatorListener viewAnimatorListener;
    private int heightSettling;

    private static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public ViewAnimator setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        return this;
    }

    public ViewAnimator setFragmentToAdd(TransitionFragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public ViewAnimator setContext(Context context) {
        this.context = context;
        return this;
    }

    public ViewAnimator setContainerForNewFragment(FrameLayout containerForNewFragment) {
        this.containerForNewFragment = containerForNewFragment;
        return this;
    }

    public ViewAnimator setContainerForNewFragment(RelativeLayout containerForFragment) {
        this.containerForNewFragment = containerForFragment;
        return this;
    }

    public ViewAnimator setViewToStartFrom(View viewToStartFrom) {
        this.oldView = viewToStartFrom;
        return this;
    }

    public void startAnimationFromWidthHeight(Context context, int width, int height) throws RuntimeException {

        if (containerForNewFragment instanceof LinearLayout) {
            throw new RuntimeException("containerForNewFragment should be frame layout or relative layout");
        }

        if (containerForAnimationImageView == null) {

            containerForAnimationImageView = new FrameLayout(context);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            containerForAnimationImageView.setLayoutParams(layoutParams);

            containerForNewFragment.addView(containerForAnimationImageView);
        }

        if (headerToDecreaseHeight != null) {
            headerToDecreaseHeight.measure(0, 0);
            heightSettling = headerToDecreaseHeight.getMeasuredHeight();
        }

        oldViewWidth = width;
        oldViewHeight = height;

        containerForAnimationImageView.removeAllViews();
        animatingImageview = new ImageView(context);
        this.containerForAnimationImageView.addView(animatingImageview);

        final int[] top3 = new int[2];
        containerForAnimationImageView.getLocationInWindow(top3);
        space = top3[1];


        final int[] top = new int[2];
        oldView.getLocationInWindow(top);
        this.oldViewX = top[0];
        this.oldViewY = top[1];


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) animatingImageview.getLayoutParams();
        params.leftMargin = oldViewX;
        params.topMargin = oldViewY - space;
        params.width = oldViewWidth;
        params.height = oldViewHeight;
        animatingImageview.setLayoutParams(params);

        initAnim(fragmentManager, fragment);

    }

    private void initAnim(FragmentManager fragmentManager, final TransitionFragment fragment) {

        fragment.setListener(new TransitionListener() {

            @Override
            public void startPostponedEnterTransition(final View rootView, final ImageView imageView) {

                if (rootView.getAlpha() != 0) {
                    Log.e(ViewAnimator.class.getName(), "Please make rootView to alpha 0 at onCreateView()");
                }

                containerForNewFragment.bringChildToFront(containerForAnimationImageView);
                rootView.setAlpha(0);

                imageView.post(new Runnable() {
                    @Override
                    public void run() {


                        if (viewAnimatorListener != null)
                            viewAnimatorListener.animationStarting();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageView.setTransitionName("sharedImage2");
                        }
                        int[] top2 = new int[2];
                        imageView.getLocationInWindow(top2);

                        newViewX = top2[0];
                        newViewY = top2[1];

                        ViewAnimator.this.newRootView = rootView;
                        ViewAnimator.this.newimageView = imageView;

                        newViewWidth = imageView.getWidth();
                        newViewHeight = imageView.getHeight();

                        rootView.setAlpha(0f);

                        int hDifference = Math.abs(newViewX - oldViewX);
                        int vDifference = Math.abs(newViewY - oldViewY);
//                            float fDifference = (hDifference + vDifference) / 2f;
                        float fDifference = Math.max(hDifference, vDifference) + 1f;//hDifference > vDifference ? hDifference : vDifference;

                        Log.d("Diffe1", (fDifference) + "");
                        Log.d("Diffe", (1 / fDifference) + "");

                        difference = (int) fDifference;//(int) (1 / fDifference * 10000) * 80;

                        if (difference <= 400) {
                            difference = 400;
                        }
                        if (difference >= 600) {
                            difference = 600;
                        }

                        startAnimationForHeader();

                        animateViewTo(difference, oldViewY, oldViewX, oldViewWidth, oldViewHeight, newViewY, newViewX, newViewWidth, newViewHeight);

                        imageView.setVisibility(View.INVISIBLE);
//                        rootView.animate().alpha(1).setStartDelay(difference-100).setDuration(100);
                        rootView.animate().alpha(1).setDuration(difference);
                        imageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rootView.setAlpha(1);
                                imageView.setVisibility(View.VISIBLE);
                                animatingImageview.setVisibility(View.INVISIBLE);
                                if (viewAnimatorListener != null)
                                    viewAnimatorListener.animationCompleted();

                            }
                        }, difference);

                    }
                });

            }


        });

        fragmentManager.beginTransaction().add(containerForNewFragment.getId(), fragment).addToBackStack("simpleFragmentB").commit();


    }

    private void startAnimationForHeader() {
        if (headerToDecreaseHeight == null)
            return;
        ValueAnimator anim = ValueAnimator.ofInt(heightSettling, 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = headerToDecreaseHeight.getLayoutParams();
                layoutParams.height = val;
                headerToDecreaseHeight.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(difference);
        anim.start();
    }

    public void removeViewWithAnimation(final RemoveListener removeListener) {
        if (viewAnimatorListener != null) viewAnimatorListener.viewRemoving();

        newRootView.animate().alpha(0f).setDuration(300).setStartDelay(0).start();

        startDecrAnimationForHeader();

        oldView.setAlpha(0);

        oldView.postDelayed(new Runnable() {
            @Override
            public void run() {
                oldView.setAlpha(1);
                containerForNewFragment.removeView(containerForAnimationImageView);
                if (viewAnimatorListener != null) viewAnimatorListener.viewRemoved();
                removeListener.viewRemoved();
            }
        }, difference);
        animateViewBack(difference, oldViewY, oldViewX, oldViewWidth, oldViewHeight, newViewY, newViewX, newViewWidth, newViewHeight);

    }

    private void startDecrAnimationForHeader() {
        if (headerToDecreaseHeight == null)
            return;
        ValueAnimator anim = ValueAnimator.ofInt(0, heightSettling);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = headerToDecreaseHeight.getLayoutParams();
                layoutParams.height = val;
                headerToDecreaseHeight.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(difference);
        anim.start();

    }

    private void animateViewTo(final int difference, final int top, final int left, int width, int height, final int newTop, final int newLeft, final int newWidth, final int newHeight) {

        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), loadBitmapFromView(newimageView, newWidth, newHeight));

        animatingImageview.setImageDrawable(drawable);
        animatingImageview.setVisibility(View.VISIBLE);

        oldView.setAlpha(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(containerForAnimationImageView, new TransitionSet()
                    .addTransition(new ChangeBounds().setDuration(difference)));
        }
        final FrameLayout.LayoutParams rlp = (FrameLayout.LayoutParams) animatingImageview.getLayoutParams();
        rlp.leftMargin = newLeft;
        rlp.topMargin = newTop - space;// - heightSettling;
        rlp.width = newWidth;
        rlp.height = newHeight;


        animatingImageview.setLayoutParams(rlp);


    }

    private void animateViewBack(final int difference, final int newTop, final int newLeft, final int newWidth, final int newHeight, int top, int left, int width, int height) {
        newimageView.setVisibility(View.INVISIBLE);
        animatingImageview.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) animatingImageview.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top - space + heightSettling;
        params.width = width;
        params.height = height;
        animatingImageview.setLayoutParams(params);

//        animatingImageview.setImageBitmap(loadBitmapFromView(newimageView, newWidth, newHeight));

        animatingImageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatingImageview.setVisibility(View.INVISIBLE);

            }
        }, difference);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(containerForAnimationImageView, new TransitionSet()
                    .setDuration(difference).addTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator())));
        }
        FrameLayout.LayoutParams rlp = (FrameLayout.LayoutParams) animatingImageview.getLayoutParams();
        rlp.leftMargin = newLeft;
        rlp.topMargin = newTop - space;
        rlp.width = newWidth;
        rlp.height = newHeight;
        animatingImageview.setLayoutParams(rlp);


    }

    public void setViewAnimatorListener(ViewAnimatorListener viewAnimatorListener) {
        this.viewAnimatorListener = viewAnimatorListener;
    }

    public void decreaseMarginForNewViewSettling(int height) {
        this.heightSettling = height;
    }

    public void setViewToBeHidden(View headerToDecreaseHeight) {
        this.headerToDecreaseHeight = headerToDecreaseHeight;
    }

    public interface RemoveListener {
        void viewRemoved();
    }

    public interface ViewAnimatorListener {

        void animationStarting();

        void animationCompleted();

        void viewRemoving();

        void viewRemoved();
    }
}