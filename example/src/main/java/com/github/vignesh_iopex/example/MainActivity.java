package com.github.vignesh_iopex.example;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.github.vignesh_iopex.flanklocation.Recon;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
  private static final int PERMISSION_FINE_LOCATION = 1;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  private void registerForLocationUpdates() {
    Recon.using(this).start(BgTask.class);
  }

  @OnClick(R.id.btn_locate) void locateMe() {
    checkPermissionAndLocate();
  }

  @OnClick(R.id.btn_stop) void stopUpdates() {
    Recon.using(this).stop(BgTask.class);
  }

  @TargetApi(Build.VERSION_CODES.M)
  private void checkPermissionAndLocate() {
    int permissionCheck = ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION);
    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
      registerForLocationUpdates();
      return;
    }

    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_FINE_LOCATION)) {
      new AlertDialog.Builder(this).setTitle("Permission Request")
          .setMessage("Need access to location to start location updates")
          .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
              ActivityCompat.requestPermissions(MainActivity.this,
                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                  PERMISSION_FINE_LOCATION);
            }
          })
          .setNegativeButton("No thanks", null)
          .show();
    } else {
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          PERMISSION_FINE_LOCATION);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                   int[] grantResults) {
    if (requestCode == PERMISSION_FINE_LOCATION) {
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        registerForLocationUpdates();
      } else {
        Toast.makeText(this, "You moron this example requires Location permission", Toast
            .LENGTH_SHORT).show();
      }
    }
  }
}
