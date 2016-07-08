package com.example.jony.myapp.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Checkable;
import android.widget.ImageView;

import com.example.jony.myapp.R;

/**
 * Created by Jony on 2016/6/13.
 * 自定义的圆形 ImageView
 */
public class AvatarView extends ImageView implements Checkable {

    private boolean mChecked;

    public AvatarView(Context context) {
        this(context,null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();//状态改变之后，重绘该 View
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {

        setChecked(!mChecked);
    }

    /**
     * Set the image for this avatar. Will be used to create a round version of this avatar.
     *
     * @param resId The image's resource id.
     */
    @SuppressLint("NewApi")
    public void setAvatar(@DrawableRes int resId) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {

            //SDK版本在5.0以上。利用剪切，先将图片剪成圆
            setClipToOutline(true);
            setImageResource(resId);
        } else {
            setAvatarPreLollipop(resId);
        }
    }

    //5.0以前的版本，将 drawable 先转化成 roundedDrawable
    private void setAvatarPreLollipop(@DrawableRes int resId) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId,
                getContext().getTheme());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        @SuppressWarnings("ConstantConditions")
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(),
                bitmapDrawable.getBitmap());
        roundedDrawable.setCircular(true);
        setImageDrawable(roundedDrawable);
    }


    @Override
    @SuppressLint("NewApi")
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            return;
        }
        if (w > 0 && h > 0) {
            setOutlineProvider(new RoundOutlineProvider(Math.min(w, h)));
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Drawable border = ContextCompat.getDrawable(getContext(), R.drawable.selector_avatar);
            border.setBounds(0, 0, getWidth(), getHeight());
            border.draw(canvas);
        }
    }




    /**
     * Encapsulates checking api levels.
     */
    public static class ApiLevelHelper {

        private ApiLevelHelper() {

        }

        public static boolean isAtLeast(int apiLevel) {

            return Build.VERSION.SDK_INT >= apiLevel;
        }

        public static boolean isLowerThan(int apiLevel) {

            return Build.VERSION.SDK_INT < apiLevel;
        }

    }


    /**
     * Creates round outlines for views.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class RoundOutlineProvider extends ViewOutlineProvider {

        private final int mSize;

        public RoundOutlineProvider(int size) {
            if (0 > size) {
                throw new IllegalArgumentException("size needs to be > 0. Actually was " + size);
            }
            mSize = size;
        }

        @Override
        public final void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, mSize, mSize);
        }

    }
}
