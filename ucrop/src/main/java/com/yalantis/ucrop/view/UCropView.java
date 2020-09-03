package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

public class UCropView extends FrameLayout {

    private final GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;
    private OnChangeDataListener onChangeDataListener;
    private Matrix initMatrix;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.ucrop_view, this, true);
        mGestureCropImageView = (GestureCropImageView) findViewById(R.id.image_view_crop);
        mViewOverlay = (OverlayView) findViewById(R.id.view_overlay);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();


        mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                mViewOverlay.setTargetAspectRatio(cropRatio);
                //第一次加载完成拷贝矩阵，还原时使用
                if(initMatrix==null){
                    initMatrix = new Matrix();
                    initMatrix.set(mGestureCropImageView.getCurrentMatrix());
                }
            }
        });
        mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            @Override
            public void onCropRectUpdated(RectF cropRect) {
                mGestureCropImageView.setCropRect(cropRect);
                if(onChangeDataListener!=null){
                    onChangeDataListener.onChangeDataListener();
                }
            }
        });
        mGestureCropImageView.setOnChangeDataListener(new OnChangeDataListener() {
            @Override
            public void onChangeDataListener() {
                if(onChangeDataListener!=null){
                    onChangeDataListener.onChangeDataListener();
                }
            }
        });
    }

    public void resetAllChange(){
        mGestureCropImageView.setImageMatrix(initMatrix);
        mViewOverlay.setTargetAspectRatio(mGestureCropImageView.getTargetAspectRatio());
    }

    public void setOnChangeDataListener(OnChangeDataListener onChangeDataListener){
        this.onChangeDataListener = onChangeDataListener;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

    public interface OnChangeDataListener{
        public void onChangeDataListener();
    }
}