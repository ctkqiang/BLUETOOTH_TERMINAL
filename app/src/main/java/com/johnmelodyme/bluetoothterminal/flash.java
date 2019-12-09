package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.
 * @DATETIME: 12/12/2019
 * @COPYRIGHT: 2019 - 2023
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TERMINAL
 */
public class flash extends AppCompatActivity {

    int flash;
    Handler handler;

    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        init();

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent TO_TERMINAL = new Intent(flash.this, Terminal.class);
                startActivity(TO_TERMINAL);
            }
        }, 3000);
    }

    private void init() {
        handler = new Handler();
        flash = 0;
    }
}
