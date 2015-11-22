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
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.location.LocationRequest;

import static com.github.vignesh_iopex.flanklocation.ActionQueue.DEFAULT_QUEUE;

public final class Flank {
  private final Context context;
  private final RequestorActionFactory requestorActionFactory;
  private final ActionQueue actionQueue;

  private Flank(Context context) {
    this(context, new DefaultActionFactory(), DEFAULT_QUEUE);
  }

  @VisibleForTesting Flank(Context context, RequestorActionFactory requestorActionFactory,
                           ActionQueue actionQueue) {
    this.context = context;
    this.requestorActionFactory = requestorActionFactory;
    this.actionQueue = actionQueue;
  }

  public static Flank using(Context context) {
    return new Flank(context);
  }

  public void startPeriodic(PendingIntent pendingIntent, LocationRequest locationRequest) {

  }

  public void startOneShot(PendingIntent pendingIntent) {

  }

  public void start(Class<? extends FlankTask> clsFlank) {
    addToQueue(context, requestorActionFactory.forStart(context, clsFlank));
  }

  public void start(Class<? extends FlankTask> clsFlank, PendingIntent callback) {
    addToQueue(context, requestorActionFactory.forStart(clsFlank, callback));
  }

  public void stop(Class<? extends FlankTask> clsFlank) {
    addToQueue(context, requestorActionFactory.forStop(context, clsFlank));
  }

  private void addToQueue(Context context, RequestorAction action) {
    actionQueue.enqueue(action);
    context.startService(new Intent(context, ApiConnector.class));
  }
}
