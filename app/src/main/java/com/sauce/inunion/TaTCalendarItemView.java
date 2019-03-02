package com.sauce.inunion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

public class TaTCalendarItemView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int dayOfWeek = -1;
    private boolean isStaticText = false;
    private long millis;
    //    private Rect rect;
//    private boolean isTouchMode;
//    private int dp11;
    private int dp16;
    private boolean hasEvent = false;
    private int[] mColorEvents;
    private boolean changeTextColor = false;
    long temp = 0;
    static long before_temp;

    public TaTCalendarItemView(Context context) {
        super(context);
        initialize();
    }

    public TaTCalendarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
//        dp11 = (int) dp2px(getContext(),11);
        dp16 = (int) dp2px(getContext(),16);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(50);
        mPaintBackground.setColor(Color.rgb(76,110,245));
        mPaintBackgroundToday.setColor(Color.rgb(76,110,245));
       /* if (Build.VERSION.SDK_INT >= 23) {

            mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        }else{
            mPaintBackground.setColor(Color.rgb(76,110,245));
            mPaintBackgroundToday.setColor(getContext().getResources().getColor(R.color.colorAccent));
            mPaintBackgroundEvent.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        }*/
        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = (long) view.getTag();
                if(temp!=before_temp && view.getId() != -1) {
                    ((TaTCalendarView) getParent()).setCurrentSelectedView(view);
                }
                Intent intent = new Intent("select");
                intent.putExtra("selected","true");
                intent.putExtra("selected_id",view.getId()+"");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                Log.d("ID",view.getId()+"");
                invalidate();
                before_temp = temp;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        mPaint.setTextAlign(Paint.Align.CENTER);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);


        TaTCalendarView calendarView = (TaTCalendarView) getParent();
        if (calendarView.getParent() instanceof ViewPager) {
            ViewGroup parent = (ViewPager) calendarView.getParent();
            TaTCalendarItemView tagView = (TaTCalendarItemView) parent.getTag();

            if (!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long) {
                long millis = (long) tagView.getTag();
                if (isSameDay(millis, this.millis)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(xPos - dp16+15, getHeight() / 2 - dp16+5, xPos + dp16-5, getHeight() / 2 + dp16-5, 50f, 50f, mPaintBackground);
                    }
                    else {
                        canvas.drawRect(xPos - dp16+20, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, mPaintBackground);
                    }
                }
            }
        }
//        if(temp==0) {
//            if (!isStaticText && isToday(millis)) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    canvas.drawRoundRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, 50f, 50f, mPaintBackgroundToday);
//                } else {
//                    canvas.drawRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, mPaintBackgroundToday);
//                }
//            }
//        }
        if (temp == 0){
            if (isStaticText) {
                // 요일 표시
                mPaint.setTypeface(Typeface.create((String) null, Typeface.NORMAL));
                mPaint.setColor(Color.rgb(76, 110, 245));
                mPaint.setTextSize(50);
                canvas.drawText(TaTCalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
            }
            else {
                // 날짜 표시
                mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL));
                if (changeTextColor) {
                    mPaint.setColor(Color.LTGRAY);
                    mPaint.setTextSize(50);
                }  else if(!isStaticText && isToday(millis)){
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextSize(50);
                }
                else {
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextSize(50);
                }
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaint);
            }
        }
        else{
            if (isStaticText) {
                // 요일 표시
                mPaint.setTypeface(Typeface.create((String) null, Typeface.NORMAL));
                mPaint.setColor(Color.rgb(76, 110, 245));
                mPaint.setTextSize(50);
                canvas.drawText(TaTCalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
            }
            else {
                ViewGroup parent = (ViewPager) calendarView.getParent();
                TaTCalendarItemView tagView = (TaTCalendarItemView) parent.getTag();
                // 날짜 표시
                mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL));
                if (changeTextColor) {
                    mPaint.setColor(Color.LTGRAY);
                    mPaint.setTextSize(50);
                }
//                else if(!isStaticText && isToday(millis)){
//                    mPaint.setColor(Color.BLACK);
//                    mPaint.setTextSize(50);
//                }
                else if(!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long){
                    long millis = (long) tagView.getTag();

                    if (isSameDay(millis, this.millis)){
                        mPaint.setColor(Color.WHITE);
                        mPaint.setTextSize(50);
                    }
                    else{
                        mPaint.setColor(Color.BLACK);
                        mPaint.setTextSize(50);
                    }
                }
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaint);
            }
        }
        if (hasEvent) {
            if (Build.VERSION.SDK_INT >= 23) {
                mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(),mColorEvents[0]));
            }else{
                mPaintBackgroundEvent.setColor(getResources().getColor(mColorEvents[0]));
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(xPos - 10, getHeight() / 2 + 50, xPos + 10, getHeight() / 2 + 70, 50f, 50f, mPaintBackground);
            }else{
                canvas.drawRect(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30, mPaintBackground);
            }
        }
        else {
                if (Build.VERSION.SDK_INT >= 23) {
                    mPaintBackgroundWhite.setColor(Color.TRANSPARENT);
                }else{
                    mPaintBackgroundWhite.setColor(Color.TRANSPARENT);
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(xPos - 10, getHeight() / 2 + 50, xPos + 10, getHeight() / 2 + 70, 50f, 50f, mPaintBackgroundWhite);
                    }else{
                    canvas.drawRect(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30, mPaintBackgroundWhite);
                    }
            }



    }

    public boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public void setDate(long millis) {
        this.millis = millis;
        setTag(millis);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        isStaticText = true;

    }

    public void setEvent(int... resid) {
        hasEvent = true;
        mColorEvents = resid;
        invalidate();
    }
    public void unSetEvent() {
        hasEvent = false;
        invalidate();
    }

    public void setTextColorChange(boolean changeTextColor) {
        this.changeTextColor = changeTextColor;
    }
    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
//        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
//                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean isStaticText() {
        return isStaticText;
    }

    public static float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
