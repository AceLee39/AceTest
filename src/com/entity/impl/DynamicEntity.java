// Copyright (c) 1998-2013 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.39
// ============================================================================
// CHANGE LOG
// CNT.5.0.039 : 2013-04-15, randy.huang, CNT-8960
// CNT.5.0.037 : 2013-02-20, wilson.lun, CNT-8210
// CNT.5.0.036 : 2013-01-16, aron.tang, CNT-7864
// CNT.5.0.035 : 2013-01-09, randy.huang, CNT-7550
// CNT.5.0.035 : 2012-12-26, real.ji, CNT-7509
// CNT.5.0.027 : 2012-08-23, real.ji, CNT-5609
// CNT.5.0.027 : 2012-08-22, aron.tang, CNT-5631
// CNT.5.0.021 : 2012-07-04, aron.tang, CNT-4824
// CNT.5.0.016 : 2012-05-22, tim.chan, CNT-4031
// CNT.5.0.1 : 2011-10-14, Eric.Chen, Add 'getReference' method.
// CNT.5.0.1 : 2011-07-26, raymond.chiu, creation
// ============================================================================

package com.core.cbx.data.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import com.core.cbx.common.type.DateTime;

public interface DynamicEntity extends Map<String, Object> {

    String getId();
    void setId(String id);

    Long getRevision();
    void setRevision(Long revision);

    void setVersion(Long version);
    Long getVersion();

    void setEntityVersion(Integer entityVersion);
    Integer getEntityVersion();

    String getEntityName();
    void setEntityName(String entityName);

    boolean isInitiated();
    void setInitiated();

    DynamicEntity clone();

    // set the entity is a new created entity
    void setNewEnity();
    boolean isNewEntity();

    // set the entity is a modified entity
    void setModifiedEntity();
    boolean isModifiedEntity();

    void setDeletedEntity();
    void setUnDeleted();
    boolean isDeletedEntity();

    Map<String, Object> getModifiedValueMap();
    Map<String, Object> getOriginalValueMap();

    DynamicEntity getModifiedEntity();

    void clearAllModifiedInfo();
    void clearModifiedFlag();

    Object putValue(String field, Object value);

    String getReference();

    DynamicEntity copy();

    String getString(String fieldId);
    String getString(String fieldId, String defaultValue);

    DateTime getDateTime(String fieldId);
    DateTime getDateTime(String fieldId, DateTime defaultValue);

    Long getLong(String fieldId);
    Long getLong(String fieldId, Long defaultValue);

    BigDecimal getBigDecimal(String fieldId);
    BigDecimal getBigDecimal(String fieldId, BigDecimal defaultValue);

    Boolean getBoolean(String fieldId);
    Boolean getBoolean(String fieldId, Boolean defaultValue);

    byte[] getByteArray(String fieldId);
    byte[] getByteArray(String fieldId, byte[] defaultValue);

    void setDate(String fieldId, String dateStr);
    void setDate(String fieldId, String dateStr, String format);
    void setDate(String fieldId, Integer aYear, Integer aMonth, Integer aDay);

    void setDateTime(String fieldId, String dateStr);
    void setDateTime(String fieldId, String dateStr, String format);
    void setDateTime(String fieldId, Integer aYear, Integer aMonth,
            Integer aDay, Integer aHour, Integer aMin, Integer aSecond);

    DynamicEntity getEntity(String fieldId);
    Collection<DynamicEntity> getEntityCollection(String fieldId);

    DynamicEntity getCurrentHeader();
    DynamicEntity getOriginalHeader();

    boolean isFullEntity();
    void setFullEntity(boolean flag);

    DynamicEntity copyForArchive();

    boolean isFullEntityModified();

    String getDomainId();
    void setDomainId(String domainId);

    String getHubDomainId();
    void setHubDomainId(String hubDomainId);

    boolean isForReference();
    void setForReferenced(boolean isForReference);

    DynamicEntity copyByTransitiveFields(Map<Integer, Object> context,
            Collection<String> transitiveFields, boolean isForReference);

    // reset meta data to be a brand new entity.
    void resetMetaData();

}
