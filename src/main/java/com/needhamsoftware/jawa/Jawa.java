/*
 * Copyright (c) 2013 Needham Software LLC.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.needhamsoftware.jawa;

/*
 * Created with IntelliJ IDEA.
 * User: gus
 * Date: 7/18/13
 * Time: 5:52 PM
 */

import java.lang.reflect.Field;
import java.util.*;

public class Jawa {
  //
  IdentitySet visited = new IdentitySet();

  Set<Class> ignoredClasses = new HashSet<>();

  List<EntryAction> onEntryActions = new ArrayList<>();
  List<ExitAction> onExitActions = new ArrayList<>();

  public void ignore(Class c) {
    ignoredClasses.add(c);
  }

  public void unIgnore(Class c) {
    ignoredClasses.remove(c);
  }

  public void addEntryAction(EntryAction action) {
    onEntryActions.add(action);
  }

  public void addExitAction(ExitAction action) {
    onExitActions.add(action);
  }

  public void walk(Object root) {
    if (visited.contains(root)) {
      return;
    }
    visited.add(root);
    for (EntryAction onEntryAction : onEntryActions) {
      onEntryAction.onEntry(root);
    }
    Field[] fields = root.getClass().getFields();
    for (Field field : fields) {
      try {
        if (!shouldTraverse(field.getType())) {
          continue;
        }
        Object contents = field.get(root);
        if (contents != null) {
          if (contents.getClass().isArray()) {
            List aryElements = Arrays.asList(contents);
            for (Object aryElement : aryElements) {
              walk(aryElement);
            }
          } else {
            walk(contents);
          }
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    for (ExitAction onExitAction: onExitActions) {
      onExitAction.onExit(root);
    }
  }

  @SuppressWarnings("unchecked")
  boolean shouldTraverse(Class c) {
    return (c.isPrimitive() || c.isAssignableFrom(Number.class) || c == String.class);
  }

}
