package com.example.matta;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private String URL ="https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=대덕고";
    String msg;
    String datestring;
    String lunchcheck;
    String dinnercheck;
    String regex="[^0-9]";
    final Bundle bundle=new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        textView.setTextSize(40);
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
                    Output+="\n"+datestring+"\n";
                    dinnercheck=datestring.replaceAll(regex,"");

                    if(lunchcheck.equals(dinnercheck)){
                        for (String sentence : menus) {
                            System.out.println(sentence); // 각 단어 출력 후 줄바꿈
                            Output += sentence + "\n";
                        }

                    }

                    textView.setText(Output);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}