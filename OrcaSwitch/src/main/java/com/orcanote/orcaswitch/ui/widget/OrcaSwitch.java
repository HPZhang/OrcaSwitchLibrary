package com.orcanote.orcaswitch.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.orcanote.orcaswitch.R;

public class OrcaSwitch extends View {

    public static final int ANIMATOR_DURATION = 250;

    // dimension
    private float mThumbRadius;
    private float mTrackWidth;
    private float mTrackHeight;
    private float mRippleRadius;
    private float mWidth;
    private float mHeight;

    // offset
    private float mCenterX;
    private float mThumbX;
    private float mThumbCircleY;
    private float mTrackLeft;
    private float mTrackRight;
    private float mRippleCircleY;
    private float mActionMoveX;
    private float mActionMoveY;

    // paint
    private Paint mThumbOnPaint;
    private Paint mThumbOffPaint;
    private Paint mThumbDisabledPaint;
    private Paint mTrackOnPaint;
    private Paint mTrackOffPaint;
    private Paint mTrackDisabledPaint;
    private Paint mRipplePaint;
    private Paint mRippleOnPaint;
    private Paint mRippleOffPaint;
    private Paint mRippleDisabledPaint;

    // rect
    private RectF mTrackRect;

    // status
    private boolean mTouching;
    private boolean mDisabled;
    private boolean mChecked;
    private boolean mToggleCheckedChangeEvent;
    private int     mTouchSlop;
    private int     mTapTimeout;

    // listener
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public OrcaSwitch(Context context) {
        super(context);

        init(null);
    }

    public OrcaSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public OrcaSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Context    context   = getContext();
        Resources  resources = getResources();
        float      density   = resources.getDisplayMetrics().density;
        TypedArray ta        = context
            .getTheme()
            .obtainStyledAttributes(attrs, R.styleable.OrcaSwitch, 0, 0);

        // status
        mTouching = false;
        mDisabled = ta.getBoolean(R.styleable.OrcaSwitch_disabled, false);
        mChecked = ta.getBoolean(R.styleable.OrcaSwitch_checked, false);
        mToggleCheckedChangeEvent = true;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTapTimeout = ViewConfiguration.getTapTimeout();

        // size
        int size = ta.getInteger(R.styleable.OrcaSwitch_size, 48);
        if (size == 40) {
            mThumbRadius = 9 * density;
            mTrackWidth = 22 * density;
            mTrackHeight = 11 * density;
            mRippleRadius = 20 * density;
        } else if (size == 56) {
            mThumbRadius = 11 * density;
            mTrackWidth = 30 * density;
            mTrackHeight = 15 * density;
            mRippleRadius = 28 * density;
        } else {
            mThumbRadius = 10 * density;
            mTrackWidth = 26 * density;
            mTrackHeight = 13 * density;
            mRippleRadius = 24 * density;
        }
        mWidth = mTrackWidth + mRippleRadius * 2;
        mHeight = size * density;

        // offset
        mCenterX = mWidth / 2;
        mTrackLeft = mCenterX - mTrackWidth / 2;
        mTrackRight = mTrackLeft + mTrackWidth;
        mThumbCircleY = mHeight / 2;
        mRippleCircleY = mRippleRadius;
        if (mChecked) {
            mThumbX = mTrackRight;
        } else {
            mThumbX = mTrackLeft;
        }

        // paint
        mThumbOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbDisabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackDisabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRippleOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRippleOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRippleDisabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int thumbOnColor = ta
            .getColor(R.styleable.OrcaSwitch_thumbOnColor, Color.parseColor("#009688"));
        int thumbOffColor = ta
            .getColor(R.styleable.OrcaSwitch_thumbOffColor, Color.parseColor("#eaeaea"));
        int thumbDisabledColor = ta
            .getColor(R.styleable.OrcaSwitch_thumbDisabledColor, Color.parseColor("#bdbdbd"));
        int trackOnColor = ta
            .getColor(R.styleable.OrcaSwitch_trackOnColor, Color.parseColor("#80009688"));
        int trackOffColor = ta
            .getColor(R.styleable.OrcaSwitch_trackOffColor, Color.parseColor("#42000000"));
        int trackDisabledColor = ta
            .getColor(R.styleable.OrcaSwitch_trackDisabledColor, Color.parseColor("#1e000000"));
        int rippleColor = Color.parseColor("#00000000");
        int rippleOnColor = ta
            .getColor(R.styleable.OrcaSwitch_rippleOnColor, Color.parseColor("#33009688"));
        int rippleOffColor = ta
            .getColor(R.styleable.OrcaSwitch_rippleOffColor, Color.parseColor("#4beaeaea"));
        int rippleDisabledColor = ta
            .getColor(R.styleable.OrcaSwitch_rippleDisabledColor, Color.parseColor("#4bbdbdbd"));
        mThumbOnPaint.setColor(thumbOnColor);
        mThumbOffPaint.setColor(thumbOffColor);
        mThumbDisabledPaint.setColor(thumbDisabledColor);
        mTrackOnPaint.setColor(trackOnColor);
        mTrackOffPaint.setColor(trackOffColor);
        mTrackDisabledPaint.setColor(trackDisabledColor);
        mRipplePaint.setColor(rippleColor);
        mRippleOnPaint.setColor(rippleOnColor);
        mRippleOffPaint.setColor(rippleOffColor);
        mRippleDisabledPaint.setColor(rippleDisabledColor);

        // rect
        mTrackRect  = new RectF(
            mTrackLeft,
            mHeight / 2 - mTrackHeight / 2,
            mTrackRight,
            mHeight / 2 - mTrackHeight / 2 + mTrackHeight
        );

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTrack(canvas);
        drawRipple(canvas);
        drawThumb(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                disableParentTouchEvent();

                setPressed(true);

                mTouching = true;

                mActionMoveX = event.getX();
                mActionMoveY = event.getY();

                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:
                if (mDisabled) {
                    break;
                }

                move((event.getX() - mActionMoveX) * 0.6F);

                mActionMoveX = event.getX();

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                enableParentTouchEvent();

                setPressed(false);

                mTouching = false;

                invalidate();

                if (mDisabled) {
                    break;
                }

                if (isTapped(event)) {
                    performClick();
                } else {
                    if (mThumbX >= mTrackLeft && mThumbX < mCenterX) {
                        animate(-mTrackWidth);
                    } else {
                        animate(mTrackWidth);
                    }
                }

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        if (mChecked) {
            animate(-mTrackWidth);
        } else {
            animate(mTrackWidth);
        }

        return false;
    }

    public boolean isDisabled() {
        return mDisabled;
    }

    public void setDisabled(boolean disabled) {
        mDisabled = disabled;
        invalidate();
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked, boolean toggleCheckedChangeEvent) {
        mToggleCheckedChangeEvent = toggleCheckedChangeEvent;

        if (checked) {
            animate(mTrackWidth);
        } else {
            animate(-mTrackWidth);
        }
    }

    public void toggle(boolean toggleCheckedChangeEvent) {
        setChecked(!mChecked, toggleCheckedChangeEvent);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private void drawTrack(Canvas canvas) {
        if (mDisabled) {
            canvas.drawRoundRect(mTrackRect, mTrackHeight / 2, mTrackHeight / 2, mTrackDisabledPaint);
            return;
        }

        if (mThumbX < mCenterX) {
            canvas.drawRoundRect(mTrackRect, mTrackHeight / 2, mTrackHeight / 2, mTrackOffPaint);
        } else {
            canvas.drawRoundRect(mTrackRect, mTrackHeight / 2, mTrackHeight / 2, mTrackOnPaint);
        }
    }

    private void drawRipple(Canvas canvas) {
        if (mTouching) {
            if (mDisabled) {
                canvas.drawCircle(mThumbX, mRippleCircleY, mRippleRadius, mRippleDisabledPaint);
                return;
            }

            if (mThumbX < mCenterX) {
                canvas.drawCircle(mThumbX, mRippleCircleY, mRippleRadius, mRippleOffPaint);
            } else {
                canvas.drawCircle(mThumbX, mRippleCircleY, mRippleRadius, mRippleOnPaint);
            }
        } else {
            if (mDisabled) {
                canvas.drawCircle(mThumbX, mRippleCircleY, mRippleRadius, mRipplePaint);
                return;
            }
            canvas.drawCircle(mThumbX, mRippleCircleY, mRippleRadius, mRipplePaint);
        }
    }

    private void drawThumb(Canvas canvas) {
        if (mDisabled) {
            canvas.drawCircle(mThumbX, mThumbCircleY, mThumbRadius, mThumbDisabledPaint);
            return;
        }

        if (mThumbX < mCenterX) {
            canvas.drawCircle(mThumbX, mThumbCircleY, mThumbRadius, mThumbOffPaint);
        } else {
            canvas.drawCircle(mThumbX, mThumbCircleY, mThumbRadius, mThumbOnPaint);
        }
    }

    private boolean isTapped(MotionEvent event) {
        float distanceX = event.getX() - mActionMoveX;
        float distanceY = event.getY() - mActionMoveY;
        float duration  = event.getEventTime() - event.getDownTime();
        return distanceX <= mTouchSlop && distanceY <= mTouchSlop && duration <= mTapTimeout;
    }

    private void performChange() {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
        }
    }

    /**
     * disable touch event of parent view
     */
    private void disableParentTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private void enableParentTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    private void move(float distance) {
        mThumbX = mThumbX + distance;

        if (mThumbX < mTrackLeft) {
            mThumbX = mTrackLeft;
        } else if (mThumbX > mTrackRight) {
            mThumbX = mTrackRight;
        }

        invalidate();
    }

    private void animate(final float distance) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, distance);
        animator.setDuration(ANIMATOR_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                OrcaSwitch.this.move(distance * animation.getAnimatedFraction());

                if (animation.getAnimatedFraction() == 1) {
                    boolean checked = mChecked;

                    if (distance > 0) {
                        mChecked = true;
                    } else if (distance < 0) {
                        mChecked = false;
                    }

                    if (checked != mChecked) {
                        if (mToggleCheckedChangeEvent) {
                            performChange();
                        }
                        mToggleCheckedChangeEvent = true;
                    }
                }
            }
        });
        animator.start();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(OrcaSwitch orcaSwitch, boolean checked);
    }
}
