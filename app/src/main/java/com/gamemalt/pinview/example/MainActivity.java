package com.gamemalt.pinview.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.gamemalt.pinview.PinView;
import com.gamemalt.pinview.PinViewListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PinView pinView=findViewById(R.id.pin_view);

        pinView.setListener(new PinViewListener() {
            @Override
            public void onPinButtonClick(int num) {
                Log.d("yoyo",""+num);
            }

            @Override
            public void onClearButtonClick() {
                Log.d("yoyo","onClearButtonClick");

            }

            @Override
            public void onClearButtonLongClick() {
                Log.d("yoyo","onClearButtonLongClick");

            }
        });
        
    }
}
