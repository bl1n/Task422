package team.lf.task422;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CustomView mCustomView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomView = findViewById(R.id.progressbar);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mCustomView, "count", 0, 100);
        progressAnimator.setDuration(5000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.GREEN, Color.RED);
        colorAnimator.setDuration(5000);
        colorAnimator.addUpdateListener(animation -> {
            mCustomView.setColorFilledLine((Integer) animation.getAnimatedValue());
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(progressAnimator, colorAnimator);
        View.OnClickListener onClickListener = v -> set.start();
        mCustomView.setOnClickListener(onClickListener);
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(onClickListener);
    }
}
