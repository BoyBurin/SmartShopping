package com.example.seniorproject.smartshopping.view.customviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.view.state.BundleSavedState;
import com.example.seniorproject.smartshopping.view.transformation.CircleTransform;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ItemView extends BaseCustomViewGroup {

    TextView tvName;
    ImageView imgItem;

    ImageView ivRoundGreen;
    ImageView ivRoundYellow;
    ImageView ivRoundRed;

    StorageReference storageReference;

    public ItemView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.item_view, this);
    }

    private void initInstances() {

        tvName = (TextView) findViewById(R.id.tvName);
        imgItem = (ImageView) findViewById(R.id.imgItem);

        ivRoundGreen = (ImageView) findViewById(R.id.ivRoundGreen);
        ivRoundYellow = (ImageView) findViewById(R.id.ivRoundYellow);
        ivRoundRed = (ImageView) findViewById(R.id.ivRoundRed);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec); // width is pixel
        height = height / 2;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height,
                MeasureSpec.EXACTLY
        );
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);

        setMeasuredDimension(width, height);

    } */




    public void setNameText(String text) {
        this.tvName.setText(text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec); // width is pixel
        int height = width;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height,
                MeasureSpec.EXACTLY
        );
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);

        setMeasuredDimension(width, height);

    }


    public void setImageUrl(String url) {
        storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.loading) //default pic
                .centerCrop()
                //.error(Drawable pic)  picture has problem
                .transform(new CircleTransform(getContext())) //Cool !!!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgItem);

    }

    public void setRemainder(long soft, long hard, long amount){
        ivRoundGreen.setVisibility(GONE);
        ivRoundYellow.setVisibility(GONE);
        ivRoundRed.setVisibility(GONE);
        if(amount > soft){
            ivRoundGreen.setVisibility(VISIBLE);
        } else if(amount < hard){
            ivRoundRed.setVisibility(VISIBLE);
        } else{
            ivRoundYellow.setVisibility(VISIBLE);
        }
    }
}
