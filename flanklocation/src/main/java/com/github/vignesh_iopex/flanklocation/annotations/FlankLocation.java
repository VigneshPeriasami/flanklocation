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
package com.github.vignesh_iopex.flanklocation.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate the implementation of {@link com.github.vignesh_iopex.flanklocation.ReconTask} with
 * this annotation to use the values to build
 * {@link com.google.android.gms.location.LocationRequest} for location updates from
 * {@link com.github.vignesh_iopex.flanklocation.Recon}
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface FlankLocation {
  long interval();

  int priority();

  long fastestInterval();

  /** values 0 & less than 0 are ignored */
  long maxWaitTime() default -1;

  /** values 0 & less than 0 are ignored */
  int numUpdates() default -1;

  /** values 0 & less than 0 are ignored */
  long duration() default -1;

  /** values 0 & less than 0 are ignored */
  float smallestDisplacement() default -1;
}
