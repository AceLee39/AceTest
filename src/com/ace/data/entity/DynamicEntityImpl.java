// Copyright (c) 2013 All rights reserved.
// ============================================================================
// CURRENT VERSION 1
// ============================================================================
// CHANGE LOG// 1 : 2013-XX-XX, Administrator, creation
// ============================================================================
package com.ace.data.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class DynamicEntityImpl extends HashMap<String, Object> implements DynamicEntity {

    private String entityName;
    private boolean isNew;

    private final Map<String, Object> modifiedValueMap = new HashMap<String, Object>();
    private final Map<String, Object> originalValueMap = new HashMap<String, Object>();


    @Override
    public String getEntityName() {
        return this.entityName;
    }

    @Override
    public void setEntityName(final String entityName) {
        this.putValue(EntityConstants.ACE_ENTITY_NAME, entityName);
        this.entityName = entityName;
    }

    @Override
    public Object putValue(final String field, final Object value) {
        return null;
    }

    @Override
    public Map<String, Object> getModifiedValueMap() {
        return this.modifiedValueMap;
    }

    @Override
    public Map<String, Object> getOriginalValueMap() {
        return this.originalValueMap;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @Override
    public void setNew(final boolean isNew) {
        putValue(EntityConstants.ACE_IS_NEW, isNew);
        this.isNew = isNew;
    }



}
