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
