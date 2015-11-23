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
import android.content.Intent;

import com.google.android.gms.location.LocationRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 18, constants = BuildConfig.class)
public class DefaultActionFactoryTest {

  private PendingIntent getDummyPendingIntent() {
    return PendingIntent.getService(RuntimeEnvironment.application.getApplicationContext(), 0,
        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  @Test public void requestPeriodicUpdates() {
    DefaultActionFactory defaultActionFactory = new DefaultActionFactory();
    LocationRequest locationRequest = new LocationRequest();
    PendingIntent callback = getDummyPendingIntent();
    RequestorAction requestorAction = defaultActionFactory
        .getActionToStart(locationRequest, callback);
    LocationApiAdapter apiAdapter = mock(LocationApiAdapter.class);
    requestorAction.onLocationApiReady(apiAdapter);
    verify(apiAdapter).requestUpdates(locationRequest, callback);
    verifyNoMoreInteractions(apiAdapter);
  }

  @Test public void requestLastLocationForOneShot() {
    DefaultActionFactory defaultActionFactory = new DefaultActionFactory();
    PendingIntent callback = getDummyPendingIntent();
    RequestorAction requestorAction = defaultActionFactory.getActionToOneShotStart(callback);
    LocationApiAdapter apiAdapter = mock(LocationApiAdapter.class);
    requestorAction.onLocationApiReady(apiAdapter);
    verify(apiAdapter).sendLastKnownLocation(callback);
    verifyNoMoreInteractions(apiAdapter);
  }

  @Test public void stopActionRequestor() {
    DefaultActionFactory defaultActionFactory = new DefaultActionFactory();
    PendingIntent callback = getDummyPendingIntent();
    RequestorAction requestorAction = defaultActionFactory.getActionToStop(callback);
    LocationApiAdapter apiAdapter = mock(LocationApiAdapter.class);
    requestorAction.onLocationApiReady(apiAdapter);
    verify(apiAdapter).stopUpdates(callback);
    verifyZeroInteractions(apiAdapter);
  }
}
