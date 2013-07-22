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
