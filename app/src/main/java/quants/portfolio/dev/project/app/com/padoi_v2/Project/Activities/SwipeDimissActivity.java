package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDimissActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private ViewPager mContentView;
    private boolean isSwipeDown=true;//default behaviour of a animation of swipe to dismiss activity

    private ViewPager mViewPager;

    public void setMySwipeDownAnimationDismissView(ViewPager view){

        this.isSwipeDown=true;
        this.mContentView =view;
        this.mContentView.setOnTouchListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private int previousFingerPosition = 0;
    private int mContentViewPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;
    private float x1,x2;
    private int MIN_DISTANCE=150;

    public boolean onTouch(View view, MotionEvent event) {
        // Get finger position on screen
        final int Y = (int) event.getRawY();
        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.EDGE_LEFT:
                break;
            case MotionEvent.EDGE_RIGHT:
                break;
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                // save default base layout height
                defaultViewHeight = mContentView.getHeight();
                // Init finger and view position
                previousFingerPosition = Y;
                mContentViewPosition = (int) mContentView.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();

                float deltaX = x2 - x1;

                if(deltaX<0 && Math.abs(deltaX) > MIN_DISTANCE){
                    mContentView.arrowScroll(View.FOCUS_RIGHT);//going right
                }else if(deltaX>0 && Math.abs(deltaX) > MIN_DISTANCE){//swiped going left
                    mContentView.arrowScroll(View.FOCUS_LEFT);
                }else{

                }


                // If user was doing a scroll up
                if(isScrollingUp){
                    // Reset mContentView position
                    mContentView.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if(isScrollingDown){
                    // Reset mContentView position
                    mContentView.setY(0);
                    // Reset base layout size
                    mContentView.getLayoutParams().height = defaultViewHeight;
                    mContentView.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isClosing){
                    int currentYPosition = (int) mContentView.getY();

                    // If we scroll up
                    if(previousFingerPosition >Y){
                        // First time android rise an event for "up" move
                        if(!isScrollingUp){
                            isScrollingUp = true;
                        }

                        // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                        if(mContentView.getHeight()<defaultViewHeight){
                            mContentView.getLayoutParams().height = mContentView.getHeight() - (Y - previousFingerPosition);
                            mContentView.requestLayout();
                        }
                        else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((mContentViewPosition - currentYPosition) > defaultViewHeight / 4) {
                                closeUpAndDismissDialog(currentYPosition);
                                return true;
                            }

                            //
                        }
                        mContentView.setY(mContentView.getY() + (Y - previousFingerPosition));

                    }
                    // If we scroll down
                    else{

                        // First time android rise an event for "down" move
                        if(!isScrollingDown){
                            isScrollingDown = true;
                        }

                        // Has user scroll enough to "auto close" popup ?
                        if (Math.abs(mContentViewPosition - currentYPosition) > defaultViewHeight / 2)
                        {
                            closeDownAndDismissDialog(currentYPosition);
                            return true;
                        }

                        // Change base layout size and position (must change position because view anchor is top left corner)
                        mContentView.setY(mContentView.getY() + (Y - previousFingerPosition));
                        mContentView.getLayoutParams().height = mContentView.getHeight() - (Y - previousFingerPosition);
                        mContentView.requestLayout();
                    }

                    // Update position
                    previousFingerPosition = Y;
                }
                break;
                default:
                    break;
        }
        return true;
    }
    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(mContentView, "y", currentPosition, -mContentView.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition){
        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(mContentView, "y", currentPosition, screenHeight+mContentView.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        positionAnimator.start();
    }
}
