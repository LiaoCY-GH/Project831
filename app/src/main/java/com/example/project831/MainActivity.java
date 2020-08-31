package com.example.project831;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    //计时器
    private Chronometer ch;
    private Button start;
    private Button pause;
    private Button restart;
    //储存
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取计时器组件
        ch = findViewById(R.id.test);
        //获取开始按钮
        start = findViewById(R.id.start);
        //暂停计时按钮
        pause = findViewById(R.id.pause);
        //继续计时按钮
        restart = findViewById(R.id.go_on);
        //储存输入内容
        edt = findViewById(R.id.edt_send);

        /*
         *计时器
         */
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置开始计时时间
                ch.setBase(SystemClock.elapsedRealtime());
                //启动计时器
                ch.start();
                pause.setEnabled(true);
                restart.setEnabled(false);
                start.setEnabled(false);
            }
        });
        //暂停按钮监听器
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("重新开始");
                ch.stop();
                start.setEnabled(true);
                restart.setEnabled(true);
                pause.setEnabled(false);
            }
        });
        //暂停按钮监听器
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("重新开始");
                ch.start();
                start.setEnabled(true);
                pause.setEnabled(true);
                restart.setEnabled(false);
            }
        });
        //绑定事件监听器
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                //如果计时到现在超过了一小时秒
                if (SystemClock.elapsedRealtime() - ch.getBase() > 3600 * 1000) {
                    ch.stop();
                    start.setEnabled(true);
                    restart.setEnabled(false);
                    pause.setEnabled(false);
                }
            }
        });
    }

    /*
     *获取储存的dialog
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edt.getText().toString().trim();
        save(inputText);
    }

    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("date", MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}