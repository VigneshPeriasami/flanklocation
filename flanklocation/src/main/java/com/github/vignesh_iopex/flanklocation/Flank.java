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

import java.util.LinkedList;
import java.util.List;

public final class Flank {
  private static final List<RequestorAction> ACTION_LIST = new LinkedList<>();
  private final Context context;
  private final RequestParserFactory requestParserFactory;

  private Flank(Context context) {
    this(context, new DefaultParserFactory());
  }

  Flank(Context context, RequestParserFactory requestParserFactory) {
    this.context = context;
    this.requestParserFactory = requestParserFactory;
  }

  public static Flank using(Context context) {
    return new Flank(context);
  }

  public void start(Class<? extends FlankTask> clsFlank) {
    pushAndStart(requestParserFactory.forStart(context, clsFlank));
  }

  public void start(Class<? extends FlankTask> clsFlank, PendingIntent callback) {
    pushAndStart(requestParserFactory.forStart(clsFlank, callback));
  }

  public void stop(Class<? extends FlankTask> clsFlank) {
    pushAndStart(requestParserFactory.forStop(context, clsFlank));
  }

  private void pushAndStart(RequestorAction requestorAction) {
    synchronized (ACTION_LIST) {
      ACTION_LIST.add(requestorAction);
    }
    context.startService(new Intent(context, ApiConnector.class));
  }

  static void informAllRequestors(LocationAdapter locationAdapter) {
    synchronized (ACTION_LIST) {
      for (RequestorAction requestorAction : ACTION_LIST) {
        locationAdapter.applyAction(requestorAction);
      }
      ACTION_LIST.clear();
    }
  }
}
