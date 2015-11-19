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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationRequest;

public final class Flank implements Parcelable {
  static final int TYPE_FORCE_ONE = 1;
  static final int TYPE_PERIODIC = 2;
  static final int TYPE_STOP = 0;

  private final LocationRequest locationRequest;
  private final PendingIntent callback;
  private final int updateType;

  Flank(LocationRequest locationRequest, PendingIntent callback, int updateType) {
    this.locationRequest = locationRequest;
    this.callback = callback;
    this.updateType = updateType;
  }

  private Flank(Parcel source) {
    this.locationRequest = source.readParcelable(Flank.class.getClassLoader());
    this.callback = source.readParcelable(Flank.class.getClassLoader());
    this.updateType = source.readInt();
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeParcelable(locationRequest, i);
    parcel.writeParcelable(callback, i);
    parcel.writeInt(updateType);
  }

  @Override public int describeContents() {
    return 0;
  }

  void onLocationApiReady(LocationRequestor locationApi) {
    if (TYPE_FORCE_ONE == updateType) {
      locationApi.sendLastKnownLocation(callback);
    } else if (TYPE_PERIODIC == updateType) {
      locationApi.requestUpdates(locationRequest, callback);
    } else {
      locationApi.stopUpdates(callback);
    }
  }

  public static final Creator<Flank> CREATOR = new Creator<Flank>() {
    @Override public Flank createFromParcel(Parcel parcel) {
      return new Flank(parcel);
    }

    @Override public Flank[] newArray(int i) {
      return new Flank[i];
    }
  };

  public static class Builder {
    private LocationRequest locationRequest;
    private int updateType;
    private PendingIntent pendingIntent;

    /**
     * LocationRequest is only required for periodic updates.
     */
    public Builder setRequest(@Nullable LocationRequest locationRequest) {
      this.locationRequest = locationRequest;
      return this;
    }

    /**
     * Get one shot update (FORCED)
     * LocationRequest is not required to get an one shot update
     */
    public Builder oneShot() {
      updateType = TYPE_FORCE_ONE;
      return this;
    }

    /**
     * Uses {@link LocationRequest} from {@link #setRequest(LocationRequest)} to get updates
     */
    public Builder periodic() {
      updateType = TYPE_PERIODIC;
      return this;
    }

    /**
     * stop the location updates for the PendingIntent which has been used as callback in
     * periodic updates request.
     */
    public Builder stop() {
      updateType = TYPE_STOP;
      return this;
    }

    /** Use {@link FlankTask} for clean callback api methods. */
    public Builder callback(@NonNull PendingIntent pendingIntent) {
      this.pendingIntent = pendingIntent;
      return this;
    }

    public void flank(@NonNull Context context) {
      Flank flank = new Flank(locationRequest, pendingIntent, updateType);
      Intent intent = new Intent(context, LocationService.class);
      intent.putExtra(LocationService.EXTRA_FLANK, flank);
      context.startService(intent);
    }
  }
}
