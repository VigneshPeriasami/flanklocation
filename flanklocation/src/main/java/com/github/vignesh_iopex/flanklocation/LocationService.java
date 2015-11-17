/*
 * Copyright 2015 Vignesh Periasami
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.vignesh_iopex.flanklocation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public final class LocationService extends Service {
  static final String EXTRA_FLANK = "extra_flank";
  private LocationAdapter locationAdapter;

  @Override public void onCreate() {
    synchronized (this) {
      if (locationAdapter == null) {
        locationAdapter = new DefaultLocationAdapter(this);
        locationAdapter.open();
      }
    }
    super.onCreate();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Flank flank = intent.getParcelableExtra(EXTRA_FLANK);
    if (flank == null)
      return super.onStartCommand(intent, flags, startId);
    locationAdapter.processFlankWhenReady(flank);
    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
