Flank Location
==============
Android location api wrapper. Using `FlankService` as a callback PendingIntent would
give clean access to Google location API locations and errors.

Usage
---------

```java
@FlankLocation(interval = 3000, fastestInterval = 2000, priority = PRIORITY_HIGH_ACCURACY)
public class ReconExample extends ReconTask {
  public ReconExample() {
    super("ExampleRecon");
  }

  protected onNextLocation(LocationResult locationResult) {
    // do something with location result
  }

  protected onConnectionError(ConnectionResult connectionResult) {
    // check if it is resolvable.
  }
}
```

**Note:** This library is not ready for production use it at your own risk.

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