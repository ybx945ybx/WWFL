package com.haitao.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Created by penley on 16/3/1.
 */
public class ShowTipsView extends RelativeLayout {
    private Point showhintPoints;
    private int radius = 0;
    private View targetView;
    private int background_alpha = 220;
    private boolean isMeasured;
    private String title;
    private int background_color, circleColor;
    private int tips_layout;

    private Bitmap bitmap;
    private Canvas temp;
    private Paint paint;
    private Paint bitmapPaint;
    private Paint circleline;
    private Paint transparentPaint;
    private PorterDuffXfermode porterDuffXfermode;
    private ShowTipsViewInterface callback;


    public ShowTipsView(Context context) {
        super(context);
        init();
    }

    public ShowTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShowTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setVisibility(View.GONE);
        this.setBackgroundColor(Color.TRANSPARENT);

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // DO NOTHING
                // HACK TO BLOCK CLICKS
                setVisibility(View.GONE);
                if (getCallback() != null) {
                    getCallback().gotItClicked();
                }

                ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(ShowTipsView.this);
            }
        });

        paint = new Paint();
        bitmapPaint = new Paint();
        circleline = new Paint();
        transparentPaint = new Paint();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            temp = new Canvas(bitmap);
        }

        if (background_color != 0)
            paint.setColor(background_color);
        else
            paint.setColor(Color.parseColor("#00000000"));

        paint.setAlpha(background_alpha);
        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(porterDuffXfermode);

        int x = showhintPoints.x;
        int y = showhintPoints.y;
        temp.drawCircle(x, y, radius, transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

        circleline.setStyle(Paint.Style.FILL);
        if (circleColor != 0)
            circleline.setColor(circleColor);
        else
            circleline.setColor(Color.TRANSPARENT);

        circleline.setAntiAlias(true);
        circleline.setStrokeWidth(0);
        canvas.drawCircle(x, y, radius, circleline);
    }

    public void show(final Activity mActivity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ViewGroup) mActivity.getWindow().getDecorView()).addView(ShowTipsView.this);
                ShowTipsView.this.setVisibility(View.VISIBLE);

                final ViewTreeObserver observer = targetView.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if (isMeasured)
                            return;

                        if (targetView.getHeight() > 0 && targetView.getWidth() > 0) {
                            isMeasured = true;
                        }
                        int[] location = new int[2];
                        targetView.getLocationInWindow(location);
                        int x = location[0] + targetView.getWidth() / 2;
                        int y = location[1] + targetView.getHeight() / 2;

                        Point p = new Point(x, y);
                        showhintPoints = p;
                        radius = targetView.getHeight() / 2;
                        invalidate();
                        createViews();

                    }
                });

            }
        }, 200);
    }

    private void createViews() {
        this.removeAllViews();
        View view = View.inflate(getContext(), tips_layout, null);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = (showhintPoints.y + radius);

        view.setLayoutParams(params);
        this.addView(view);
    }

    public void setTarget(View v) {
        targetView = v;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBackground_color() {
        return background_color;
    }

    public void setBackground_color(int background_color) {
        this.background_color = background_color;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getBackground_alpha() {
        return background_alpha;
    }

    public void setBackground_alpha(int background_alpha) {
        if (background_alpha > 255)
            this.background_alpha = 255;
        else if (background_alpha < 0)
            this.background_alpha = 0;
        else
            this.background_alpha = background_alpha;

    }

    public int getTips_layout() {
        return tips_layout;
    }

    public void setTips_layout(int tips_layout) {
        this.tips_layout = tips_layout;
    }

    public ShowTipsViewInterface getCallback() {
        return callback;
    }

    public void setCallback(ShowTipsViewInterface callback) {
        this.callback = callback;
    }

    public interface ShowTipsViewInterface {
        public void gotItClicked();
    }
}
