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
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;

import static com.google.android.gms.location.FusedLocationProviderApi.KEY_LOCATION_CHANGED;

public abstract class FlankTask extends IntentService {
  private static final String EXTRAS_CONNECTION_RESULT = "extras_connection_result";
  private Intent intent;

  public FlankTask(String name) {
    super(name);
  }

  static Intent getFailureAsExtras(ConnectionResult connectionResult) {
    Intent failureExtras = new Intent();
    failureExtras.putExtra(EXTRAS_CONNECTION_RESULT, connectionResult);
    return failureExtras;
  }

  @Override protected final void onHandleIntent(Intent intent) {
    this.intent = intent;
    ConnectionResult connectionResult = intent.getParcelableExtra(EXTRAS_CONNECTION_RESULT);
    if (connectionResult != null) {
      onConnectionError(connectionResult);
      return;
    }

    Location location = intent.getParcelableExtra(KEY_LOCATION_CHANGED);
    onNextLocation(location);
  }

  protected Intent getIntent() {
    return intent;
  }

  protected abstract void onConnectionError(@Nullable ConnectionResult connectionResult);

  protected abstract void onNextLocation(@Nullable Location location);
}
