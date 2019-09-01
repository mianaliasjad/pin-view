package com.gamemalt.pinview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

public class PinView extends LinearLayout {


    private PinViewListener pinViewListener;
    private View mainLayout;


    private int pinButtonBackground;
    private int imageClearBackground;
    private int buttonTextColor;
    private int buttonTextSize;


    private ImageButton buttonClear;

    private boolean isHapticFeedBack = false;

    private TextView[] pinButtons = new TextView[10];
    boolean isEnabled = true;


    public PinView(Context context) {
        super(context);
        setDefaultAttr(null);
        initView();
    }


    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultAttr(attrs);
        initView();

    }

    public PinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaultAttr(attrs);
        initView();
    }

    private void setDefaultAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PinView);
        try {
            imageClearBackground = typedArray.getResourceId(R.styleable.PinView_buttonClearBackground, R.drawable.v_back);
            pinButtonBackground = typedArray.getResourceId(R.styleable.PinView_pinButtonBackground, android.R.color.transparent);
            buttonTextColor = typedArray.getColor(R.styleable.PinView_buttonTextColor, Color.WHITE);
            buttonTextSize = typedArray.getInt(R.styleable.PinView_buttonTextSize, 24);
            isHapticFeedBack = typedArray.getBoolean(R.styleable.PinView_isHapticFeedBack, false);

        } finally {
            typedArray.recycle();
        }

    }

    public void setListener(PinViewListener listener) {
        this.pinViewListener = listener;
    }


    private void initView() {

        mainLayout = inflate(getContext(), R.layout.pin_view, this);
        findAllViews();


        buttonClear.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (pinViewListener == null || !isEnabled)
                    return true;

                pinViewListener.onClearButtonLongClick();

                if (isHapticFeedBackEnabled()) {
                    vibrate();
                }

                return true;

            }
        });

        buttonClear.setOnClickListener(clickListener);

    }


    private void findAllViews() {

        buttonClear = findViewById(R.id.clear);
        setImageClearBackground(imageClearBackground);

        pinButtons[0] = findViewById(R.id.button_0);
        pinButtons[1] = findViewById(R.id.button_1);
        pinButtons[2] = findViewById(R.id.button_2);
        pinButtons[3] = findViewById(R.id.button_3);
        pinButtons[4] = findViewById(R.id.button_4);
        pinButtons[5] = findViewById(R.id.button_5);
        pinButtons[6] = findViewById(R.id.button_6);
        pinButtons[7] = findViewById(R.id.button_7);
        pinButtons[8] = findViewById(R.id.button_8);
        pinButtons[9] = findViewById(R.id.button_9);

        for (TextView button : pinButtons) {
            button.setOnClickListener(clickListener);

        }

        setPinButtonTextColor(buttonTextColor);
        setPinButtonTextSize(buttonTextSize);

    }


    public void shufflePinPad(boolean shouldShuffle) {
        Integer[] numArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        if (shouldShuffle) {
            Collections.shuffle(Arrays.asList(numArray));
        }

        for (int a = 0; a < pinButtons.length; a++) {
            pinButtons[a].setText(String.valueOf(numArray[a]));
        }


    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (pinViewListener == null || !isEnabled)
                return;

            if (v.getId() == R.id.clear) {

                pinViewListener.onClearButtonClick();

            } else {
                pinViewListener.onPinButtonClick(Integer.parseInt(((TextView) v).getText().toString().trim()));

            }

            if (isHapticFeedBackEnabled()) {
                vibrate();
            }

        }
    };

    private void vibrate() {
        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP,
                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                        | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
    }


    public void setPinButtonBackground(int pinButtonBackground) {
        this.pinButtonBackground = pinButtonBackground;
        for (TextView button : pinButtons) {
            button.setBackgroundResource(pinButtonBackground);
        }

    }

    public void setPinButtonTextColor(int pinButtonTextColor) {
        buttonTextColor = pinButtonTextColor;
//        ColorStateList ColorStateList = getResources().getColorStateList(pinButtonTextColor);

        buttonClear.setColorFilter(pinButtonTextColor, PorterDuff.Mode.SRC_IN);

        for (TextView button : pinButtons) {
            button.setTextColor(pinButtonTextColor);
        }
    }


    public void setPinButtonTextSize(int pinButtonTextSize) {
        buttonTextSize = pinButtonTextSize;

        for (TextView button : pinButtons) {
            button.setTextSize(pinButtonTextSize);
        }
    }


    /*
     * Provide resource ID
     * */
    public void setImageClearBackground(int imageClearBackground) {
        this.imageClearBackground = imageClearBackground;
        buttonClear.setImageResource(imageClearBackground);
    }

    public boolean isHapticFeedBackEnabled() {
        return isHapticFeedBack;
    }

    public void setHapticFeedBack(boolean isEnabled) {
        this.isHapticFeedBack = isEnabled;
    }

    public void setPinViewEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


}
