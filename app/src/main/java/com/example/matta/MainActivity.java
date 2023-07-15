package com.example.matta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView textView;  //여기에 급식 식단표 표시
    TextView dinnertextView;
    TextView CalenderView;
    TextView timerTextView; //시간과 다음시간 표시
    private String URL ="https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=대덕고";  //급식 크롤링 대상 링크
    private String CalenderURLbase="https://daedeokhs.djsch.kr/schedule/list.do?s=taedokhs&schdYear=";
    String CalenderURL;
    String msg; //크롤링에 사용하는 내부 메시지
    String datestring; //오늘 날짜를 자료화함(***같은 형식으로)
    String lunchcheck;  //점심 메뉴 존재 체크용 변수
    String dinnercheck; //저녁 메뉴 존재 체크용 변수
    String regex="[^0-9]"; //정수 전부 제거하기 위한 0-9까지의 숫자
    final Bundle bundle=new Bundle();
    String today=" TODAY";  //대체할 문자열(공백)
    Calendar calendar = Calendar.getInstance(); //오늘 날짜 받는 변수
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());    //날짜 받는 변수의 형태 선언
    SimpleDateFormat ymFormat=new SimpleDateFormat("yyyy.MM",Locale.getDefault());
    String dat = dateFormat.format(calendar.getTime()); //분 단위 변수 불러오기
    String ymdat=ymFormat.format(calendar.getTime());
    String[] parts=ymdat.split("\\.");
    String year;
    String month;
    String name;    //이번시간 이름을 시간표에서 받아오기
    String cellnum; //
    String arraynum;
    TextView cellTextView;
    String dateUI;
    String outputdate;
    String outputtime;
    String CurrentSubject;
    int Class=4;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int myInt;
    int dayofmonth;
    String daystring;
    String[] menuf;
    int startday;
    String Subject[][]=new String[10][10];

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        year=parts[0];
        month=parts[1];
        CalenderURL=CalenderURLbase+year+"&schdMonth="+month;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView) findViewById(R.id.textViewMain);
        dinnertextView=(TextView) findViewById(R.id.textViewMaindinner);
        //textView.setTextSize(30);
        timerTextView=findViewById(R.id.textView2);
        timerTextView.setTextSize(30);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        dayofmonth=calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayofmonth);


        NumberPicker npker = findViewById(R.id.Classpick);

        npker.setOnLongPressUpdateInterval(100);
        npker.setWrapSelectorWheel(true);
        npker.setMaxValue(11); //최대값
        npker.setMinValue(1); //최소값

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        myInt=pref.getInt("Class", 4);
        npker.setValue(myInt);

        Class=npker.getValue();

        if(Class==4){
            myInt=4;
            editor.putInt("Class", myInt);
            editor.apply();
            Subject[1][1]="선택 A";
            Subject[1][2]="국어";
            Subject[1][3]="제2외국어";
            Subject[1][4]="영어 A";
            Subject[1][5]="영어 B";
            Subject[1][6]="선택 C";
            Subject[1][7]="창체";
            Subject[2][1]="미감";
            Subject[2][2]="선택 C";
            Subject[2][3]="기하";
            Subject[2][4]="선택 B";
            Subject[2][5]="국어";
            Subject[2][6]="영어 A";
            Subject[2][7]="선택 A";
            Subject[3][1]="기하";
            Subject[3][2]="제2외국어";
            Subject[3][3]="선택 A";
            Subject[3][4]="국어";
            Subject[3][5]="스생";
            Subject[3][6]="선택 D";
            Subject[3][7]="영어 B";
            Subject[4][1]="기하";
            Subject[4][2]="선택 C";
            Subject[4][3]="스생";
            Subject[4][4]="국어";
            Subject[4][5]="선택 B";
            Subject[4][6]="심리";
            Subject[4][7]="선택 E";
            Subject[5][1]="국어";
            Subject[5][2]="영어 B";
            Subject[5][3]="선택 B";
            Subject[5][4]="진로";
            Subject[5][5]="기하";
            Subject[5][6]="자율";
            Subject[5][7]="자율";
            for(int i=1;i<5;i++){ //i요일
                for(int j=1;j<7;j++){  //교시
                    CurrentSubject=Subject[i][j];
                    if(i==1&&j!=5){
                        cellnum="cell_Monday"+j;
                        int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                        cellTextView = findViewById(resId);
                        cellTextView.setText(CurrentSubject);
                    }else if(i==2&&j!=5){
                        cellnum="cell_Tuesday"+j;
                        int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                        cellTextView = findViewById(resId);
                        cellTextView.setText(CurrentSubject);
                    }else if(i==3&&j!=5){
                        cellnum="cell_Wednesday"+j;
                        int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                        cellTextView = findViewById(resId);
                        cellTextView.setText(CurrentSubject);
                    }else if(i==4&&j!=5){
                        cellnum="cell_Thursday"+j;
                        int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                        cellTextView = findViewById(resId);
                        cellTextView.setText(CurrentSubject);
                    }else if(i==5&&j!=5){
                        cellnum="cell_Friday"+j;
                        int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                        cellTextView = findViewById(resId);
                        cellTextView.setText(CurrentSubject);
                    }
                }
            }
        }


        npker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                Class=newVal;
                if(Class==4){
                    myInt=4;
                    editor.putInt("Class", myInt);
                    editor.apply();
                    Subject[1][1]="선택 A";
                    Subject[1][2]="국어";
                    Subject[1][3]="제2외국어";
                    Subject[1][4]="영어 A";
                    Subject[1][5]="영어 B";
                    Subject[1][6]="선택 C";
                    Subject[1][7]="창체";
                    Subject[2][1]="미감";
                    Subject[2][2]="선택 C";
                    Subject[2][3]="기하";
                    Subject[2][4]="선택 B";
                    Subject[2][5]="국어";
                    Subject[2][6]="영어 A";
                    Subject[2][7]="선택 A";
                    Subject[3][1]="기하";
                    Subject[3][2]="제2외국어";
                    Subject[3][3]="선택 A";
                    Subject[3][4]="국어";
                    Subject[3][5]="스생";
                    Subject[3][6]="선택 D";
                    Subject[3][7]="영어 B";
                    Subject[4][1]="기하";
                    Subject[4][2]="선택 C";
                    Subject[4][3]="스생";
                    Subject[4][4]="국어";
                    Subject[4][5]="선택 B";
                    Subject[4][6]="심리";
                    Subject[4][7]="선택 E";
                    Subject[5][1]="국어";
                    Subject[5][2]="영어 B";
                    Subject[5][3]="선택 B";
                    Subject[5][4]="진로";
                    Subject[5][5]="기하";
                    Subject[5][6]="자율";
                    Subject[5][7]="자율";
                    for(int i=1;i<5;i++){ //i요일
                        for(int j=1;j<7;j++){  //교시
                            CurrentSubject=Subject[i][j];
                            if(i==1&&j!=5){
                                cellnum="cell_Monday"+j;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                cellTextView.setText(CurrentSubject);
                            }else if(i==2&&j!=5){
                                cellnum="cell_Tuesday"+j;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                cellTextView.setText(CurrentSubject);
                            }else if(i==3&&j!=5){
                                cellnum="cell_Wednesday"+j;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                cellTextView.setText(CurrentSubject);
                            }else if(i==4&&j!=5){
                                cellnum="cell_Thursday"+j;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                cellTextView.setText(CurrentSubject);
                            }else if(i==5&&j!=5){
                                cellnum="cell_Friday"+j;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                cellTextView.setText(CurrentSubject);
                            }
                        }
                    }
                }//여기서 else if를 함으로서 반 설정
            }
        });

        for(int i=1;i<5;i++){ //i요일
            for(int j=1;j<7;j++){  //교시
                CurrentSubject=Subject[i][j];
                if(i==1&&j!=5){
                    cellnum="cell_Monday"+j;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                    cellTextView.setText(CurrentSubject);
                }else if(i==2&&j!=5){
                    cellnum="cell_Tuesday"+j;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                    cellTextView.setText(CurrentSubject);
                }else if(i==3&&j!=5){
                    cellnum="cell_Wednesday"+j;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                    cellTextView.setText(CurrentSubject);
                }else if(i==4&&j!=5){
                    cellnum="cell_Thursday"+j;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                    cellTextView.setText(CurrentSubject);
                }else if(i==5&&j!=5){
                    cellnum="cell_Friday"+j;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                    cellTextView.setText(CurrentSubject);
                }
            }
        }

        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 8; row++) {
                if(column==0){
                    cellnum="cell_Layout"+row;
                     
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                } else if(column==1){
                    cellnum="cell_Monday"+row;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                }else if(column==2){
                    cellnum="cell_Tuesday"+row;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                }else if(column==3){
                    cellnum="cell_Wednesday"+row;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                }else if(column==4){
                    cellnum="cell_Thursday"+row;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                }else if(column==5){
                    cellnum="cell_Friday"+row;
                    int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                    cellTextView = findViewById(resId);
                     
                    cellTextView.setTextSize(30);
                }
            }
        }

        cellTextView = findViewById(R.id.cell_Layout5);
         
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_Layout8);
         
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_Layout9);
         
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_Layout10);
         
        cellTextView.setTextSize(30);
        cellTextView = findViewById(R.id.cell_Layout11);
         
        cellTextView.setTextSize(30);

        new Thread(){

            @Override
            public void run(){
                Document doc =null;
                try{
                    String Output="";
                    String dinnerOutput="";
                    doc=Jsoup.connect(URL).get();
                    //doc.outputSettings().prettyPrint(false);
                    Element elements =doc.select(".time_normal_list").first();
                    msg=elements.text();
                    msg=msg.replaceAll("\\(","");
                    menuf=msg.split("\\s+");
                    Element date=doc.select(".cm_date").first();
                    //Output+=msg;
                    datestring=date.text();
                    datestring=datestring.replaceAll((today),"");
                    dateUI=datestring.replaceAll("점심","");
                    Output+="중식"+"\n";
                    Output+="";
                    lunchcheck=datestring.replaceAll(regex,"");

                    for (String sentence : menuf) {
                        System.out.println(sentence); // 각 단어 출력 후 줄바꿈
                        Output+=sentence+"\n";
                    }
                    elements =doc.select(".time_normal_list").get(1);
                    msg=elements.text();
                    msg=msg.replaceAll("\\(","");
                    String [] menus=msg.split("\\s+");;
                    Element date2=doc.select(".cm_date").get(1);
                    datestring=date2.text();
                    datestring=datestring.replaceAll((today),"");
                    dateUI=datestring.replaceAll("중식","석식");
                    dinnercheck=datestring.replaceAll(regex,"");
                    dinnercheck=datestring.replaceAll(regex,"");
                    if(lunchcheck.equals(dinnercheck)){
                        dinnerOutput+="석식"+"\n";
                        for (String sentence : menus) {
                            System.out.println(sentence); // 각 단어 출력 후 줄바꿈
                            dinnerOutput += sentence + "\n";
                        }
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    String finalOutput = Output;
                    String finaldOutput=dinnerOutput;
                    handler.post(new Runnable() {
                        public void run() {
                            textView.setText(finalOutput);
                            dinnertextView.setText(finaldOutput);
                        }
                    });
                    //여기부터 달력업데이트

                        Output="";
                        doc=Jsoup.connect(CalenderURL).get();
                        Elements Celements =doc.select(".tb_base_box.tm_box table.tb_calendar tbody tr");
                        if(Celements == null){
                            System.out.println("Celements is null");
                        }else{
                            System.out.println("Celements is not null");
                        }

                        msg=Celements.text();
                        msg = msg.replaceAll("\\([^\\(\\)]*\\)", "");
                        msg = msg.replaceAll("토", "");
                        msg = msg.replaceAll("일", "");
                        menuf=msg.split("\\d+");
                        List<String> menuList = new ArrayList<>();

                        for (String item : menuf) {
                            if (!item.trim().isEmpty()) {
                                menuList.add(item);
                            }
                        }
                        String[] menufWithoutEmpty = menuList.toArray(new String[0]);
                        
                        handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {
                            @Override
                            public void run(){
                                int position=1;//표시할 칸의 위치
                                int j=0;//몇주차인지 판단 이게 0~5면 1주차 6~10이면 2주차...
                                int vieweddays=0;//며칠자의 정보를 나타낼 것인지
                                for(int k=1;k<menufWithoutEmpty.length+2;k++){
                                    daystring= "day"+String.valueOf(position);//day에 표시할 칸의 좌표 합치고
                                    int ressId = getResources().getIdentifier(daystring, "id", getPackageName());//그걸로 하나의 변수로 해서
                                    TextView todaytextview=findViewById(ressId);//표시할 칸 찾음(1~표시할 문자만큼)

                                    int dayforprint=0;//표시할 날짜(첫주차:k값 2주차 k+2...)
                                    if(dayofmonth==1){//일요일이 1일이라면
                                        startday=2;
                                    }else if(dayofmonth==2){//월요일이 1일이라면
                                        startday=1;//그달 1일은 월요일
                                    }else if(dayofmonth==3){
                                        startday=2;//그달 1일은 화요일
                                    }else if(dayofmonth==4){
                                        startday=3;
                                    }else if(dayofmonth==5){
                                        startday=4;
                                    }else if(dayofmonth==6){
                                        startday=5;
                                    }else if(dayofmonth==7){
                                        startday=1;
                                    }
                                    if(0<=j&&j<5){
                                        dayforprint=j;
                                    }else if(5<=j&&j<=10){
                                        dayforprint=j+2;
                                    }else if(10<=j&&j<=15){
                                        dayforprint=j+4;
                                    }else if(15<=j&&j<=20){
                                        dayforprint=j+6;
                                    }else if(20<=j&&j<=25){
                                        dayforprint=j+8;
                                    }else if(25<=j&&j<=30){
                                        dayforprint=j+10;
                                    }
                                    if(startday>position){
                                        todaytextview.setText(" ");
                                        todaytextview.setTextSize(30);
                                        int COLOR=Color.parseColor("#FFFFFF");
                                        todaytextview.setTextColor(COLOR);
                                    }else{
                                        if(menufWithoutEmpty[k-2].substring(0,2).equals(" 월")||menufWithoutEmpty[k-2].substring(0,2).equals(" 화")||menufWithoutEmpty[k-2].substring(0,2).equals(" 수")||menufWithoutEmpty[k-2].substring(0,2).equals(" 목")||menufWithoutEmpty[k-2].substring(0,2).equals(" 금")){
                                            todaytextview.setText(" "+dayforprint+" "+menufWithoutEmpty[k-2].substring(0,2)+"\n"+menufWithoutEmpty[k-2].substring(2));
                                            todaytextview.setTextSize(25);
                                            todaytextview.setGravity(Gravity.CENTER);
                                            int COLOR=Color.parseColor("#FFFFFF");
                                            todaytextview.setTextColor(COLOR);
                                        }else{
                                            todaytextview.setText(" "+dayforprint+"\n"+menufWithoutEmpty[k-2].substring(1));
                                            todaytextview.setTextSize(25);
                                            todaytextview.setGravity(Gravity.CENTER);
                                            int COLOR=Color.parseColor("#FFFFFF");
                                            todaytextview.setTextColor(COLOR);
                                        }

                                    }

                                    position++;
                                    j++;
                                    vieweddays++;
                                }for(int k=menufWithoutEmpty.length+2;k<31;k++){

                                    daystring= "day"+String.valueOf(position);//day에 표시할 칸의 좌표 합치고
                                    int ressId = getResources().getIdentifier(daystring, "id", getPackageName());//그걸로 하나의 변수로 해서
                                    TextView todaytextview=findViewById(ressId);//표시할 칸 찾음(1~표시할 문자만큼)todaytextview.setText(" ");
                                    todaytextview.setTextSize(30);
                                    int COLOR=Color.parseColor("#FFFFFF");
                                    todaytextview.setTextColor(COLOR);
                                    todaytextview.setText(" ");
                                    position++;
                                }
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
                        timerTextView.setText(String.format("%02d:%02d", hour, minute));
                        if(timevalue>=490&&timevalue<540){//8시 10분 1교시
                            int timepercentage=(timevalue-490)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 1교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=540&&timevalue<550){
                            outputtime="2";
                            int timepercentage=(timevalue-540)*100/10;
                            if(dat.equals("월요일")){
                                outputdate="Monday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();

                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 다음시간:%s", hour,minute,name));
                        }else if(timevalue>=550&&timevalue<600){
                            int timepercentage=(timevalue-550)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 2교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=600&&timevalue<610){
                            outputtime="3";
                            int timepercentage=(timevalue-600)*100/10;
                            if(dat.equals("월요일")){
                                outputdate="Monday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 다음시간:%s", hour, minute,name));
                        }else if(timevalue>=610&&timevalue<660){
                            int timepercentage=(timevalue-610)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 3교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=660&&timevalue<670){
                            outputtime="4";
                            int timepercentage=(timevalue-660)*100/10;
                            if(dat.equals("월요일")){
                                outputdate="Monday";;
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 다음시간:%s", hour, minute,name));
                        }else if(timevalue>=670&&timevalue<720){
                            int timepercentage=(timevalue-670)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 4교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=720&&timevalue<780){
                            outputtime="7";
                            int timepercentage=(timevalue-720)*100/60;
                            if(dat.equals("월요일")){
                                outputdate="Monday";;
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 점심시간 다음시간:%s", hour, minute,name));
                        }else if(timevalue>=780&&timevalue<830){
                            int timepercentage=(timevalue-780)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 5교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=830&&timevalue<840){
                            outputtime="7";
                            int timepercentage=(timevalue-830)*100/10;
                            if(dat.equals("월요일")){
                                outputdate="Monday";;
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 다음시간:%s", hour, minute,name));
                        }else if(timevalue>=840&&timevalue<890){
                            int timepercentage=(timevalue-840)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 6교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=890&&timevalue<910){
                            outputtime="7";
                            int timepercentage=(timevalue-890)*100/20;
                            if(dat.equals("월요일")){
                                outputdate="Monday";;
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("화요일")){
                                outputdate="Tuesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("수요일")){
                                outputdate="Wednesday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("목요일")){
                                outputdate="Thursday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }else if(dat.equals("금요일")){
                                outputdate="Friday";
                                cellnum="cell_"+outputdate+outputtime;
                                int resId = getResources().getIdentifier(cellnum, "id", getPackageName());
                                cellTextView = findViewById(resId);
                                name= cellTextView.getText().toString();
                            }
                            timerTextView.setText(String.format("%02d:%02d 시간 다음시간:%s", hour, minute,name));
                        }else if(timevalue>=910&&timevalue<960){
                            int timepercentage=(timevalue-910)*100/50;
                            timerTextView.setText(String.format("%02d:%02d 7교시 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=960&&timevalue<970){
                            int timepercentage=(timevalue-960)*100/10;
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=970&&timevalue<1080){
                            int timepercentage=(timevalue-970)*100/110;
                            timerTextView.setText(String.format("%02d:%02d 오후자습 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1080&&timevalue<1140){
                            int timepercentage=(timevalue-1080)*100/60;
                            timerTextView.setText(String.format("%02d:%02d 석식시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1140&&timevalue<1230){
                            int timepercentage=(timevalue-1140)*100/90;
                            timerTextView.setText(String.format("%02d:%02d 야자 1 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1230&&timevalue<1240){
                            int timepercentage=(timevalue-1230)*100/10;
                            timerTextView.setText(String.format("%02d:%02d 쉬는시간 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1240&&timevalue<1320){
                            int timepercentage=(timevalue-1240)*100/80;
                            timerTextView.setText(String.format("%02d:%02d 야자 2 %d%%", hour, minute,timepercentage));
                        }else if(timevalue>=1320){
                            timerTextView.setText(String.format("%02d:%02d 집가 이제...", hour, minute));
                        }

                    }
                });
            }
        }, 0, 5);
    }
    public void fillcalendar(){


    }
}