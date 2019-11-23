package com.gamemalt.pinview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;

import java.util.Arrays;
import java.util.Collections;

public class PinView extends LinearLayout {


    private PinViewListener pinViewListener;
    private View mainLayout;


    private int pinButtonBackground;
    private int buttonClearBackground;
    private int buttonOkBackground;
    private int buttonTextColor;
    private int buttonTextSize;

    private int minButtonTextSize;
    private int maxButtonTextSize;

    private int autoSizeStepGranularity;

    private ImageButton buttonClear;
    private ImageButton buttonOk;

    private boolean showOkButton, showClearButton;

    private boolean isHapticFeedBack = false;

    private TextView[] pinButtons = new TextView[10];
    boolean isEnabled = true;
    private OnConfigurationChangedListener onConfigurationChangedListener;

    private String passWord = "";


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
            buttonClearBackground = typedArray.getResourceId(R.styleable.PinView_buttonClearBackground, R.drawable.v_back);
            buttonOkBackground = typedArray.getResourceId(R.styleable.PinView_buttonOkBackground, R.drawable.v_tick);
            pinButtonBackground = typedArray.getResourceId(R.styleable.PinView_pinButtonBackground, android.R.color.transparent);
            buttonTextColor = typedArray.getColor(R.styleable.PinView_buttonTextColor, Color.WHITE);

            buttonTextSize = typedArray.getInt(R.styleable.PinView_buttonTextSize, 24);

            minButtonTextSize = typedArray.getInt(R.styleable.PinView_minButtonTextSize, -1);
            maxButtonTextSize = typedArray.getInt(R.styleable.PinView_maxButtonTextSize, -1);

            autoSizeStepGranularity = typedArray.getInt(R.styleable.PinView_buttonAutoSizeStepGranularity, 1);

            isHapticFeedBack = typedArray.getBoolean(R.styleable.PinView_isHapticFeedBack, false);
            showOkButton = typedArray.getBoolean(R.styleable.PinView_showOkButton, true);
            showClearButton = typedArray.getBoolean(R.styleable.PinView_showClearButton, true);


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


                if (!isEnabled)
                    return true;

                resetPassword();
                if (isHapticFeedBackEnabled()) {
                    vibrate();
                }

                if (pinViewListener != null)
                    pinViewListener.onClearButtonLongClick();


                return true;

            }
        });

        buttonClear.setOnClickListener(clickListener);
        buttonOk.setOnClickListener(clickListener);

        setShowClearButton(showClearButton);
        setShowOkButton(showOkButton);

    }


    private void findAllViews() {

        buttonClear = findViewById(R.id.button_clear);
        buttonOk = findViewById(R.id.button_ok);

        setButtonClearBackground(buttonClearBackground);
        setButtonOkBackground(buttonOkBackground);

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
        checkAndSetButtonTextSize();
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

            if (!isEnabled)
                return;

            if (v.getId() == R.id.button_clear) {

                passWord = removeLast(passWord);

                if (pinViewListener != null)
                    pinViewListener.onClearButtonClick(passWord);

            } else if (v.getId() == R.id.button_ok) {

                if (pinViewListener != null)
                    pinViewListener.onOkButtonClick(passWord);
            } else {

                int num = Integer.parseInt(((TextView) v).getText().toString().trim());

                passWord += String.valueOf(num);

                if (pinViewListener != null)
                    pinViewListener.onPinButtonClick(num, passWord);

            }

            if (isHapticFeedBackEnabled() && v.getId() != R.id.button_ok) {
                //we don't vibrate on OK button
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
        buttonOk.setColorFilter(pinButtonTextColor, PorterDuff.Mode.SRC_IN);

        for (TextView button : pinButtons) {
            button.setTextColor(pinButtonTextColor);
        }
    }


    private void checkAndSetButtonTextSize() {
        if (minButtonTextSize < 0 || maxButtonTextSize < 0) {

            setPinButtonTextSize(buttonTextSize);
        } else {
            setPinButtonAutoTextSize(minButtonTextSize, maxButtonTextSize, autoSizeStepGranularity);
        }
    }


    /*
     * Sets button text
     * size in SP
     * */
    public void setPinButtonTextSize(int pinButtonTextSize) {
        buttonTextSize = pinButtonTextSize;

        for (TextView button : pinButtons) {
            button.setTextSize(pinButtonTextSize);
        }
    }

    /*
     * Sets auto text size automatically within the range of given min and max sizes
     * Size is in SP
     * */
    public void setPinButtonAutoTextSize(int minSize, int maxSize, int autoSizeStepGranularity) {
        this.minButtonTextSize = minSize;
        this.maxButtonTextSize = maxSize;
        this.autoSizeStepGranularity = autoSizeStepGranularity;

        for (TextView button : pinButtons) {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, minSize, maxSize, autoSizeStepGranularity, TypedValue.COMPLEX_UNIT_SP);
        }


    }


    /*
     * Provide resource ID
     * */
    public void setButtonClearBackground(int imageClearBackground) {
        this.buttonClearBackground = imageClearBackground;
        buttonClear.setImageResource(imageClearBackground);
    }

    /*
     * Provide resource ID
     * */
    public void setButtonOkBackground(int imageClearBackground) {
        this.buttonOkBackground = imageClearBackground;
        buttonOk.setImageResource(imageClearBackground);
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

    public void setShowOkButton(boolean shouldShow) {
        showOkButton = shouldShow;
        buttonOk.setVisibility(shouldShow ? VISIBLE : INVISIBLE);
    }

    public void setShowClearButton(boolean shouldShow) {
        showClearButton = shouldShow;
        buttonClear.setVisibility(shouldShow ? VISIBLE : INVISIBLE);
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (onConfigurationChangedListener != null)
            onConfigurationChangedListener.onConfigurationChanged(newConfig);


    }

    public void setOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.onConfigurationChangedListener = onConfigurationChangedListener;

    }

    public interface OnConfigurationChangedListener {
        void onConfigurationChanged(Configuration newConfig);
    }

    public void resetPassword() {
        passWord = "";
    }

    public String getPassword() {
        return passWord;
    }


    /*
     * Removes the last character of the string and return the new string
     * */
    public static String removeLast(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
