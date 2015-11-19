package com.github.vignesh_iopex.example;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.github.vignesh_iopex.flanklocation.Flank;
import com.google.android.gms.location.LocationRequest;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  private LocationRequest getLocationRequest() {
    LocationRequest request = new LocationRequest();
    request.setFastestInterval(3000);
    request.setInterval(5000);
    request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return request;
  }

  private PendingIntent getLocationTaskIntent() {
    return PendingIntent.getService(this, 0,
        new Intent(this, BgTask.class), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  @OnClick(R.id.btn_locate) void locateMe() {
    Flank.Builder flankBuilder = new Flank.Builder();
    flankBuilder.setRequest(getLocationRequest())
        .periodic()
        .callback(getLocationTaskIntent()).flank(this);
  }

  @OnClick(R.id.btn_stop) void stopUpdates() {
    Flank.Builder flankBuilder = new Flank.Builder();
    flankBuilder.callback(getLocationTaskIntent()).stop().flank(this);
  }
}
