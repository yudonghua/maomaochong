package com.example.pc.tanchishe2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Administrator on 2016/7/16.
 */
public class DrawView extends View {

    public float currentx = 400 ;
    public  float currenty = 400 ;
    public float height = 400 ;
    public  float width = 400 ;
    public float foodx = 200 ;
    public  float foody = 600 ;
    public float rotate=0;
    public  float size = 25;
    public static int num=23;
    public static int g=0;
    public  boolean game=false;
    public  boolean open=false;
    public float x[]=new float[num],y[]=new float[num];


    /**
     *
     * @param context
     */
    public DrawView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //创建画笔 ;
        Paint p = new Paint() ;
        p.setTextSize(30);
        p.setColor(Color.rgb(152,217,0)) ;
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.apple);
        Bitmap bhead= BitmapFactory.decodeResource(getResources(), R.mipmap.head);
        Bitmap bopen= BitmapFactory.decodeResource(getResources(), R.mipmap.open);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (int)(size*2);
        int newHeight = (int)(size*2);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bhead = Bitmap.createBitmap(bhead, 0, 0, width, height, matrix, true);
        bopen = Bitmap.createBitmap(bopen, 0, 0, width, height, matrix, true);
        draw(canvas,bitmap,(int)(foodx-size),(int)(foody-size));
        for(int i=0;i<num;i++){
            canvas.drawCircle(x[i], y[i],(float)0.9*size, p) ;
        }
        if(open){
            canvas.rotate(rotate,currentx,currenty);
            draw(canvas,bopen,(int)(currentx-size),(int)(currenty-size));
            canvas.rotate(-rotate,currentx,currenty);
        }
        else{
            canvas.rotate(rotate,currentx,currenty);
            draw(canvas,bhead,(int)(currentx-size),(int)(currenty-size));
            canvas.rotate(-rotate,currentx,currenty);
        }



    }
    public void draw(Canvas canvas,Bitmap bitmap,int x,int y){
        canvas.drawBitmap(bitmap,x,y,null);
    }
}

