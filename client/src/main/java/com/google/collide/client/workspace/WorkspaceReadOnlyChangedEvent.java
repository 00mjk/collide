// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.collide.client.workspace;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is fired within a {@link WorkspacePlace} to notify all listeners
 * that the workspace has been opened in read only mode.
 */
public class WorkspaceReadOnlyChangedEvent extends GwtEvent<WorkspaceReadOnlyChangedEvent.Handler> {

  public interface Handler extends EventHandler {
    void onWorkspaceReadOnlyChanged(WorkspaceReadOnlyChangedEvent event);
  }
  
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private boolean isReadOnly;
  
  public WorkspaceReadOnlyChangedEvent(boolean isReadOnly) {
    this.isReadOnly = isReadOnly;
  }
  
  public boolean isReadOnly() {
    return isReadOnly;
  }
  
  @Override
  protected void dispatch(Handler handler) {
    handler.onWorkspaceReadOnlyChanged(this);
  }
  
  @Override 
  public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
    return TYPE;
  }
}
