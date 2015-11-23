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
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.location.LocationRequest;

import static com.github.vignesh_iopex.flanklocation.ActionQueue.DEFAULT_QUEUE;
import static com.github.vignesh_iopex.flanklocation.RequestParser.DEFAULT_PARSER;

public final class Recon {
  private final Context context;
  private final RequestorActionFactory requestorActionFactory;
  private final ActionQueue actionQueue;
  private final RequestParser requestParser;

  private Recon(Context context) {
    this(context, new DefaultActionFactory(), DEFAULT_QUEUE, DEFAULT_PARSER);
  }

  @VisibleForTesting Recon(Context context, RequestorActionFactory requestorActionFactory,
                           ActionQueue actionQueue, RequestParser requestParser) {
    this.context = context;
    this.requestorActionFactory = requestorActionFactory;
    this.actionQueue = actionQueue;
    this.requestParser = requestParser;
  }

  public static Recon using(@NonNull Context context) {
    return new Recon(context);
  }

  /**
   * Start Periodic updates using passed in LocationRequest and callback.
   * This can still use {@link ReconTask} as callback
   */
  public void start(@NonNull LocationRequest locationRequest, @NonNull PendingIntent callback) {
    addToQueue(requestorActionFactory.getActionToStart(locationRequest, callback));
  }

  /**
   * Gets the last known location to the callback
   */
  public void startOneShot(@NonNull PendingIntent callback) {
    addToQueue(requestorActionFactory.getActionToOneShotStart(callback));
  }

  /**
   * Extracts location request from
   * {@link com.github.vignesh_iopex.flanklocation.annotations.FlankLocation} annotation and
   * uses default pending intent construction for callback
   */
  public void start(@NonNull Class<? extends ReconTask> clsFlank) {
    start(clsFlank, requestParser.getPendingIntent(context, clsFlank));
  }

  /**
   * Starts the update by using the custom PendingIntent
   * and uses the {@link com.github.vignesh_iopex.flanklocation.annotations.FlankLocation}
   * annotation to extract LocationRequest information
   */
  public void start(@NonNull Class<? extends ReconTask> clsFlank,
                    @NonNull PendingIntent callback) {
    start(requestParser.getLocationRequest(clsFlank), callback);
  }

  /**
   * Stops the location updates by using the default pending intent for the given class
   */
  public void stop(@NonNull Class<? extends ReconTask> clsFlank) {
    stop(requestParser.getPendingIntent(context, clsFlank));
  }

  /** Stops the location updates for the given pending intent */
  public void stop(@NonNull PendingIntent whichCallback) {
    addToQueue(requestorActionFactory.getActionToStop(whichCallback));
  }

  private void addToQueue(RequestorAction action) {
    actionQueue.enqueue(action);
    context.startService(new Intent(context, ConnectorService.class));
  }

  @VisibleForTesting ActionQueue getActionQueue() {
    return actionQueue;
  }
}
