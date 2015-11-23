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

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;

class DefaultActionFactory implements RequestorActionFactory {

  @Override public RequestorAction getActionToStart(@NonNull LocationRequest locationRequest,
                                                    @NonNull PendingIntent callback) {
    return new PeriodicStartAction(locationRequest, callback);
  }

  @Override public RequestorAction getActionToOneShotStart(@NonNull PendingIntent callback) {
    return new OneShotAction(callback);
  }

  @Override public RequestorAction getActionToStop(@NonNull PendingIntent callback) {
    return stopPeriodicUpdates(callback);
  }

  private RequestorAction stopPeriodicUpdates(@NonNull PendingIntent callback) {
    return new StopAction(callback);
  }

  private class StopAction implements RequestorAction {
    private final PendingIntent callback;

    StopAction(PendingIntent callback) {
      this.callback = callback;
    }

    @Override public void onLocationApiReady(LocationApiAdapter apiAdapter) {
      apiAdapter.stopUpdates(callback);
    }
  }

  private class PeriodicStartAction implements RequestorAction {
    private final LocationRequest locationRequest;
    private final PendingIntent callback;

    PeriodicStartAction(LocationRequest locationRequest, PendingIntent callback) {
      this.locationRequest = locationRequest;
      this.callback = callback;
    }

    @Override public void onLocationApiReady(LocationApiAdapter apiAdapter) {
      apiAdapter.requestUpdates(locationRequest, callback);
    }
  }

  private class OneShotAction implements RequestorAction {
    private final PendingIntent callback;

    OneShotAction(PendingIntent callback) {
      this.callback = callback;
    }

    @Override public void onLocationApiReady(LocationApiAdapter apiAdapter) {
      apiAdapter.sendLastKnownLocation(callback);
    }
  }
}
