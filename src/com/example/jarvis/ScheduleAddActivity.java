package com.example.jarvis;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ScheduleAddActivity extends Activity {
        
        private TextView saveText;  
        private static final String[] rep={"不重复","每天","每工作日","每周","每两周","每月","每年"};  
        private static final String[] rem={"不提醒","事件发生时","15分钟前","30分钟前","一小时前","一天前"};
    private Spinner spinnerrep;
    private Spinner spinnerrem;
    private ArrayAdapter<String> adapterrep;  
    private ArrayAdapter<String> adapterrem;
    //设置保存各个参数的变量

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedule_add);         
                //调用读取各个参数的函数，设置默认值
                
                //处理重复下拉框 
        spinnerrep = (Spinner) findViewById(R.id.repeat);  
        adapterrep = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rep);  
        adapterrep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinnerrep.setAdapter(adapterrep);  
        spinnerrep.setOnItemSelectedListener(new SpinnerSelectedListener());  
        spinnerrep.setVisibility(View.VISIBLE);  
        
                //处理提醒下拉框 
        spinnerrem = (Spinner) findViewById(R.id.reminder);  
        adapterrem = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rem);  
        adapterrem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinnerrem.setAdapter(adapterrem);  
        spinnerrem.setOnItemSelectedListener(new SpinnerSelectedListener());  
        spinnerrem.setVisibility(View.VISIBLE);  
        
                //处理save
                saveText=(TextView)this.findViewById(R.id.save);
                saveText.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
            //各变量=从各个控件处get参数
                        //调用存入数据库函数将各变量存入数据库
                        }                       
                });
          
    }  

    class SpinnerSelectedListener implements OnItemSelectedListener{    
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
        }    
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }           
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.schedule_add, menu);
                return true;
        }

}