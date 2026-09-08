package com.mxsgold.nexa;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    NexaView view;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(11,11,16));
        getWindow().setNavigationBarColor(Color.rgb(11,11,16));
        view = new NexaView();
        setContentView(view);
    }
    @Override public void onBackPressed() {
        if (view.screen == 1 || view.screen == 2) { view.screen = 0; view.invalidate(); }
        else super.onBackPressed();
    }

    class NexaView extends View {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        float den;
        int screen = 0; // 0 home, 1 chat, 2 profile
        String active = "Maksim";
        String[] names = {"Maksim","Sofia","Artem","Nika","David"};
        String[] texts = {"Увидимся вечером?","Скину фото через минуту","Го в кино завтра","Ты уже дома?","Окей, договорились"};
        int[] colors = {0xff6c63ff,0xffff6b8a,0xff20b486,0xffff9f43,0xff4dabf7};
        String lastMessage = "";

        NexaView() {
            super(MainActivity.this);
            den = getResources().getDisplayMetrics().density;
            setBackgroundColor(0xff0b0b10);
            p.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        }
        float d(float x) { return x * den; }
        float dpW() { return getWidth() / den; }
        float dpH() { return getHeight() / den; }
        void rect(Canvas c,float l,float t,float r,float b,float rad,int color){
            p.setColor(color); p.setStyle(Paint.Style.FILL);
            c.drawRoundRect(d(l),d(t),d(r),d(b),d(rad),d(rad),p);
        }
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){
            p.setColor(color); p.setTextSize(d(size));
            p.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
            p.setStyle(Paint.Style.FILL); c.drawText(s,d(x),d(y),p);
        }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            if(screen == 1) drawChat(c); else if(screen == 2) drawProfile(c); else drawHome(c);
        }
        void drawHome(Canvas c) {
            float w=dpW(), h=dpH();
            text(c,"Nexa",22,42,30,0xfff7f7fb,true);
            text(c,"2 онлайн",22,64,13,0xff777786,false);
            text(c,"⌕",w-78,44,30,0xffb5b5c4,false);
            rect(c,w-58,18,w-12,58,21,0xff17171f); text(c,"⋮",w-43,45,26,0xffdcdce6,true);
            rect(c,18,80,w-18,128,18,0xff14141b);
            text(c,"⌕",34,111,25,0xff777786,false); text(c,"Поиск чатов",67,110,15,0xff777786,false);
            text(c,"Чаты",20,166,22,0xfff7f7fb,true); text(c,"Все",w-66,166,14,0xff8d89ff,true);
            float y=186;
            for(int i=0;i<names.length;i++){ drawRow(c,i,y); y+=72; }
            float navTop=h-66;
            float fabY=Math.min(navTop-112, y+5);
            rect(c,w-94,fabY,w-18,fabY+70,26,0xff6c63ff);
            text(c,"+",w-69,fabY+47,30,Color.WHITE,true);
            text(c,"Новый чат",w-112,fabY+98,12,0xff777786,false);
            rect(c,0,navTop,w,h,0,0xff101017);
            text(c,"Чаты",48,navTop+34,12,0xff9b97ff,true);
            text(c,"◉",w/2-10,navTop+37,20,0xff777786,false);
            text(c,"Профиль",w-82,navTop+34,12,0xff777786,false);
        }
        void drawRow(Canvas c,int i,float y){
            float w=dpW();
            rect(c,20,y,64,y+44,22,colors[i]);
            text(c,names[i].substring(0,1),37,y+30,17,Color.WHITE,true);
            text(c,names[i],78,y+18,16,0xfff2f2f7,true);
            String preview = (i==0 && lastMessage.length()>0) ? lastMessage : texts[i];
            text(c,preview,78,y+39,13,0xff858591,false);
            text(c,(i+1)+" мин",Math.max(250,w-74),y+18,11,0xff666674,false);
            if(i<2) rect(c,w-15,y+31,w-10,y+36,3,0xff6c63ff);
        }
        void drawChat(Canvas c){
            float w=dpW(), h=dpH();
            rect(c,0,0,w,74,0,0xff101017);
            text(c,"‹",20,50,38,0xfff2f2f7,false);
            rect(c,58,15,98,55,20,0xff6c63ff); text(c,active.substring(0,1),70,42,17,Color.WHITE,true);
            text(c,active,112,36,17,0xfff5f5fa,true); text(c,"в сети",112,55,12,0xff20b486,false);
            text(c,"⋮",w-38,46,27,0xffd9d9e2,true);
            text(c,"Сегодня",w/2-25,100,11,0xff666674,true);
            bubble(c,"Привет! Ты сегодня свободен?",18,128,260,0xff181821,false);
            bubble(c,"Привет 👋 Да, после семи свободен.",145,184,w-18,0xff6c63ff,true);
            bubble(c,"Тогда увидимся у метро в 19:30?",18,246,276,0xff181821,false);
            bubble(c,"Договорились 👍",250,302,w-18,0xff6c63ff,true);
            if(lastMessage.length()>0) bubble(c,lastMessage,18,364,300,0xff181821,false);
            float inputY=h-70;
            rect(c,14,inputY,w-64,inputY+52,26,0xff15151d);
            text(c,"Сообщение...",35,inputY+32,15,0xff777786,false);
            rect(c,w-58,inputY+8,w-14,inputY+44,18,0xff6c63ff); text(c,"↑",w-44,inputY+33,21,Color.WHITE,true);
            text(c,"＋",25,inputY+83,25,0xff777786,false); text(c,"⌁",77,inputY+83,25,0xff777786,false); text(c,"Камера",116,inputY+81,11,0xff777786,false);
        }
        void bubble(Canvas c,String s,float l,float t,float r,int color,boolean mine){
            p.setTextSize(d(14)); float width=Math.min(r-l, p.measureText(s)/den+34);
            float left=mine ? r-width : l; float right=mine ? r : l+width;
            rect(c,left,t,right,t+46,18,color);
            text(c,s,left+14,t+28,14,0xfff4f4f7,false);
        }
        void drawProfile(Canvas c){
            float w=dpW(), h=dpH();
            text(c,"‹",20,52,38,0xfff2f2f7,false);
            text(c,"Профиль",w/2-45,44,20,0xfff5f5fa,true);
            rect(c,w/2-48,95,w/2+48,191,48,0xff6c63ff);
            text(c,"N",w/2-18,158,38,Color.WHITE,true);
            text(c,"Nexa User",w/2-50,225,21,0xfff5f5fa,true);
            text(c,"@nexa_user",w/2-40,249,13,0xff777786,false);
            rect(c,20,282,w-20,338,18,0xff14141b);
            text(c,"Мой статус",38,306,13,0xff777786,false); text(c,"В сети",38,327,15,0xff20b486,true);
            rect(c,20,356,w-20,412,18,0xff14141b);
            text(c,"Уведомления",38,381,15,0xfff0f0f5,false); text(c,"›",w-48,391,24,0xff777786,false);
            rect(c,20,430,w-20,486,18,0xff14141b);
            text(c,"Тёмная тема",38,455,15,0xfff0f0f5,false); text(c,"Вкл",w-70,455,13,0xff9b97ff,true);
            text(c,"Nexa Messenger • Demo",w/2-78,h-40,11,0xff555563,false);
        }
        void showMessageDialog(){
            final EditText input=new EditText(MainActivity.this);
            input.setHint("Введите сообщение");
            input.setSingleLine(false);
            input.setTextColor(Color.WHITE); input.setHintTextColor(0xff888895);
            input.setBackgroundColor(0xff17171f);
            int pad=(int)d(12); input.setPadding(pad,pad,pad,pad);
            AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                .setTitle("Новое сообщение")
                .setView(input)
                .setNegativeButton("Отмена",null)
                .setPositiveButton("Отправить",null).create();
            dialog.setOnShowListener(x -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String s=input.getText().toString().trim();
                if(!s.isEmpty()){ lastMessage=s; dialog.dismiss(); invalidate(); Toast.makeText(MainActivity.this,"Сообщение отправлено",Toast.LENGTH_SHORT).show(); }
            }));
            dialog.getWindow(); dialog.show(); input.requestFocus();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent e){
            if(e.getAction()!=MotionEvent.ACTION_UP) return true;
            float x=e.getX()/den, y=e.getY()/den, w=dpW(), h=dpH();
            if(screen==1){
                if(y<75 && x<65){ screen=0; invalidate(); return true; }
                if(y>h-95){ showMessageDialog(); return true; }
                return true;
            }
            if(screen==2){ if(y<75){screen=0;invalidate();} return true; }
            float navTop=h-66;
            if(y>=navTop && x>w/2+55){ screen=2; invalidate(); return true; }
            if(y>=180 && y<540){ int i=(int)((y-186)/72); if(i>=0&&i<names.length){active=names[i];screen=1;invalidate();} return true; }
            float fabY=Math.min(navTop-112,186+names.length*72+5);
            if(y>=fabY && y<fabY+110 && x>w-130){ Toast.makeText(MainActivity.this,"Новый чат — демо",Toast.LENGTH_SHORT).show(); return true; }
            return true;
        }
    }
}"}