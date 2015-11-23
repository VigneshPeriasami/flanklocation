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

import java.util.LinkedList;
import java.util.List;

interface ActionQueue {
  List<RequestorAction> ACTION_LIST = new LinkedList<>();

  void enqueue(RequestorAction action);

  void informAll(PlayServiceAdapter playServiceAdapter);

  ActionQueue DEFAULT_QUEUE = new ActionQueue() {
    @Override public void enqueue(RequestorAction action) {
      synchronized (ACTION_LIST) {
        ACTION_LIST.add(action);
      }
    }

    @Override public void informAll(PlayServiceAdapter playServiceAdapter) {
      synchronized (ACTION_LIST) {
        for (RequestorAction requestorAction : ACTION_LIST) {
          playServiceAdapter.applyAction(requestorAction);
        }
        ACTION_LIST.clear();
      }
    }
  };
}
