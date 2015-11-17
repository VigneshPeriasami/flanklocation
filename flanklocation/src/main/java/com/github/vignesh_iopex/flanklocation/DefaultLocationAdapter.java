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
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.LinkedList;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

class DefaultLocationAdapter implements LocationAdapter,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationApiAdapter {
  private final Context context;
  private GoogleApiClient mGoogleApiClient;
  private final LinkedList<Flank> pendingQueues = new LinkedList<>();

  DefaultLocationAdapter(Context context) {
    this.context = context;
    mGoogleApiClient = new GoogleApiClient.Builder(context)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override public void open() {
    mGoogleApiClient.connect();
  }

  @Override public boolean isReady() {
    return mGoogleApiClient.isConnected();
  }

  @Override public void processFlankWhenReady(@NonNull Flank flank) {
    if (!isReady() && mGoogleApiClient.isConnecting()) {
      synchronized (pendingQueues) {
        pendingQueues.add(flank);
      }
      return;
    }
    flank.onLocationApiReady(this);
  }

  private void processPendingQueues() {
    synchronized (pendingQueues) {
      for (Flank flank : pendingQueues) {
        flank.onLocationApiReady(this);
      }
    }
  }

  @Override public void close() {
    mGoogleApiClient.disconnect();
  }

  @Override public void onConnected(Bundle bundle) {
    processPendingQueues();
  }

  @Override public void onConnectionSuspended(int i) {
    // pending
  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {
    // pending
  }

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
      callback.send(context, Flank.TYPE_FORCE_ONE, locationExtras);
    } catch (PendingIntent.CanceledException e) {
      e.printStackTrace();
    }
  }
}
