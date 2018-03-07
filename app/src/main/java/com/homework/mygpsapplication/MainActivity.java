package com.homework.mygpsapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LocationManager lm;
    private TextView longitude,latitude,speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = (TextView)findViewById(R.id.longitude);
        latitude = (TextView)findViewById(R.id.latitude);
        speed = (TextView)findViewById(R.id.speed);



        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(lm)) {
            Toast.makeText(MainActivity.this, "OPEN GPS~", Toast.LENGTH_SHORT).show();
            openGPS2();
        }
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            //Get GPS latest COORDINATE data
            Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateShow(lc);
            //set up 2 seconds as interval to get GPS data 设置间隔两秒获得一次GPS定位信息
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Update the coordinate when GPS data changed 当GPS定位信息发生改变时，更新定位
                    updateShow(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Update the coordinate when GPS LocationProvider available  当GPS LocationProvider可用时，更新定位
                    if(ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        updateShow(lm.getLastKnownLocation(provider));
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {
                    updateShow(null);
                }
            });
        }

    }




    //Define a display method 定义一个更新显示的方法
    private void updateShow(Location location) {
        if (location != null) {
            longitude.setText("longitude：" + location.getLongitude());
            latitude.setText("latitude：" + location.getLatitude());
            speed.setText("speed:" + location.getSpeed());
//            StringBuilder sb = new StringBuilder();
//            sb.append("当前的位置信息：\n");
//            sb.append("精度：" + location.getLongitude() + "\n");
//            sb.append("纬度：" + location.getLatitude() + "\n");
//            sb.append("高度：" + location.getAltitude() + "\n");
//            sb.append("速度：" + location.getSpeed() + "\n");
//            sb.append("方向：" + location.getBearing() + "\n");
//            sb.append("定位精度：" + location.getAccuracy() + "\n");
            //tv_show.setText(sb.toString());
        } else{
            longitude.setText("longitude：" );
            latitude.setText("latitude：");
        }
    }


    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }


    //打开设置页面让用户自己设置
    private void openGPS2() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }
}
