package com.bixiri.gproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bixiri.gproject.databinding.ActivityTimeTableBinding;

import java.util.ArrayList;

public class TimeTable2 extends Activity {
    private ActivityTimeTableBinding binding; // View Binding
    public static Context context;
    public final int[] finishtime = new int[5];
    public final int[] mealtime = new int [5];
    public final String[] finishstring = new String[5];
    public final String[] mealstring = new String[5];
    public int onoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // View Binding
        binding = ActivityTimeTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this; //다른 Activity에서 참조할 수 있도록 하기

        //sharedpref 관련
        final SharedPreferences test = getSharedPreferences("test", MODE_PRIVATE);
        final SharedPreferences.Editor ed = test.edit();
        final ArrayList<String> key = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            key.add(Integer.toString(i));
        }
        final int[] keyindex = {test.getInt("keyindex", 0)}; //key값 index 불러오기
        //선언 끝


        //view , adapter 선언
        final String[] title = {"MON", "TUE", "WED", "THU", "FRI", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        final ArrayAdapter<String> ad = new ArrayAdapter<String>(this, R.layout.gridviewcustom, title);
        String[] times = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        ArrayAdapter<String> timead = new ArrayAdapter<>(this, R.layout.gridviewcustom, times);
        binding.time.setAdapter(timead);
        //선언 끝

        //불러오기
        for (int i = 0; i < 1000; i++) {
            int temp = test.getInt(key.get(i), -9); //저장된 position load
            if (temp == -1) continue;//바뀐 값일 경우 continue
            else if (temp == -9) break;//값이 있는 최대 index 도달 시 break
            title[temp] = "수업";
        }

        int var1 = test.getInt("onoff",-1);
        if(var1 == 0) binding.clickable.setClickable(true);
        else if(var1 ==1) binding.clickable.setClickable(false);
        //불러오기 과정끝

        binding.gridview.setAdapter(ad);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (title[position].equals("수업")) {
                    title[position] = " ";
                    for (int i = 0; i < 1000; i++) {
                        int temp = test.getInt(key.get(i), -9);
                        if (temp == -1) continue; //바뀐 값이면 continue
                        else if (temp == -9) break; //최대 index 도달 시 break
                        if (temp == position) {
                            ed.putInt(key.get(i), -1);
                            ed.apply();
                        } //sharedpref에 저장된 position값들 중 방금 삭제한 position에 일치시 해당 data 삭제
                    }
                } else {
                    title[position] = "수업";
                    ed.putInt(key.get(keyindex[0]++), position); // 바꾼 값의 index 저장
                    ed.putInt("keyindex", keyindex[0]); // 저장된 index의 key값 따로 저장
                    ed.apply();
                }
                ad.notifyDataSetChanged();
            }
        });


        //수정가능/불가 상태 구현
        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clickable.setClickable(true);
                onoff = 0;
                ed.putInt("onoff",onoff);
                ed.apply();
            }
        });
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clickable.setClickable(false);
                onoff = 1;
                ed.putInt("onoff",onoff);
                ed.apply();
            }
        });


        //끝나는 시간 구하기
        for(int i=6; i<title.length; i++){
            if(title[i].equals("수업")){
                switch (i%5){
                    case 0:
                        finishtime[0] = i/5+9;
                        break;
                    case 1:
                        finishtime[1] = i/5+9;
                        break;
                    case 2:
                        finishtime[2] = i/5+9;
                        break;
                    case 3:
                        finishtime[3] = i/5+9;
                        break;
                    case 4:
                        finishtime[4] = i/5+9;
                        break;
                    default:
                        break;
                }
            }
        }
        for(int i=0; i<5; i++){
            if(finishtime[i]==0){
                finishstring[i] = "공강";
            }
            else finishstring[i] = Integer.toString(finishtime[i]);
        }
        //finishtime에 끝나는 시간 저장[월,화,수,목,금]
        //mealtime 구하기
        int mon =5,tue=6,wed=7,thu=8,fri=9,
                monmin=999,monfirst=0,
                tuemin=999,tuefirst=0,
                wedmin=999,wedfirst=0,
                thumin=999,thufirst=0,
                frimin=999,frifirst=0;

        for(;mon<title.length;mon+=5){
                    if(title[mon].equals("수업"))
                    {   monfirst = mon;
                        break;   }
            }
            for(mon = monfirst; mon<title.length;mon+=5)
            {   if(title[mon].equals("수업"))continue;
                else{
                if(monmin>(mon/5+8))
                monmin = (mon/5+8);
            }}
         for(;tue<title.length;tue+=5){
                    if(title[tue].equals("수업"))
                    {   tuefirst = tue;
                        break;   }
                }
         for(tue = tuefirst; tue<title.length;tue+=5)
                {   if(title[tue].equals("수업"))continue;
                else{
                    if(tuemin>(tue/5+8))
                        tuemin = (tue/5+8);
                }}
         for(;wed<title.length;wed+=5){
                        if(title[wed].equals("수업"))
                        {   wedfirst = wed;
                            break;   }
                    }
          for(wed = wedfirst; wed<title.length;wed+=5)
                    {   if(title[wed].equals("수업"))continue;
                    else{
                        if(wedmin>(wed/5+8))
                            wedmin = (wed/5+8);
                    }}
          for(;thu<title.length;thu+=5){
                            if(title[thu].equals("수업"))
                            {   thufirst = thu;
                                break;   }
                        }
          for(thu = thufirst; thu<title.length;thu+=5)
                        {   if(title[thu].equals("수업"))continue;
                        else{
                            if(thumin>(thu/5+8))
                                thumin = (thu/5+8);
                        }}
          for(;fri<title.length;fri+=5){
                                if(title[fri].equals("수업"))
                                {   frifirst = fri;
                                    break;   }
                            }
          for(fri = frifirst; fri<title.length;fri+=5)
                            {   if(title[fri].equals("수업"))continue;
                            else{
                                if(frimin>(fri/5+8))
                                    frimin = (fri/5+8);
                            }
                            }
          for(int i=0;i<5;i++){
              switch(i){
                  case 0:
                      mealstring[i] = Integer.toString(monmin);
                      break;
                  case 1:
                      mealstring[i] = Integer.toString(tuemin);
                      break;
                  case 2:
                      mealstring[i] = Integer.toString(wedmin);
                      break;
                  case 3:
                      mealstring[i] = Integer.toString(thumin);
                      break;
                  case 4:
                      mealstring[i] = Integer.toString(frimin);
                      break;
              }
              if(finishstring[i].equals("공강")) mealstring[i] = "공강";

          }
          binding.textView.setText("식사 시간 : \n" + mealstring[0]+"\n"+mealstring[1]+"\n"+mealstring[2]+"\n"+mealstring[3]+"\n"+mealstring[4]+"\n"+
                            "하교 시간 : \n" + finishstring[0] +"\n"+ finishstring[1]+"\n"+finishstring[2]+"\n"+finishstring[3]+"\n"+finishstring[4]);
        binding.ReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
