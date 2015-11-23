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

import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.android.gms.location.FusedLocationProviderApi.KEY_LOCATION_CHANGED;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 18, constants = BuildConfig.class)
public class ReconTaskTest {

  @Test public void withConnectionResult() {
    ConnectionResult connectionResult = new ConnectionResult(0, null);
    Intent intent = ReconTask.getFailureAsExtras(connectionResult);
    ReconTask reconTask = Mockito.spy(new TestReconTask());
    reconTask.onHandleIntent(intent);
    verify(reconTask).onConnectionError(connectionResult);
    verifyNoMoreInteractions(reconTask);
  }

  @Test public void withOneShotLocation() {
    Intent intent = new Intent();
    Location location = new Location("JUNIT");
    intent.putExtra(KEY_LOCATION_CHANGED, location);
    ReconTask reconTask = Mockito.spy(new TestReconTask());
    reconTask.onHandleIntent(intent);
    verify(reconTask).onNextLocation(notNull(LocationResult.class));
    verifyNoMoreInteractions(reconTask);
  }

  class TestReconTask extends ReconTask {
    public TestReconTask() {
      super("testrecon");
    }

    @Override protected void onConnectionError(@Nullable ConnectionResult connectionResult) {

    }

    @Override protected void onNextLocation(@Nullable LocationResult locationResult) {

    }
  }
}
