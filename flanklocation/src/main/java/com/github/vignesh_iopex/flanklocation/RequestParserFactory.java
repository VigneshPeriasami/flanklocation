package com.github.vignesh_iopex.flanklocation;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface RequestParserFactory {
  RequestorAction forStart(Context context, @NonNull Class<? extends FlankTask> clsFlankTask);

  RequestorAction forStart(@NonNull Class<? extends FlankTask> clsFlankTask,
                           @Nullable PendingIntent callback);

  RequestorAction forStop(@NonNull PendingIntent pendingIntent);

  RequestorAction forStop(Context context, Class<? extends FlankTask> clsFlankTask);
}
