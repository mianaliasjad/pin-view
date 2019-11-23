package com.gamemalt.pinview;

public abstract class PinViewListener {

    public abstract void onPinButtonClick(int num,String currentPassword);

    public void onClearButtonClick(String currentPassword) {
    }

    public void onClearButtonLongClick() {
    }

    public void onOkButtonClick(String currentPassword) {
    }

}
