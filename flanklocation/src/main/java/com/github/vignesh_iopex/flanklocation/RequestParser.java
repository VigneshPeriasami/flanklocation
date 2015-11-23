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

import com.github.vignesh_iopex.flanklocation.annotations.FlankLocation;
import com.google.android.gms.location.LocationRequest;

import java.lang.annotation.Annotation;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

interface RequestParser {
  @NonNull LocationRequest getLocationRequest(Class<? extends ReconTask> clsFlank);

  @NonNull PendingIntent getPendingIntent(Context context, Class<? extends ReconTask> clsFlank);

  RequestParser DEFAULT_PARSER = new RequestParser() {
    @Override
    @NonNull
    public LocationRequest getLocationRequest(Class<? extends ReconTask> clsFlank) {
      return extractLocationRequest(clsFlank);
    }

    private LocationRequest extractLocationRequest(Class<? extends ReconTask> clsFlankTask) {
      LocationRequest locationRequest = null;
      for (Annotation annotation : clsFlankTask.getAnnotations()) {
        if (annotation instanceof FlankLocation) {
          locationRequest = extractLocationRequest((FlankLocation) annotation);
        }
      }
      return locationRequest;
    }

    private LocationRequest extractLocationRequest(FlankLocation flankLocation) {
      LocationRequest locationRequest = new LocationRequest();
      locationRequest.setInterval(flankLocation.interval());
      locationRequest.setFastestInterval(flankLocation.fastestInterval());
      locationRequest.setPriority(flankLocation.priority());

      if (flankLocation.maxWaitTime() > 0)
        locationRequest.setMaxWaitTime(flankLocation.maxWaitTime());
      if (flankLocation.numUpdates() > 0)
        locationRequest.setNumUpdates(flankLocation.numUpdates());
      if (flankLocation.smallestDisplacement() > 0)
        locationRequest.setSmallestDisplacement(flankLocation.smallestDisplacement());
      if (flankLocation.duration() > 0)
        locationRequest.setExpirationDuration(flankLocation.duration());

      return locationRequest;
    }

    @Override
    @NonNull
    public PendingIntent getPendingIntent(Context context,
                                                    Class<? extends ReconTask> clsFlank) {
      Intent callIntent = new Intent(context, clsFlank);
      return PendingIntent.getService(context, 0, callIntent, FLAG_UPDATE_CURRENT);
    }
  };
}
