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

class DefaultParserFactory implements RequestParserFactory {

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
    return new RequestorAction() {
      private LocationRequest request;
      private PendingIntent callback;

      @Override public void onLocationApiReady(LocationRequestor requestor) {
        requestor.requestUpdates(request, callback);
      }

      RequestorAction init(LocationRequest request, PendingIntent callback) {
        this.request = request;
        this.callback = callback;
        return this;
      }
    }.init(locationRequest, callback);
  }

  private RequestorAction stopPeriodicUpdates(PendingIntent callback) {
    return new RequestorAction() {
      private PendingIntent callback;

      @Override public void onLocationApiReady(LocationRequestor requestor) {
        requestor.stopUpdates(callback);
      }

      private RequestorAction init(PendingIntent callback) {
        this.callback = callback;
        return this;
      }
    }.init(callback);
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
}
