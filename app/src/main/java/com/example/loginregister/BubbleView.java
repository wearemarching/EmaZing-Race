package com.example.loginregister;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Display;
import android.view.View;
import android.graphics.drawable.GradientDrawable;
import android.view.WindowManager;


import static android.R.attr.bitmap;
import static android.R.attr.radius;

/**
 * Created by HP on 4/15/2017.
 */

public class BubbleView extends View {
    private int diameter;
    private int x;
    private int y;
    private int width;
    private int height;
    private int mwidth;
    private int mheight;
    Bitmap bitmap;
    private GradientDrawable bubble;

    public BubbleView(Context context) {
        super(context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mwidth = size.x;
        mheight = size.y;
        width = (size.x)/2;
        height = (size.y)/2;

        createBubble();
    }
    private void createBubble() {
        x = width;
        y = height;
        diameter = width/10;
        bubble = new GradientDrawable();
        bubble.setColors(new int[]{
                0xffffffff,
                0x00000000
        });
        bubble.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        bubble.setShape(GradientDrawable.OVAL);
        bubble.setBounds(x, y, x + diameter, y + diameter);
        bubble.setGradientRadius(diameter/2);
        bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.peta);

    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        // paint.setColor(Color.CYAN);
        canvas.drawBitmap(getclip(), 0, 0, paint);

    }

    public Bitmap getclip() {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        canvas.drawCircle(x,
                y, diameter, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    protected void move(float f, float g) {
        System.out.println("TESTINGBRUH " + f + "|" + g + "|" + x + "|" + y);
        if ((x <= 0 && f > 0) || (x >= width*2 && f < 0) || (x > 0 && x < width*2)) {
            x = (int) (x + f);
        }
        if ((y <= 0 && g > 0) || (y >= height*2 && g < 0) || (y > 0 && y < height*2)) {
            y = (int) (y + g);
        }
       // bubble.setBounds(x, y, x + diameter, y + diameter);
    }

    public boolean checkQuest(int id) {
        float p1x = (float) 0.38 * bitmap.getWidth();
        float p1y = (float) 0.48 * bitmap.getHeight();
        float p2x = (float) 0.45 * bitmap.getWidth();
        float p2y = (float) 0.42 * bitmap.getHeight();
        float p3x = (float) 0.62 * bitmap.getWidth();
        float p3y = (float) 0.48 * bitmap.getHeight();
        float p4x = (float) 0.71 * bitmap.getWidth();
        float p4y = (float) 0.41 * bitmap.getHeight();
        if (id == 1) {
            if (x < p1x + diameter && x > p1x - diameter && y < p1y + diameter && y > p1y - diameter) {
                return true;
            }
        } else if (id == 2) {
            if (x < p2x + diameter && x > p2x - diameter && y < p2y + diameter && y > p2y - diameter) {
                return true;
            }
        } else if (id == 3) {
            if (x < p3x + diameter && x > p3x - diameter && y < p3y + diameter && y > p3y - diameter) {
                return true;
            }
        } else if (id == 4) {
            if (x < p4x + diameter && x > p4x - diameter && y < p4y + diameter && y > p4y - diameter) {
                return true;
            }
        }
        return false;
    }
}
