package com.github.vignesh_iopex.flanklocation;

import java.util.LinkedList;
import java.util.List;

public interface ActionQueue {
  List<RequestorAction> ACTION_LIST = new LinkedList<>();

  void enqueue(RequestorAction action);

  void informAll(LocationAdapter locationAdapter);

  ActionQueue DEFAULT_QUEUE = new ActionQueue() {
    @Override public void enqueue(RequestorAction action) {
      synchronized (ACTION_LIST) {
        ACTION_LIST.add(action);
      }
    }

    @Override public void informAll(LocationAdapter locationAdapter) {
      synchronized (ACTION_LIST) {
        for (RequestorAction requestorAction : ACTION_LIST) {
          locationAdapter.applyAction(requestorAction);
        }
        ACTION_LIST.clear();
      }
    }
  };
}
