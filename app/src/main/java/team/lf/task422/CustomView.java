package team.lf.task422;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CustomView extends View {

    public static final int STANDART_CIRCLE_COLOR = Color.BLACK;

    private Paint mMainTextPaint;


    private float mMainRadius;
    private float mSecondaryRadius;






    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mMainTextPaint = new Paint();
        mMainTextPaint.setStyle(Paint.Style.STROKE);
        mMainTextPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mMainTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.large_text));
        float width = mMainTextPaint.measureText("100");
        mMainTextPaint.setStrokeWidth(width/10);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMainRadius = mMainTextPaint.measureText("100");
        mSecondaryRadius = mMainRadius* 1.2f;

        int desireRadius = (int) (mMainRadius * 3f);
        int measuredWidth = resolveSize(desireRadius, widthMeasureSpec);
        int measuredHeight = resolveSize(desireRadius, heightMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        canvas.drawCircle(cx,cy,mSecondaryRadius,mMainTextPaint);

//        canvas.drawCircle(cx,cy,mMainRadius,mMainTextPaint);
    }

    private class Sector{
        private float mValue;
        private Paint mPaint;

        private float mAngle;

        public Sector(float value, int color) {
            mValue = value;
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
        }

        private float draw(Canvas canvas, RectF bounds, float startAngle){
            canvas.drawArc(bounds, startAngle, mAngle, true, mPaint);
            return startAngle + mAngle;
        }

        private void calculate(float valueSum) {
            mAngle = mValue / valueSum * 360f;
        }
    }
}
