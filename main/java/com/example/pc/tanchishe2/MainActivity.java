package com.example.pc.tanchishe2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{
    static int x=100,y=500,k=0,i=0,num=1,best=0,score=0,width=1980,height=1080;
    static float t1=0,t2=0,datx=0,daty=0,size=0,time=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawView draw = new DrawView(this) ;
        final LinearLayout main = (LinearLayout)findViewById(R.id.root) ;
        final RelativeLayout re = (RelativeLayout) findViewById(R.id.re) ;
        main.post(new Runnable() {
            @Override
            public void run() {
                width=re.getWidth();
                height=re.getHeight();
                draw.height=height;
                draw.width=width;
                size=height/80;
                draw.size=size;
            }
        });

        SharedPreferences best0 = getSharedPreferences("best",
                Activity.MODE_PRIVATE);
        int history = best0.getInt("best", 0);
        TextView besttext = (TextView)findViewById(R.id.best);
        besttext.setText("最高分："+history);
        best=history;
        draw.setMinimumWidth(300) ;
        draw.setMinimumHeight(500) ;
        main.addView(draw) ;
        draw.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(Math.abs(event.getX()-t1)>=2&&Math.abs(event.getX()-t1)<=30) {
                    datx=size*(num+10)/(num+100)*(float) Math.sin(Math.atan((event.getX() - t1)/(event.getY() - t2)));
                    daty=size*(num+10)/(num+100)*(float) Math.cos(Math.atan((event.getX() - t1)/(event.getY() - t2)));
                    if(event.getY() - t2<0){
                        daty=-daty;
                        datx=-datx;
                    }
                }else if(Math.abs(event.getY()-t2)>=2&&Math.abs(event.getY()-t2)<=30){
                    datx=size*(num+10)/(num+100)*(float) Math.sin(Math.atan((event.getX() - t1)/(event.getY() - t2)));
                    daty=size*(num+10)/(num+100)*(float) Math.cos(Math.atan((event.getX() - t1)/(event.getY() - t2)));
                    if(event.getY() - t2<0){
                        daty=-daty;
                        datx=-datx;
                    }
                }
                if(Math.abs(event.getX()-t1)>=2&&Math.abs(event.getX()-t1)<=30||Math.abs(event.getY()-t2)>=2&&Math.abs(event.getY()-t2)<=30){
                    if(datx==0)draw.rotate=0;
                    draw.rotate=-(float)( Math.atan((datx)/(daty))*180/Math.PI);
                    if(daty>0)draw.rotate+=180;
                }

                t1=event.getX();
                t2=event.getY();
                return true;


            }

        }) ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(time>4){
                        draw.open=false;
                    }
                    else if(time<4){
                        draw.open = true;
                        time+=1;
                    }
                    else if(time==4){
                        num+=1;
                        draw.foodx=(float)Math.random()*(width-2*size)+size;
                        draw.foody=(float)Math.random()*(height-2*size)+size;
                        time+=1;
                        score=num-1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView currentscore = (TextView)findViewById(R.id.score);
                                currentscore.setText("得分："+score);
                            }
                        });
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    draw.x[0]=draw.currentx;
                    draw.y[0]=draw.currenty;
                    for(int i=DrawView.num-2;i>=0;i--){
                        draw.x[i+1]=draw.x[i];
                        draw.y[i+1]=draw.y[i];
                    }
                    draw.currentx+=datx;
                    draw.currenty+=daty;

                    if(draw.currentx<size){
                        score=num-1;
                        draw.game=true;
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;

                    }
                    if(draw.currentx>width-size){
                        score=num-1;
                        draw.game=true;
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }
                    if(draw.currenty<size){
                        score=num-1;
                        draw.game=true;
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }
                    if(draw.currenty>height-size){
                        score=num-1;
                        draw.game=true;
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }
                    if(Math.abs(draw.currentx-draw.foodx)<2*size&&Math.abs(draw.currenty-draw.foody)<2*size){
                        time=0;

                    }
                    draw.postInvalidate();


                }
            }
        }).start();

    }


    @Override
    public void run() {
        if(best<score)best=score;
        SharedPreferences best0 = getSharedPreferences("best", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = best0.edit();
        editor.putInt("best", best);
        editor.commit();
        TextView bestscore = (TextView)findViewById(R.id.best2);
        bestscore.setVisibility(View.VISIBLE);
        bestscore.setText("最高分："+best);
        TextView currentscore = (TextView)findViewById(R.id.score2);
        currentscore.setVisibility(View.VISIBLE);
        currentscore.setText("得分："+score);
        Button button=(Button)findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
    }
    public void replay(View view){
        x=10;y=50;k=0;i=0;num=1;
        t1=0;t2=0;datx=0;daty=0;
        time=100;
        Log.d("replay:","again");
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
