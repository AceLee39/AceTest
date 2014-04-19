// Copyright (c) 2013 All rights reserved.
// ============================================================================
// CURRENT VERSION 1
// ============================================================================
// CHANGE LOG// 1 : 2013-XX-XX, Administrator, creation
// ============================================================================
package com.ace.data.entity;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public interface DynamicEntity extends Map<String, Object> {
    String getEntityName();
    void setEntityName(String entityNames);

    boolean isNew();
    void setNew(boolean isNew);

    Object putValue(String field, Object value);

    Map<String, Object> getModifiedValueMap();

    Map<String, Object> getOriginalValueMap();



}
