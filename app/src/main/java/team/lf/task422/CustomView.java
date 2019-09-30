package team.lf.task422;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {
    private static final String TAG = "CustomView";

    private Paint mMainRadiusPaint;
    private Paint mWhiteArcPaint;


    private float mMainRadius;
    //strokes
    private float mInnerLongStrokesRadius;
    private float mOuterLongStrokesRadius;
    private float mInnerShortStrokesRadius;
    private float mOutterShotStrokesRadius;


    private RectF mStandardBounds;
    private RectF mTotalBounds;
    private RectF mInnerOval;

    private int mColorMain;
    private int mColorSecondary;
    private int mColorBackground;

    private List<Stroke> mLongStrokes;
    private List<Stroke> mShortStrokes;

    private float mAngleSum = 0f;


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
        mColorSecondary = mainTypedArray.getColor(R.styleable.CustomView_colorSecondary, Color.WHITE);
        mColorBackground = mainTypedArray.getColor(R.styleable.CustomView_colorBackground, Color.WHITE);

        mMainRadiusPaint = new Paint();
        mMainRadiusPaint.setColor(mColorMain);
        mMainRadiusPaint.setStyle(Paint.Style.STROKE);
        mMainRadiusPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.large_text));
        mMainRadiusPaint.setAntiAlias(true);
        mMainRadiusPaint.setStrokeWidth(5f);

        mWhiteArcPaint = new Paint();
        mWhiteArcPaint.setColor(Color.WHITE);
        mWhiteArcPaint.setStyle(Paint.Style.FILL);
        mWhiteArcPaint.setAntiAlias(true);


        mStandardBounds = new RectF();
        mTotalBounds = new RectF();
        mInnerOval = new RectF();

        mLongStrokes = new ArrayList<>();
        mShortStrokes = new ArrayList<>();

        for(int i = 0; i< 8; i++){
            mLongStrokes.add(new Stroke());
        }

        for(int i=0; i<40; i++){
            mShortStrokes.add(new Stroke());
        }


    }

    private List<Sector> sectorsFactory(int count, int total, float strokeSize) {
        List<Sector> sectors = new ArrayList<>(total);
        if (count == total || count > total) {
            sectors.add(new Sector(1f, mColorMain));
            return sectors;
        } else if (count == 0) {
            sectors.add(new Sector(1f, mColorSecondary));
            return sectors;
        } else {
            for (int i = 0; i < count; i++) {
                sectors.add(new Sector(1f, mColorMain));
                sectors.add(new Sector(strokeSize, mColorBackground));
            }
            for (int i = 0; i < (total - count); i++) {
                sectors.add(new Sector(1f, mColorSecondary));
                sectors.add(new Sector(strokeSize, mColorBackground));
            }
            return sectors;
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMainRadius = mMainRadiusPaint.measureText("10");
        mInnerLongStrokesRadius = mMainRadius * 1.1f;
        mOuterLongStrokesRadius = mMainRadius * 1.25f;
        mInnerShortStrokesRadius = mMainRadius * 1.15f;
        mOutterShotStrokesRadius = mMainRadius*1.2f;
        int desiredDiameter = (int) (mMainRadius * 1f);
        int measuredWidth = resolveSize(desiredDiameter, widthMeasureSpec);
        int measuredHeight = resolveSize(desiredDiameter, heightMeasureSpec);
        mTotalBounds.set(0, 0, measuredWidth, measuredHeight);
        mStandardBounds.set(mTotalBounds);
        mStandardBounds.inset(measuredWidth * 0.22f, measuredHeight * 0.22f);
        mInnerOval.set(mTotalBounds);
        mInnerOval.inset(measuredWidth * 0.05f, measuredHeight * 0.05f);
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
            s.draw(cx, cy, mInnerShortStrokesRadius, mOutterShotStrokesRadius, canvas, mMainRadiusPaint);
        }
        canvas.restore();

        canvas.save();
        canvas.rotate(45f, cx,cy);
        canvas.drawArc(mTotalBounds,1f,88f,true,mWhiteArcPaint);
        canvas.restore();





    }


    private static class Stroke {

        public Stroke() {
        }

        private void draw(int cx, int cy, float innerRadius, float outerRadius, Canvas canvas, Paint paint) {
            canvas.drawLine(cx + innerRadius, cy, cx +outerRadius, cy, paint);
        }

    }

    private static class Sector {
        private float mValue;
        private Paint mPaint;

        private float mAngle;
        private float mPercent;
        private float mStartAngle;
        private float mEndAngle;

        private Sector(float value, int color) {
            mValue = value;
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
        }

        private float draw(Canvas canvas, RectF bounds, float startAngle) {
            canvas.drawArc(bounds, startAngle, mAngle, true, mPaint);
            return startAngle + mAngle;
        }


        private void calculate(float valueSum) {
            mAngle = mValue / valueSum * 270f;
            mPercent = mValue / valueSum * 100f;
        }
    }
}
