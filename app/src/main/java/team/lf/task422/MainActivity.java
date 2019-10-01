package team.lf.task422;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {

    private CustomView mCustomView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomView = findViewById(R.id.progressbar);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mCustomView, "count", 0, 100);
        progressAnimator.setDuration(10000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        mCustomView.setOnClickListener(v->{
            progressAnimator.start();
        });

        findViewById(R.id.btn).setOnClickListener(v->{
            progressAnimator.start();
        });
    }
}
