// Copyright (c) 2013 All rights reserved.
// ============================================================================
// CURRENT VERSION 1
// ============================================================================
// CHANGE LOG// 1 : 2013-XX-XX, Administrator, creation
// ============================================================================
package com.ace.data.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class AceTest {
    public static void main(final String[] args) {
        final Map<String, Object> m1 = new HashMap<String, Object>();
        setMap(m1);
        System.out.println(m1.toString());
    }


    public static void setMap(final Map<String, Object> m1) {

        m1.put("1", "m1");
    }
}
