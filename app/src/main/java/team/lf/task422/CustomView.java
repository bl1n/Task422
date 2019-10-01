package team.lf.task422;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private Paint mMainRadiusPaint;
    private Paint mMainTextPaint;
    private Paint mSecondaryTextPaint;
    private Paint mWhiteArcPaint;
    private Paint mFilledRadiusPaint;


    private float mMainRadius;
    //strokes
    private float mInnerLongStrokesRadius;
    private float mOuterLongStrokesRadius;
    private float mInnerShortStrokesRadius;
    private float mOuterShotStrokesRadius;


    private RectF mStandardBounds;
    private RectF mTotalBounds;
    private RectF mInnerOval;

    private Rect mMainTextBounds;

    private int mColorMain;
    private int mFilledLineColor;
    private int mColorBackground;

    private List<Stroke> mLongStrokes;
    private List<Stroke> mShortStrokes;

    private float mSweepAngle;
    private int mCount;

    public void setCount(int count) {
        mCount = count;
        mSweepAngle = (270f * mCount) / 100;
        invalidate();
    }

    public void setColorFilledLine(int filledLineColor) {
        mFilledLineColor = filledLineColor;
        mFilledRadiusPaint.setColor(mFilledLineColor);
        invalidate();
    }

    public int getFilledLineColor() {
        return mFilledLineColor;
    }

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray mainTypedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        mColorMain = mainTypedArray.getColor(R.styleable.CustomView_colorMain, Color.BLACK);
        mFilledLineColor = mainTypedArray.getColor(R.styleable.CustomView_colorFilledLine, Color.BLUE);
        mColorBackground = mainTypedArray.getColor(R.styleable.CustomView_colorBackground, Color.WHITE);

        mMainRadiusPaint = new Paint();
        mMainRadiusPaint.setColor(mColorMain);
        mMainRadiusPaint.setStyle(Paint.Style.STROKE);
        mMainRadiusPaint.setAntiAlias(true);
        mMainRadiusPaint.setStrokeWidth(5f);

        mWhiteArcPaint = new Paint();
        mWhiteArcPaint.setColor(Color.WHITE);
        mWhiteArcPaint.setStyle(Paint.Style.FILL);
        mWhiteArcPaint.setAntiAlias(true);

        mFilledRadiusPaint = new Paint();
        mFilledRadiusPaint.setColor(mFilledLineColor);
        mFilledRadiusPaint.setStyle(Paint.Style.FILL);
        mFilledRadiusPaint.setAntiAlias(true);

        mMainTextPaint = new Paint();
        mMainTextPaint.setColor(mColorMain);
        mMainTextPaint.setStyle(Paint.Style.STROKE);
        mMainTextPaint.setAntiAlias(true);
        mMainTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.large_text));

        mSecondaryTextPaint = new Paint();
        mSecondaryTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.small_text));
        mSecondaryTextPaint.setAntiAlias(true);
        mSecondaryTextPaint.setAlpha(50);
        mSecondaryTextPaint.setColor(mColorMain);
        mSecondaryTextPaint.setStyle(Paint.Style.STROKE);

        mStandardBounds = new RectF();
        mTotalBounds = new RectF();
        mInnerOval = new RectF();

        mMainTextBounds = new Rect();

        mLongStrokes = new ArrayList<>();
        mShortStrokes = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            mLongStrokes.add(new Stroke());
        }
        for (int i = 0; i < 40; i++) {
            mShortStrokes.add(new Stroke());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMainRadius = mMainTextPaint.measureText("100");
        mMainTextPaint.getTextBounds(String.valueOf(mCount),0,1, mMainTextBounds);
        mInnerLongStrokesRadius = mMainRadius * 1.1f;
        mOuterLongStrokesRadius = mMainRadius * 1.25f;
        mInnerShortStrokesRadius = mMainRadius * 1.15f;
        mOuterShotStrokesRadius = mMainRadius * 1.2f;

        int desiredDiameter = (int) (mMainRadius);
        int measuredWidth = resolveSize(desiredDiameter, widthMeasureSpec);
        int measuredHeight = resolveSize(desiredDiameter, heightMeasureSpec);

        mTotalBounds.set(0, 0, measuredWidth, measuredHeight);
        mStandardBounds.set(mTotalBounds);
        mStandardBounds.inset(measuredWidth * 0.175f, measuredHeight * 0.175f);
        mInnerOval.set(mStandardBounds);
        mInnerOval.inset(measuredWidth * 0.015f, measuredHeight * 0.015f);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;


        canvas.drawCircle(cx, cy, mMainRadius, mMainRadiusPaint);

        //long strokes
        canvas.save();
        for (Stroke s : mLongStrokes) {
            canvas.rotate(-45f, cx, cy);
            s.draw(cx, cy, mInnerLongStrokesRadius, mOuterLongStrokesRadius, canvas, mMainRadiusPaint);
        }
        canvas.restore();

        //short strokes
        canvas.save();
        for (Stroke s : mShortStrokes) {
            canvas.rotate(-9f, cx, cy);
            s.draw(cx, cy, mInnerShortStrokesRadius, mOuterShotStrokesRadius, canvas, mMainRadiusPaint);
        }
        canvas.restore();

        //cut
        canvas.save();
        canvas.rotate(45f, cx, cy);
        canvas.drawArc(mTotalBounds, 1f, 89f, true, mWhiteArcPaint);
        canvas.restore();

        //fill progress
        canvas.save();
        canvas.rotate(135f, cx, cy);
        canvas.drawArc(mStandardBounds, 0f, mSweepAngle, true, mFilledRadiusPaint);
        canvas.drawOval(mInnerOval, mWhiteArcPaint);
        canvas.drawCircle(cx + mMainRadius, cy, 10f, mFilledRadiusPaint);
        canvas.rotate(mSweepAngle, cx, cy);
        canvas.drawCircle(cx + mMainRadius, cy, 10f, mFilledRadiusPaint);
        canvas.restore();

        //draw text
        canvas.drawText(String.valueOf(mCount), cx-mMainTextPaint.measureText(String.valueOf(mCount))/2, cy + mMainTextBounds.height()/2, mMainTextPaint);
        canvas.drawText("%", cx-mSecondaryTextPaint.measureText("%")/2, cy + mMainTextBounds.height(), mSecondaryTextPaint);
    }


    private static class Stroke {
        Stroke() {
        }
        private void draw(int cx, int cy, float innerRadius, float outerRadius, Canvas canvas, Paint paint) {
            canvas.drawLine(cx + innerRadius, cy, cx + outerRadius, cy, paint);
        }
    }
}
