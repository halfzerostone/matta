package com.example.matta;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.widget.GridLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView timerTextView;
    private String URL ="https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=대덕고";
    String msg;
    String datestring;
    String lunchcheck;
    String dinnercheck;
    String regex="[^0-9]";
    final Bundle bundle=new Bundle();
    String today=" TODAY";
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    String dat = dateFormat.format(calendar.getTime());
    String name;
    String cellnum;
    TextView cellTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        textView.setTextSize(40);
        timerTextView=findViewById(R.id.textView2);
        timerTextView.setTextSize(40);
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 6; column++) {
                cellnum="cell_"+column+row;
                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                cellTextView = findViewById(resId);
                System.out.println(cellnum);
                cellTextView.setTextSize(30);
            }
        }
        cellTextView = findViewById(R.id.cell_05);
        System.out.println(cellnum);
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_06);
        System.out.println(cellnum);
        cellTextView.setTextSize(30);
        for (int row = 6; row < 9; row++) {
            for (int column = 0; column < 6; column++) {
                cellnum="cell_"+column+row;
                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                cellTextView = findViewById(resId);
                System.out.println(cellnum);
                cellTextView.setTextSize(30);
            }
        }
        cellTextView = findViewById(R.id.cell_09);
        System.out.println(cellnum);
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_010);
        System.out.println(cellnum);
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_011);
        System.out.println(cellnum);
        cellTextView.setTextSize(30);

        new Thread(){
            @Override
            public void run(){
                Document doc =null;
                try{
                    doc=Jsoup.connect(URL).get();
                    //doc.outputSettings().prettyPrint(false);
                    Element elements =doc.select(".time_normal_list").first();
                    msg=elements.text();
                    String[] menuf=msg.split("\\s+");
                    Element date=doc.select(".cm_date").first();

                    datestring=date.text();
                    datestring=datestring.replaceAll((today),"");
                    String Output="";
                    Output+=datestring+"\n";
                    lunchcheck=datestring.replaceAll(regex,"");

                    for (String sentence : menuf) {
                        System.out.println(sentence); // 각 단어 출력 후 줄바꿈
                        Output+=sentence+"\n";
                    }


                    elements =doc.select(".time_normal_list").get(1);
                    msg=elements.text();
                    String [] menus=msg.split("\\s+");

                    date=doc.select(".cm_date").get(1);
                    datestring=date.text();
                    datestring=datestring.replaceAll((today),"");
                    Output+="\n"+datestring+"\n";
                    dinnercheck=datestring.replaceAll(regex,"");

                    if(lunchcheck.equals(dinnercheck)){
                        for (String sentence : menus) {
                            System.out.println(sentence); // 각 단어 출력 후 줄바꿈
                            Output += sentence + "\n";
                        }

                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    String finalOutput = Output;
                    handler.post(new Runnable() {
                        public void run() {
                            textView.setText(finalOutput);
                        }
                    });


                }catch (IOException e){
                    e.printStackTrace();
                }


            }
        }.start();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 현재 시간 구하기
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int timevalue=hour*60+minute;
                        // TextView 업데이트
                        timerTextView.setText(String.format("%02d:%02d 어서와 모두 널 기다려", hour, minute));
                        if(timevalue>=490&&timevalue<540){
                            int timepercentage=(timevalue-490)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 1교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=540&&timevalue<550){
                            int timepercentage=(timevalue-540)*100/10;
                            if(dat.equals("Monday")){
                                name="국어선택";
                            }else if(dat.equals("Tuesday")){
                                name="선택 C";
                            }else if(dat.equals("Wednesday")){
                                name="제2외국어";
                            }else if(dat.equals("Thursday")){
                                name="선택 C";
                            }else if(dat.equals("Friday")){
                                name="영어 B";
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=550&&timevalue<600){
                            int timepercentage=(timevalue-550)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 2교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=600&&timevalue<610){
                            int timepercentage=(timevalue-600)*100/10;
                            if(dat.equals("Monday")){
                                name="제2외국어";
                            }else if(dat.equals("Tuesday")){
                                name="기하";
                            }else if(dat.equals("Wednesday")){
                                name="선택 A";
                            }else if(dat.equals("Thursday")){
                                name="스생";
                            }else if(dat.equals("Friday")){
                                name="선택 B";
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=610&&timevalue<660){
                            int timepercentage=(timevalue-610)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 3교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=660&&timevalue<670){
                            int timepercentage=(timevalue-660)*100/10;
                            if(dat.equals("Monday")){
                                name="영어 B";
                            }else if(dat.equals("Tuesday")){
                                name="선택 B";
                            }else if(dat.equals("Wednesday")){
                                name="국어선택";
                            }else if(dat.equals("Thursday")){
                                name="국어선택";
                            }else if(dat.equals("Friday")){
                                name="진로";
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=670&&timevalue<720){
                            int timepercentage=(timevalue-660)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 4교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=720&&timevalue<780){
                            int timepercentage=(timevalue-720)*100/60;
                            if(dat.equals("Monday")){
                                name="영어 A";
                            }else if(dat.equals("Tuesday")){
                                name="국어선택";
                            }else if(dat.equals("Wednesday")){
                                name="스생";
                            }else if(dat.equals("Thursday")){
                                name="선택 B";
                            }else if(dat.equals("Friday")){
                                name="기하";
                            }
                            timerTextView.setText(String.format("%02d:%02d 점심시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=780&&timevalue<830){
                            int timepercentage=(timevalue-780)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 5교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=830&&timevalue<840){
                            int timepercentage=(timevalue-830)*100/10;
                            if(dat.equals("Monday")){
                                name="선택 C";
                            }else if(dat.equals("Tuesday")){
                                name="영어 A";
                            }else if(dat.equals("Wednesday")){
                                name="선택 D";
                            }else if(dat.equals("Thursday")){
                                name="심리";
                            }else if(dat.equals("Friday")){
                                name="자율";
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=840&&timevalue<890){
                            int timepercentage=(timevalue-840)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 6교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=890&&timevalue<910){
                            int timepercentage=(timevalue-890)*100/20;
                            if(dat.equals("Monday")){
                                name="창체";
                            }else if(dat.equals("Tuesday")){
                                name="선택 A";
                            }else if(dat.equals("Wednesday")){
                                name="영어 B";
                            }else if(dat.equals("Thursday")){
                                name="선택 E";
                            }else if(dat.equals("Friday")){
                                name="자율";
                            }
                            timerTextView.setText(String.format("%02d:%02d 청소(안하는)시간 %d%% 다음시간:%s", hour, minute,timepercentage,name));
                        }else if(timevalue>=910&&timevalue<960){
                            int timepercentage=(timevalue-910)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 7교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=960&&timevalue<970){
                            int timepercentage=(timevalue-960)*100/10;
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=970&&timevalue<1080){
                            int timepercentage=(timevalue-970)*100/90;
                            timerTextView.setText(String.format("%02d:%02d 오자시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1080&&timevalue<1140){
                            int timepercentage=(timevalue-1080)*100/60;
                            timerTextView.setText(String.format("%02d:%02d 석식시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1140&&timevalue<1230){
                            int timepercentage=(timevalue-1140)*100/90;
                            timerTextView.setText(String.format("%02d:%02d 야자 1교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1230&&timevalue<1240){
                            int timepercentage=(timevalue-1230)*100/10;
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1240&&timevalue<1320){
                            int timepercentage=(timevalue-1240)*100/80;
                            timerTextView.setText(String.format("%02d:%02d 야자 2교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1320){
                            timerTextView.setText(String.format("%02d:%02d 집가 이제...", hour, minute));
                        }

                    }
                });
            }
        }, 0, 5);
    }
}