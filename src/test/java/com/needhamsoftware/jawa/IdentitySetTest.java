package com.needhamsoftware.jawa;

/*
 * Created with IntelliJ IDEA.
 * User: gus
 * Date: 7/22/13
 * Time: 2:25 PM
 */

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class IdentitySetTest {

  @Test
  public void testAdd() {
    IdentitySet set = new IdentitySet();

    // Point is used below because it overrides equals such that
    // a.equals(b) is true, but a == b will be false.

    Point a = new Point(4,2);
    Point b = new Point(4,2);
    Point c = new Point(5,3);

    assertEquals(a,b);
    assertEquals(false, a == b);
    assertEquals(false, a.equals(c));

    assertEquals(0,set.size());
    assertEquals(false, set.contains(a));
    assertEquals(false, set.contains(b));
    assertEquals(false, set.contains(c));
    set.add(a);
    assertEquals(1,set.size());
    assertEquals(true, set.contains(a));
    assertEquals(false, set.contains(b));
    assertEquals(false, set.contains(c));
    set.add(b);
    assertEquals(2,set.size());
    assertEquals(true, set.contains(a));
    assertEquals(true, set.contains(b));
    assertEquals(false, set.contains(c));
    set.add(a);
    assertEquals(2,set.size());
    assertEquals(true, set.contains(a));
    assertEquals(true, set.contains(b));
    assertEquals(false, set.contains(c));
    set.add(c);
    assertEquals(3,set.size());
    assertEquals(true, set.contains(a));
    assertEquals(true, set.contains(b));
    assertEquals(true, set.contains(c));
    set.remove(a);
    assertEquals(2,set.size());
    assertEquals(false, set.contains(a));
    assertEquals(true, set.contains(b));
    assertEquals(true, set.contains(c));
  }

}
