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

import java.util.*;

public class IdentitySet implements Set {
  Map<Integer, List<Object>> elements = new HashMap<>();

  @Override
  public int size() {
    int size=0;
    for (Integer integer : elements.keySet()) {
      size += elements.get(integer).size();
    }
    return size;
  }

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    if (o == null) {
      return false;
    }
    List<Object> objects = elements.get(System.identityHashCode(o));
    return objects != null && objects.contains(o);
  }

  List<Object> bigList() {
    List result = new ArrayList();
    for (Integer bucket : elements.keySet()) {
      for (Object o : elements.get(bucket)) {
        result.add(o);
      }
    }
    return result;
  }

  @Override
  public Iterator iterator() {
    return bigList().iterator();
  }

  @Override
  public Object[] toArray() {
    return bigList().toArray();
  }

  @Override
  public boolean add(Object o) {
    int key = System.identityHashCode(o);
    List<Object> bucket = elements.get(key);
    if (bucket != null && bucket.contains(o)) {
      return false;
    } else {
      if (bucket == null) {
        bucket = new ArrayList<>();
      }
      bucket.add(o);
      elements.put(key,bucket);
      return true;
    }
  }

  @Override
  public boolean remove(Object o) {
    List<Object> bucket = elements.get(System.identityHashCode(o));
    return bucket != null && bucket.remove(o);
  }

  @Override
  public boolean containsAll(Collection c) {
    for (Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(Collection c) {
    boolean anyAdded = false;
    for (Object o : c) {
      anyAdded |= add(o);
    }
    return anyAdded;
  }

  @Override
  public boolean retainAll(Collection c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection c) {
    boolean anyRemoved = false;
    for (Object o : c) {
      anyRemoved |= remove(o);
    }
    return anyRemoved;
  }

  @Override
  public void clear() {
    elements = new HashMap<>();
  }

  @Override
  public Object[] toArray(Object[] a) {
    return bigList().toArray();
  }
}
