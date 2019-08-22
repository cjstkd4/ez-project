package com.example.dateui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dateui.helper.DateTimeHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Detail_writeActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, OnMapReadyCallback {
    // 객체 선언
    EditText detail_write_name, detail_write_person, detail_write_help;
    Button detail_write_date, detail_write_time, detail_write_bt1, detail_write_bt2;
    int YEAR =0, MONTH =0, DAY = 0;
    int HOUR = 0, MINIUTE=0;

    GoogleMap googleMap;
    MapFragment fragment;
    LocationManager lm;                 // 위치 관리자
    boolean permissionCK = false;    // 퍼미션 결과 저장
    Marker marker;                      // 지도에 표시할 마커 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_write);
        detail_write_name = findViewById(R.id.detail_write_name);
        detail_write_person = findViewById(R.id.detail_write_person);
        detail_write_date = findViewById(R.id.detail_write_date);
        detail_write_time = findViewById(R.id.detail_write_time);
        detail_write_help = findViewById(R.id.detail_write_help);
        detail_write_bt1 = findViewById(R.id.detail_write_bt1);
        detail_write_bt2 = findViewById(R.id.detail_write_bt2);

        // Dialog에 출력하기 위해 현재 시스템 날짜를 구하여 전역변수에 미리 셋팅
        int[] date = DateTimeHelper.getInstance().getDate();
        YEAR = date[0];
        MONTH = date[1];
        DAY = date[2];
        // Dialog에 출력하기 위해 현재 시스템 시간를 구하여 전역변수에 미리 셋팅
        int[] time = DateTimeHelper.getInstance().getTime();
        HOUR = time[0];
        MINIUTE = time[1];


        // 객체 초기화
        // 줄 그은 것은 곧 사라지지만 옛 버전을 사용하기 위해서 사용하는 것이 좋다.
        // 단, 신버전의 사용 방법을 알고 있다면 그것을 사용하는 것이 좋다!
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 구글맵 초기화
        fragment.getMapAsync(this);

        detail_write_date.setOnClickListener(this);
        detail_write_time.setOnClickListener(this);
        detail_write_bt1.setOnClickListener(this);
        detail_write_bt2.setOnClickListener(this);

        // 퍼미션 체크
        permissionCheck();
    }

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            permissionCK = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        readyMap();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            날짜 Dialog
            case R.id.detail_write_date:
                showDatePickerDialog();
                break;
//          시간 Dialog
            case R.id.detail_write_time:
                showTimePickerDialog();
                break;
//                저장 버튼
            case R.id.detail_write_bt1:
                break;
//                취소 버튼
            case R.id.detail_write_bt2:
                finish();
                break;
        }
    }
//    시간 dialog
    private void showTimePickerDialog() {
        // 원본 데이터 백업
        final int temp_hh = HOUR;
        final int temp_nn = MINIUTE;
        // TimePickerDialog 객체 생성
        // TimePickerDialog(Context, 이벤트 핸들러, 시, 분, 24시간제 사용여부)
        // 24시간제 사용여부 : treu=24시간제, false=12시간제
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // 사용자가 선택한 값을 전역변수에 저장
                HOUR = hourOfDay;
                MINIUTE = minute;
                detail_write_time.setText(HOUR +"시" + MINIUTE +"분");
            }
        }, HOUR, MINIUTE, false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HOUR = temp_hh;
                MINIUTE = temp_nn;
            }
        });
        dialog.setTitle("시간 선택");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage("약속시간을 선택하세요.");
        dialog.setCancelable(false);
        dialog.show();
    }
//    날짜 dialog
    private void showDatePickerDialog() {
        // 원본 데이터 백업
        final int temp_yy = YEAR;
        final int temp_mm = MONTH;
        final int temp_dd = DAY;

        // DatePickerDialog 객체 생성
        // DatePickerDialog(Context, 이벤트 핸들러, 년, 월, 일)
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 사용자가 선택한 값을 전역변수에 저장
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;
                detail_write_date.setText(YEAR + "년 " + MONTH + "월 " + DAY + "일");
            }
        }, YEAR, MONTH, DAY);
        // 사용자가 Back 키나 "취소"를 눌렸을 대, 동작하는 이벤트 정의
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 백업해 두었던 값을 원복시킴 : 버전에 따라 좀 다르기 때문에, 이번 버전에선느 필요 없음
                YEAR = temp_yy;
                MONTH = temp_mm;
                DAY = temp_dd;

            }
        });
        Log.d("[test]", temp_yy + " / " + temp_mm + " / " + temp_dd);
        dialog.setTitle("날짜 선택");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage("생일을 선택하세요.");
        dialog.show();
    }


    // 지정된 시간, 거리마다 한번식 호출된다.
    @Override
    public void onLocationChanged(Location location) {
        //현재 위도와 경도 얻기
        double lat = location.getLatitude();    // 위도
        double lng = location.getLongitude();   // 경도
        /* 구글맵에 위치 설정하기 */
//        현재 위치 관리 객체
        LatLng position = new LatLng(lat, lng);
//        마커 표시
        if(marker == null) { //마커가 없는 경우, 새로 생성하여 지도에 추가
            MarkerOptions options = new MarkerOptions();
            options.position(position);
            marker = googleMap.addMarker(options);
        } else {
            marker.setPosition(position);
        }
//        지도 배율 설정 : zoom : 1 ~ 21 (값이 커질수록 확대)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 19);
//        현재 위치로 앱 이동
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 초기화된 구글
        this.googleMap = googleMap;
        if (permissionCK)
            readyMap();
    }

    // 위치 수신 준비
    private void readyMap() {
        // 현재 사용가능한 하드웨어 이름 얻기 : GPS, NETWORK 값을 동기에 가져오기 가능
        String provider = lm.getBestProvider(new Criteria(), true);
        if (provider == null) {
            Toast.makeText(this, "위치 정보를 사용할 상태가 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 해당 장치가 마지막으로 수신한 위치 얻기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(provider);
        if(location != null){
            onLocationChanged(location);    // 이벤트 함수 강제 호출
        }
        // 위치 정보 취득시작
        // (하드웨어 이름, 갱신시간주기, 갱신거리주기, 이벤트 핸들러)
        lm.requestLocationUpdates(provider, 1000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 수신 종료
        lm.removeUpdates(this);
    }
}
