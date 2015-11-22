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
import android.location.Location;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

final class DefaultLocationAdapter implements LocationAdapter {
  private final Context context;
  private final GoogleApiClient mGoogleApiClient;
  private ConnectionResult connectionResult;

  private LocationApiAdapter success = new LocationApiAdapter() {
    @Override public void requestUpdates(LocationRequest locationRequest, PendingIntent callback) {
      FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, callback);
    }

    @Override public void stopUpdates(PendingIntent callback) {
      FusedLocationApi.removeLocationUpdates(mGoogleApiClient, callback);
    }

    @Override public void sendLastKnownLocation(PendingIntent callback) {
      Location location = FusedLocationApi.getLastLocation(mGoogleApiClient);
      Intent locationExtras = new Intent();
      locationExtras.putExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
      try {
        callback.send(context, 0, locationExtras);
      } catch (PendingIntent.CanceledException e) {
        e.printStackTrace();
      }
    }
  };

  private LocationApiAdapter failure = new LocationApiAdapter() {
    private Intent getFailureExtras() {
      return FlankTask.getFailureAsExtras(connectionResult);
    }

    private void sendFailure(PendingIntent callback) {
      try {
        callback.send(context, -1, getFailureExtras());
      } catch (PendingIntent.CanceledException e) {
        e.printStackTrace();
      }
    }

    @Override public void requestUpdates(LocationRequest locationRequest, PendingIntent callback) {
      sendFailure(callback);
    }

    @Override public void stopUpdates(PendingIntent pendingIntent) {
      sendFailure(pendingIntent);
    }

    @Override public void sendLastKnownLocation(PendingIntent pendingIntent) {
      sendFailure(pendingIntent);
    }
  };

  DefaultLocationAdapter(Context context) {
    this(context, new GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .build());
  }

  @VisibleForTesting DefaultLocationAdapter(Context context, GoogleApiClient googleApiClient) {
    this.context = context;
    this.mGoogleApiClient = googleApiClient;
  }

  @Override public void connect() {
    connectionResult = mGoogleApiClient.blockingConnect();
  }

  @Override public boolean isConnected() {
    return mGoogleApiClient.isConnected();
  }

  private void assertConnection() {
    if (connectionResult == null)
      throw new IllegalStateException("Location adapter is not connected, call connect");
  }

  @Override public void applyAction(RequestorAction flank) {
    assertConnection();
    if (connectionResult.isSuccess()) {
      flank.onLocationApiReady(success);
    } else {
      flank.onLocationApiReady(failure);
    }
  }

  @Override public void disconnect() {
    mGoogleApiClient.disconnect();
  }
}
