package com.github.vignesh_iopex.example;

import android.app.NotificationManager;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.github.vignesh_iopex.flanklocation.FlankTask;
import com.google.android.gms.common.ConnectionResult;

public class BgTask extends FlankTask {

  public BgTask() {
    super("BackgroundLocationTask");
  }

  @Override protected void onConnectionError(ConnectionResult connectionResult) {

  }

  @Override protected void onNextLocation(Location location) {
    String content = "No location";
    if (location != null)
      content = location.toString();
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setSmallIcon(R.drawable.notification_template_icon_bg)
        .setContentTitle("Location Update")
        .setContentText(content);
    NotificationManager notifyManager = (NotificationManager) getSystemService(
        NOTIFICATION_SERVICE);
    notifyManager.notify(1, mBuilder.build());
  }
}
