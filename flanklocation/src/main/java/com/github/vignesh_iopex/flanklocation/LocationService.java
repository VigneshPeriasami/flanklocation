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

import android.app.IntentService;
import android.content.Intent;

public final class LocationService extends IntentService {
  static final String EXTRA_FLANK = "extra_flank";
  private LocationAdapter locationAdapter;

  public LocationService() {
    super("FlankLocationService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    if (locationAdapter == null)
      locationAdapter = new DefaultLocationAdapter(this);
    if (!locationAdapter.isConnected())
      locationAdapter.connect();

    Flank flank = intent.getParcelableExtra(EXTRA_FLANK);
    locationAdapter.processFlank(flank);
  }

  @Override public void onDestroy() {
    if (locationAdapter != null)
      locationAdapter.disconnect();
    super.onDestroy();
  }
}
