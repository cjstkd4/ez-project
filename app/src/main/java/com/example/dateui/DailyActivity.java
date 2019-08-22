package com.example.dateui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;


import com.example.dateui.Detail_writeActivity;
import com.example.dateui.R;

import java.util.Calendar;

public class DailyActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        //  이벤트
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        // 커스텀 다이얼로그에 Date를 보내기 위해서 작성
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "";
        switch (week) {
            case 1:
                dayOfWeek = "일요일";
                break;
            case 2:
                dayOfWeek = "월요일";
                break;
            case 3:
                dayOfWeek = "화요일";
                break;
            case 4:
                dayOfWeek = "수요일";
                break;
            case 5:
                dayOfWeek = "목요일";
                break;
            case 6:
                dayOfWeek = "금요일";
                break;
            case 7:
                dayOfWeek = "토요일";
                break;
        }
        showDetailDialog(year, month, dayOfMonth, dayOfWeek);
    }

    //   달력을 눌렀을 때 다이얼로그를 띄어 줌으로써 해당 날짜를 자세하게 볼 수 있는 기능을 추가
    private void showDetailDialog(int year, int month, int dayOfMonth, String dayOfWeek) {
        final View detailDialog = getLayoutInflater().inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(detailDialog);

        // 날짜 객체 초기화
        TextView Detail_day = detailDialog.findViewById(R.id.Detail_day);
        TextView Detail_year = detailDialog.findViewById(R.id.Detail_year);
        TextView Detail_month = detailDialog.findViewById(R.id.Detail_month);
        TextView Detail_week = detailDialog.findViewById(R.id.Detail_week);

        // 날자 객체 설정
        Detail_day.setText(Integer.toString(dayOfMonth) + "일");
        Detail_year.setText(Integer.toString(year) + "년");
        Detail_month.setText(Integer.toString(month) + "월");
        Detail_week.setText(dayOfWeek);

        builder.setPositiveButton("추가 하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DailyActivity.this, Detail_writeActivity.class);
                startActivity(intent);
            }
        });

        builder.show();
    }
}