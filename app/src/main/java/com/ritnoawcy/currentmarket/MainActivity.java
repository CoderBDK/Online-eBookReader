package com.ritnoawcy.currentmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //loadFlash();
       // initLayout();

        Main.init(this);
        Main.loadContentView(this);



    }



    public void initLayout() {
       new Thread(new Runnable() {
           int a =0;
           @Override
           public void run() {
               View v=getLayoutInflater().inflate(R.layout.activity_main,null);
               TextView tvFlash = findViewById(R.id.tvFlash);

               Handler handler=new Handler(getMainLooper());

               Runnable runnable=new Runnable() {
                   @Override
                   public void run() {
                       tvFlash.setText(""+ a);
                       handler.postDelayed(this,100);
                   }
               };
               handler.post(runnable);
               for (int i=0;i<10000000;i++){
                        a++;

                            for(int j=0;j<1000;j++){

                            }

               }

               handler.removeCallbacks(runnable);

               int b = a;
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      setContentView(v);
                  }
              });

           }
       }).start();
    }

    private void loadFlash() {
        setContentView(R.layout.flash_screen);
    }
}