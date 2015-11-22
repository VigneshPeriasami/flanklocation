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
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.vignesh_iopex.flanklocation.annotations.Periodic;
import com.google.android.gms.location.LocationRequest;

import java.lang.annotation.Annotation;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

class DefaultActionFactory implements RequestorActionFactory {

  @Override public RequestorAction forStart(Context context,
                                            @NonNull Class<? extends FlankTask> clsFlankTask) {
    return forStart(clsFlankTask, getDefaultPendingIntent(context, clsFlankTask));
  }

  @Override public RequestorAction forStart(@NonNull Class<? extends FlankTask> clsFlankTask,
                                            @Nullable PendingIntent callback) {
    return startPeriodicRequest(clsFlankTask, callback);
  }

  @Override public RequestorAction forStop(@NonNull PendingIntent callback) {
    return stopPeriodicUpdates(callback);
  }

  @Override public RequestorAction forStop(Context context,
                                           Class<? extends FlankTask> clsFlankTask) {
    return forStop(getDefaultPendingIntent(context, clsFlankTask));
  }

  private PendingIntent getDefaultPendingIntent(Context context,
                                                Class<? extends FlankTask> clsFlankTask) {
    Intent callIntent = new Intent(context, clsFlankTask);
    return PendingIntent.getService(context, 0, callIntent, FLAG_UPDATE_CURRENT);
  }

  private RequestorAction startPeriodicRequest(Class<? extends FlankTask> clsFlankTask,
                                               final PendingIntent callback) {
    LocationRequest locationRequest = extractLocationRequest(clsFlankTask);
    return new PeriodicStartAction(locationRequest, callback);
  }

  private RequestorAction stopPeriodicUpdates(PendingIntent callback) {
    return new StopAction(callback);
  }

  private LocationRequest extractLocationRequest(Class<? extends FlankTask> clsFlankTask) {
    LocationRequest locationRequest = null;
    for (Annotation annotation : clsFlankTask.getAnnotations()) {
      if (annotation instanceof Periodic) {
        locationRequest = extractLocationRequest((Periodic) annotation);
      }
    }
    return locationRequest;
  }

  private LocationRequest extractLocationRequest(Periodic periodic) {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(periodic.interval());
    locationRequest.setFastestInterval(periodic.fastestInterval());
    locationRequest.setPriority(periodic.priority());
    return locationRequest;
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
}
