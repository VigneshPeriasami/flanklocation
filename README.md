Flank Location
==============
A simple API that cleanly registers pending intent for location updates from Google location api.

Using `Recon` and `FlankLocation` annotation the location update registration using pending
intent is made easier, This still supports dynamic location request at runtime by accepting it as
parameter when registering for location updates.

Usage
------

```java
@FlankLocation(interval = 3000, fastestInterval = 2000, priority = PRIORITY_HIGH_ACCURACY)
public class ReconExample extends ReconTask {
  public ReconExample() {
    super("ExampleRecon");
  }

  /** starts registering for location updates */
  public static void start(Context context) {
    Recon.using(context).start(ReconExample.class);
  }

  /** stops listening to location updates */
  public static void stop(Context context) {
    Recon.using(context).stop(ReconExample.class);
  }

  protected onNextLocation(LocationResult locationResult) {
    // do something with location result
  }

  protected onConnectionError(ConnectionResult connectionResult) {
    // check if it is resolvable.
  }
}
```


**Note:** This library is still experimental hence could undergo major design changes.

Download
--------

Download via maven:
```xml
<dependency>
    <groupId>com.github.vignesh-iopex</groupId>
    <artifactId>flanklocation</artifactId>
    <version>0.9</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.github.vignesh-iopex:flanklocation:0.9'
```

License
-------

    Copyright 2015 Vignesh Periasami

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.