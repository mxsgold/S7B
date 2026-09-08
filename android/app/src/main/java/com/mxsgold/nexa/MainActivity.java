package com.mxsgold.nexa;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.Toast;
import java.util.*;

public class MainActivity extends Activity {
    NexaView view;
    @Override public void onCreate(Bundle b) { super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(11,11,16)); getWindow().setNavigationBarColor(Color.rgb(11,11,16)); view=new NexaView(); setContentView(view); }
    @Override public void onBackPressed() { if(view.chatOpen){ view.chatOpen=false; view.invalidate(); } else super.onBackPressed(); }

    class NexaView extends View {
        Paint p=new Paint(3); boolean chatOpen=false; float den; String active="Maksim";
        String[] names={"Maksim","Sofia","Artem","Nika","David"};
        String[] texts={"Увидимся вечером?","Скину фото через минуту","Го в кино завтра","Ты уже дома?","Окей, договорились"};
        int[] colors={0xff6c63ff,0xffff6b8a,0xff20b486,0xffff9f43,0xff4dabf7};
        NexaView(){ super(MainActivity.this); den=getResources().getDisplayMetrics().density; p.setTypeface(Typeface.create("sans",0)); setBackgroundColor(0xff0b0b10); }
        float d(float x){return x*den;}
        void rect(Canvas c,float l,float t,float r,float b,float rad,int color){p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawRoundRect(d(l),d(t),d(r),d(b),d(rad),d(rad),p);}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setColor(color);p.setTextSize(d(size));p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));p.setStyle(Paint.Style.FILL);c.drawText(s,d(x),d(y),p);}
        @Override protected void onDraw(Canvas c){super.onDraw(c); if(chatOpen) drawChat(c); else drawHome(c);}
        void drawHome(Canvas c){
            text(c,"Nexa",22,40,30,0xfff7f7fb,true); text(c,"2 онлайн",22,62,13,0xff777786,false);
            text(c,"⌕",318,43,30,0xffb5b5c4,false); rect(c,350,20,392,56,21,0xff17171f); text(c,"⋮",364,46,26,0xffdcdce6,true);
            rect(c,18,78,392,126,18,0xff14141b); text(c,"⌕",34,109,25,0xff777786,false); text(c,"Поиск чатов",67,108,15,0xff777786,false);
            text(c,"Чаты",20,164,22,0xfff7f7fb,true); text(c,"Все",345,164,14,0xff8d89ff,true);
            for(int i=0;i<names.length;i++) drawRow(c,i,184+i*72);
            rect(c,316,548,386,618,26,0xff6c63ff); text(c,"+",341,595,30,Color.WHITE,false); text(c,"Новый чат",301,638,12,0xff777786,false);
            rect(c,0,665,410,720,0,0xff101017); text(c,"Чаты",45,698,12,0xff9b97ff,true); text(c,"◉",193,700,20,0xff777786,false); text(c,"Профиль",326,698,12,0xff777786,false);
        }
        void drawRow(Canvas c,int i,float y){
            rect(c,20,y,64,y+44,22,colors[i]); text(c,names[i].substring(0,1),37,y+30,17,Color.WHITE,true);
            text(c,names[i],78,y+18,16,0xfff2f2f7,true); text(c,texts[i],78,y+39,13,0xff858591,false);
            text(c,(i+1)+" мин",337,y+18,11,0xff666674,false); if(i<2) {rect(c,379,y+31,384,y+36,3,0xff6c63ff);}
        }
        void drawChat(Canvas c){
            rect(c,0,0,410,74,0,0xff101017); text(c,"‹",20,49,38,0xfff2f2f7,false); rect(c,58,15,98,55,20,0xff6c63ff); text(c,"M",70,42,17,Color.WHITE,true); text(c,active,112,36,17,0xfff5f5fa,true); text(c,"в сети",112,55,12,0xff20b486,false); text(c,"⋮",374,46,27,0xffd9d9e2,true);
            text(c,"Сегодня",177,100,11,0xff666674,true);
            bubble(c,"Привет! Ты сегодня свободен?",18,128,260,0xff181821,false);
            bubble(c,"Привет 👋 Да, после семи свободен.",145,184,392,0xff6c63ff,true);
            bubble(c,"Тогда увидимся у метро в 19:30?",18,246,276,0xff181821,false);
            bubble(c,"Договорились 👍",250,302,392,0xff6c63ff,true);
            rect(c,14,570,396,622,26,0xff15151d); text(c,"Сообщение...",35,602,15,0xff777786,false); rect(c,344,578,388,614,18,0xff6c63ff); text(c,"↑",358,604,21,Color.WHITE,true);
            text(c,"＋",25,655,25,0xff777786,false); text(c,"⌁",77,655,25,0xff777786,false); text(c,"Камера",116,653,11,0xff777786,false);
        }
        void bubble(Canvas c,String s,float l,float t,float r,int color,boolean mine){ float w=Math.min(r-l, (p.measureText(s)/den)+34); float rr=mine? r : l+w; rect(c,mine?l:18,t,mine?392:18+w,t+46,18,color); text(c,s,mine?l+14:32,t+28,14,0xfff4f4f7,false); }
        @Override public boolean onTouchEvent(android.view.MotionEvent e){ if(e.getAction()!=MotionEvent.ACTION_UP)return true; float x=e.getX()/den,y=e.getY()/den;
            if(chatOpen){ if(y<70&&x<55){chatOpen=false;invalidate();return true;} if(y>565&&y<630&&x>320){Toast.makeText(MainActivity.this,"Сообщение отправлено (демо)",Toast.LENGTH_SHORT).show();} return true; }
            if(y>=180&&y<560){int i=(int)((y-184)/72); if(i>=0&&i<names.length){active=names[i];chatOpen=true;invalidate();} return true;} if(y>540&&y<635&&x>285){Toast.makeText(MainActivity.this,"Новый чат — демо",Toast.LENGTH_SHORT).show();} return true; }
    }
}
