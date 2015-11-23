package com.github.vignesh_iopex.example;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.github.vignesh_iopex.flanklocation.ReconTask;
import com.github.vignesh_iopex.flanklocation.annotations.FlankLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationResult;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

@FlankLocation(interval = 3000, priority = PRIORITY_HIGH_ACCURACY, fastestInterval = 2000)
public class BgTask extends ReconTask {

  public BgTask() {
    super("BackgroundLocationTask");
  }

  @Override protected void onConnectionError(ConnectionResult connectionResult) {

  }

  @Override protected void onNextLocation(LocationResult location) {
    String content = "No location";
    if (location != null)
      content = location.getLastLocation().toString();
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setSmallIcon(R.drawable.notification_template_icon_bg)
        .setContentTitle("Location Update")
        .setContentText(content);
    NotificationManager notifyManager = (NotificationManager) getSystemService(
        NOTIFICATION_SERVICE);
    notifyManager.notify(1, mBuilder.build());
  }
}
