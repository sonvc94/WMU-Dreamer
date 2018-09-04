package top3haui.c.interviewjava.View;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wang.avi.AVLoadingIndicatorView;

import top3haui.c.interviewjava.R;

/**
 * Created by VanCuong on 27/2/2017.
 */

public class Splash extends AppCompatActivity {
    private AVLoadingIndicatorView avi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        String indicator = getIntent().getStringExtra("indicator");
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);

        int secondsDelayed = 1;
        showClick();
        new Handler().postDelayed(new Runnable() {
            public void run() {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        }, secondsDelayed * 2000);
//        hideClick();
    }
    public void hideClick() {
        avi.hide();
        // or avi.smoothToHide();
    }

    public void showClick() {
        avi.show();
        // or avi.smoothToShow();
    }
}
