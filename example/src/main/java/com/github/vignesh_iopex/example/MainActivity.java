package com.github.vignesh_iopex.example;

import android.app.Activity;
import android.os.Bundle;

import com.github.vignesh_iopex.flanklocation.Recon;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btn_locate) void locateMe() {
    Recon.using(this).start(BgTask.class);
  }

  @OnClick(R.id.btn_stop) void stopUpdates() {
    Recon.using(this).stop(BgTask.class);
  }
}
