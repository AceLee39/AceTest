// Copyright (c) 1998-2013 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.40
// ============================================================================
// CHANGE LOG
// CNT.5.0.040 : 2013-05-23, jet.yang, CNT-9518
// CNT.5.0.039 : 2013-04-15, randy.huang, CNT-8960
// CNT.5.0.038 : 2013-04-01, aron.tang, CNT-8738
// CNT.5.0.038 : 2013-03-22, wilson.lun, CNT-8660
// CNT.5.0.038 : 2013-03-19, wilson.lun, CNT-8609
// CNT.5.0.037 : 2013-02-20, wilson.lun, CNT-8210
// CNT.5.0.037 : 2013-02-20, real.ji, CNT-8211
// CNT.5.0.036 : 2013-01-28, wilson.lun, CNT-7252
// CNT.5.0.036 : 2013-01-16, aron.tang, CNT-7864
// CNT.5.0.035 : 2013-01-09, randy.huang, CNT-7550
// CNT.5.0.035 : 2012-12-26, real.ji, CNT-7509
// CNT.5.0.034 : 2012-12-04, real.ji, CNT-7164
// CNT.5.0.032 : 2012-11-12, real.ji, CNT-6722
// CNT.5.0.029 : 2012-09-25, Andy.Li, CNT-5839
// CNT.5.0.029 : 2011-09-26, aron.tang, CNT-6142
// CNT.5.0.029 : 2012-09-19, real.ji, CNT-6033
// CNT.5.0.029 : 2012-09-17, real.ji, CNT-6025
// CNT.5.0.027 : 2012-08-23, real.ji, CNT-5609
// CNT.5.0.027 : 2012-08-22, aron.tang, CNT-5631
// CNT.5.0.024 : 2012-08-01, real.ji, CNT-5280
// CNT.5.0.024 : 2012-07-30, real.ji, CNT-5222
// CNT.5.0.024 : 2012-07-27, tim.chan, CNT-5226
// CNT.5.0.024 : 2012-07-26, aron.tang, CNT-5223
// CNT.5.0.023 : 2012-07-23, real.ji, CNT-5129
// CNT.5.0.021 : 2012-07-13, raymond.chiu, re-apply CNT-4874
// CNT.5.0.021 : 2012-07-04, aron.tang, CNT-4824
// CNT.5.0.020 : 2012-06-27, aron.tang, CNT-4682
// CNT.5.0.020 : 2012-06-26, raymond.chiu, CNT-4644
// CNT.5.0.019 : 2012-06-12, randy.huang, CNT-4429
// CNT.5.0.016 : 2012-05-31, Andy.Li, CNT-3629
// CNT.5.0.016 : 2012-05-22, tim.chan, CNT-4031
// CNT.5.0.015 : 2012-05-18, randy.huang, CNT-3975
// CNT.5.0.015 : 2012-05-16, Jacky.Zhang, CNT-3881
// CNT.5.0.1 : 2012-05-06, randy.huang, CNT-3614
// CNT.5.0.1 : 2011-10-14, Eric.Chen, CNT-339 Modified to save refNo for new entity.
// CNT.5.0.1 : 2011-09-05, andy.li, modified and refractored base on code-reviewed feedback.
// CNT.5.0.1 : 2011-08-24, andy.li, set default to 0 when the revision is null in getRevision()
// CNT.5.0.1 : 2011-08-18, andy.li, creation
// ============================================================================

package com.core.cbx.data.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import com.core.cbx.calculation.CalculationHelper;
import com.core.cbx.common.calculation.Calculator;
import com.core.cbx.common.convert.BigDecimalConverter;
import com.core.cbx.common.convert.BooleanConverter;
import com.core.cbx.common.convert.ByteArrayConverter;
import com.core.cbx.common.convert.Converter;
import com.core.cbx.common.convert.LongConverter;
import com.core.cbx.common.convert.StringConverter;
import com.core.cbx.common.logging.CNTLogger;
import com.core.cbx.common.logging.LogFactory;
import com.core.cbx.common.type.DateTime;
import com.core.cbx.common.util.UUIDGenerator;
import com.core.cbx.conf.service.SystemConfigManager;
import com.core.cbx.data.EntityFieldHelper;
import com.core.cbx.data.constants.ExceptionConstants;
import com.core.cbx.data.convert.ConverterFactory;
import com.core.cbx.data.convert.DateTimeConverter;
import com.core.cbx.data.convert.EntityCollectionConverter;
import com.core.cbx.data.convert.EntityConverter;
import com.core.cbx.data.def.EntityDefManager;
import com.core.cbx.data.def.EntityDefModel;
import com.core.cbx.data.def.entity.EntityDefinition;
import com.core.cbx.data.def.entity.FieldDefinition;
import com.core.cbx.data.def.entity.FieldType;
import com.core.cbx.data.exception.DataException;
import com.core.cbx.security.AuthenticationUtil;


/**
 * This is internal implementation of DynamicEntity, and it is not support to use outside of data layer
 *
 * @author andy.li
 */
public class DynamicEntityImp extends HashMap<String, Object> implements DynamicEntity, EntityConstants {
    private static final long serialVersionUID = 1L;
    private static final CNTLogger logger = LogFactory.getLogger(DynamicEntityImp.class);
    private static final Set<String> NON_TRANSITIVE_MODULES
            = new HashSet<String>(Arrays.asList("hcl", "codelist"));

    private String entityName;
    private Integer entityVersion;
    private boolean newEntity;
    private boolean isInitiated;
    private boolean modifiedEntity;
    private boolean deletedEntity;
    private boolean isFullEntity;
    private final Map<String, Object> modifiedValueMap = new HashMap<String, Object>();
    private final Map<String, Object> originalValueMap = new HashMap<String, Object>();

    public DynamicEntityImp() {
        super();
    }

    public DynamicEntityImp(final String entityName, final Integer entityVersion) {
        super();
        Integer entityVer = entityVersion;
        EntityDefinition entityDefinition = null;
        try {
            if (entityVer == null) {
                entityVer = EntityDefManager.getLatestEntityVersion(entityName);
            }
            entityDefinition = EntityDefManager.getEntityDefinition(entityName, entityVer);
            if (entityDefinition != null) {
                final List<FieldDefinition> fieldDefinitionList = entityDefinition.getFieldDefinitions();
                for (final FieldDefinition fieldDefinition : fieldDefinitionList) {
                    if (EntityDefModel.isBooleanFieldType(fieldDefinition.getFieldType())
                            && !StringUtils.equals(fieldDefinition.getFieldId(), EntityConstants.PTY_IS_LATEST)) {
                        this.put(fieldDefinition.getFieldId(), Boolean.FALSE);
                    } else {
                        this.put(fieldDefinition.getFieldId(), null);
                    }
                }
            }
        } catch (final DataException e) {
            throw new RuntimeException("Error in construct a new entity", e);
        }
        this.setEntityName(entityName);
        this.setEntityVersion(entityVer);
    }

    @Override
    public String getId() {
        return (String) get(PTY_ID);
    }

    @Override
    public void setId(final String id) {
        put(PTY_ID, id);
    }

    @Override
    public Long getRevision() {
        final Number revision = (Number) get(PTY_REVISION);
        if (revision == null) {
            return Long.valueOf(0L);
        }
        return revision.longValue();
    }

    @Override
    public void setRevision(final Long revision) {
        putValue(PTY_REVISION, revision);
    }

    @Override
    public Long getVersion() {
        final Number version = (Number) get(PTY_VERSION);
        if (version == null) {
            return null;
        }
        return version.longValue();
    }

    @Override
    public void setVersion(final Long version) {
        putValue(PTY_VERSION, version);
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public void setEntityName(final String entityName) {
        this.putValue(EntityConstants.PTY_ENTITY_NAME, entityName);
        this.entityName = entityName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DynamicEntityImp)) {
            return false;
        }

        final DynamicEntityImp other = (DynamicEntityImp) o;

        // if the id is missing, return false
        if (getId() == null) {
            return false;
        }

        // equivalence by id
        return StringUtils.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(entityName)
            .hashCode();
    }

    @Override
    public DynamicEntity clone() {
        try {
            final Map<String, Object> context = new HashMap<String, Object>();
            return clone(context, true);
        } catch (final DataException e) {
            throw new RuntimeException("clone()", e);
        }
    }

    @SuppressWarnings("unchecked")
    private DynamicEntity clone(final Map<String, Object> context, final boolean cloneChild) throws DataException {
        // assign temp UUID if the ID is not set yet.
        final boolean emptyId = StringUtils.isEmpty(getId());
        if (emptyId) {
            setId(UUIDGenerator.getUUID());
        }

        final DynamicEntityImp dei = new DynamicEntityImp(getEntityName(), getEntityVersion());
        context.put(getId(), dei);

        final String curModule = EntityDefManager.getModuleCodeByEntityName(getEntityName());
        for (final Map.Entry<String, Object> entry : entrySet()) {
            final String fieldId = entry.getKey();
            final Object value = entry.getValue();
            if (value instanceof Collection) {
                if (!cloneChild) {
                    continue;
                }
                final List<DynamicEntity> children = new ArrayList<DynamicEntity>();
                for (final DynamicEntity de : (Collection<DynamicEntity>) value) {
                    // check if the entity already cloned
                    DynamicEntity eClone = (DynamicEntity) context.get(de.getId());
                    if (eClone == null) {
                        eClone = ((DynamicEntityImp) de).clone(context, cloneChild);
                    }
                    children.add(eClone);
                }
                dei.put(fieldId, children);
            } else if (value instanceof DynamicEntity) {
                final String module = EntityDefManager.getModuleCodeByEntityName(
                        ((DynamicEntity) value).getEntityName());
                final FieldDefinition fieldDef = EntityDefManager.getFieldDefinition(
                        this.getEntityName(), this.getEntityVersion().intValue(), fieldId);

                if (StringUtils.equals(module, curModule)) {
                    if (fieldDef != null) {
                        setEntityValue(dei, fieldId, context, (DynamicEntity) value);
                    } else {
                        dei.put(fieldId, value);
                    }
                } else {
                    if (fieldDef != null && fieldDef.getManaged()) {
                        setEntityValue(dei, fieldId, context, (DynamicEntity) value);
                    } else {
                        dei.put(fieldId, value);
                    }
                }
            } else {
                dei.put(fieldId, value);
            }
        }

        if (this.newEntity) {
            dei.setNewEnity();
        }
        if (this.modifiedEntity) {
            dei.setModifiedEntity();
        }
        if (this.deletedEntity) {
            dei.setDeletedEntity();
        }

        // reset ID if the original ID is null
        if (emptyId) {
            setId(null);
            dei.setId(null);
        }
        dei.setInitiated();
        return dei;
    }


    private void setEntityValue(final DynamicEntity entity, final String key, final Map<String, Object> context,
            final DynamicEntity childEntity) throws DataException {
        if (context.containsKey((childEntity).getId())) {
            entity.put(key, context.get((childEntity).getId()));
        } else {
            final DynamicEntity eClone = ((DynamicEntityImp) childEntity).clone(context, true);
            entity.put(key, eClone);
        }
    }

    /**
     * put the value into the entity and record in originalValueMap and modifiedValueMap
     *
     * @param field
     *            the field name
     * @param value
     *            the field value
     */
    @Override
    public Object putValue(final String field, final Object value) {
        // Not allow to put non-serializable values
        if (value != null && !(value instanceof Serializable)) {
            logger.warn(ExceptionConstants.DATA_EXCEPTION_000001,
                    "Not allow to put no-serializable values. "
                   + field + " : " + value.getClass().getName());
            return null;
        }

        final Object convertedValue = toConvertValue(field, value);
        if (ableToPutValue(field, convertedValue)) {
            this.addToOriginalValueMap(field, get(field));
            this.addToModifiedValueMap(field, convertedValue);
            this.setModifiedEntity();
            return super.put(field, convertedValue);

        }
        return super.put(field, convertedValue);
    }

    private boolean hasNonEnChar(final String s) {
        boolean result = false;
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);
                if (c > 127) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private Object toConvertValue(final String fieldId, final Object value)  {
        if (!isInitiated || StringUtils.equals(fieldId, EntityConstants.PTY_ENTITY_VERSION)) {
            return value;
        }
        FieldDefinition fieldDef = null;
        try {
            fieldDef = EntityDefManager.getFieldDefinition(getEntityName(), this.entityVersion.intValue(), fieldId);
        } catch (final DataException e) {
            throw new RuntimeException("Failed to find the field definition of field:" + fieldId);
        }
        if (value == null) {
            return null;
        }
        if (fieldDef == null) {
            logger.debug("Field definition of field: " + fieldId + " is null, return the value:" + value);
            return value;
        }
        final String fieldType = fieldDef.getFieldType();

        try {
            if (EntityDefModel.isDecimalFieldType(fieldType)) {
                final Calculator calculator = CalculationHelper.getCalculator(getDomainId(), fieldDef);
                return calculator.getDecimalVal(value);
            }
        } catch (final Exception e) {
            logger.info("Failed to convert decimal field: " + fieldDef.getFieldId() + ", to format value", e);
        }

        // Ignore the entity field type convert logic for JIRA: CNT-3362
        if (EntityDefModel.isEntityType(fieldType)) {
            return value;
        }
        //logger.debug("Get field: " + fieldId + "'s converter of entity: " + getEntityName());
        final Converter<?> converter = ConverterFactory.getInstance().getConverter(fieldDef);
        if (converter == null) {
            return value;
        }
        Object result = null;
        try {
            result = converter.doConvert(value);
        } catch (final Exception e) {
            logger.warn(ExceptionConstants.DATA_EXCEPTION_000001,
                    "Failed to convert field: " + fieldId + ", return null value instead");
        }

        // truncate length for string and number/decimal
        if (EntityDefModel.isTextType(fieldType) && fieldDef.getMaxLength() > 0
                && result instanceof String) {
            String s = (String) result;
            if (s.length() > fieldDef.getMaxLength()) {
                // Truncate the text value if exceeds the max length, and log as error
                final String newStr = StringUtils.substring(s, 0, fieldDef.getMaxLength());
                logger.warn(ExceptionConstants.DATA_EXCEPTION_000001, "Overflow error exceeding the range "
                        + fieldDef.getMinLength() + " - " + fieldDef.getMaxLength() + " identified. " + "Field value ["
                        + fieldId + "] has been reset from [" + s + "] to [" + newStr + "] by system.");
                s = newStr;
            }

            if (s.length() > 1300 && hasNonEnChar(s)) {
                // check if it has Non-English characters (e.g chinese)
                // If yes, truncate by 1300 characters
                logger.debug("Text has non-English charactors, truncate to maximum 1300 charators");
                s = StringUtils.substring(s, 0, 1300);
            }
            result = s;

        } else if ((EntityDefModel.isIntegerFieldType(fieldType) || EntityDefModel.isDecimalFieldType(fieldType))
                && fieldDef.getMaxLength() > 0 && result instanceof Number) {
            final Number n = (Number) result;
            // If it is number/decimal, truncate the value if the part before decimal exceeds the max length
            final String longPart = StringUtils.substringBefore(n.toString(), EntityConstants.SEPARATOR_DOT);
            if (longPart.length() > fieldDef.getMaxLength()) {
                if (n instanceof Long) {
                    result = NumberUtils.toLong(StringUtils.substring(longPart,
                            longPart.length() - fieldDef.getMaxLength()), Long.MAX_VALUE);
                } else if (n instanceof BigDecimal) {
                    result = NumberUtils.createBigDecimal(StringUtils.substring(n.toString(),
                            longPart.length() - fieldDef.getMaxLength()));
                }
                logger.warn(ExceptionConstants.DATA_EXCEPTION_000001, "Overflow error exceeding the range "
                        + fieldDef.getMinLength() + " - " + fieldDef.getMaxLength() + " identified. " + "Field value ["
                        + fieldId + "] has been reset from [" + n.toString() + "] to [" + result + "] by system.");
            }
        }

        if (result instanceof BigDecimal && EntityDefModel.isDecimalFieldType(fieldType)) {
            result = EntityFieldHelper.round(getEntityName(), fieldId, result);
        }

        return result;
    }

    private boolean ableToPutValue(final String field, final Object value) {
        boolean ableToPutValue = true;
        if (!isInitiated()) {
            ableToPutValue = false;
        } else {
            if (!isEntityField(this, field)) {
                ableToPutValue = false;
            } else if (value instanceof Collection) {
                ableToPutValue = false;
            } else if (value instanceof DynamicEntity) {
                final Object origin = super.get(field);
                if ((origin != null && !(origin instanceof DynamicEntity))
                        || ObjectUtils.equals(value, super.get(field))) {
                    return false;
                }
            } else if (ObjectUtils.equals(value, super.get(field))) {
                ableToPutValue = false;
            }
        }
        return ableToPutValue;
    }

    /**
     * @param entity
     * @param field
     * @return
     * @throws DataException
     */
    private boolean isEntityField(final DynamicEntity entity, final String field) {
        try {
            return EntityDefManager.getFieldDefinition(
                    entity.getEntityName(), entity.getEntityVersion().intValue(), field) != null;
        } catch (final DataException e) {
            throw new RuntimeException("Fail to get field definition: " + field);
        }
    }

    /**
     * put the value in this entity and will not touch the OriginalValueMap and the ModifiedValueMap
     */
    @Override
    public Object put(final String field, final Object value) {
        return putValue(field, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> map) {
        for (final Map.Entry<? extends String, ? extends Object> entry : map.entrySet()) {
            this.putValue(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean isModifiedEntity() {
        return modifiedEntity;
    }

    @Override
    public void setModifiedEntity() {
        modifiedEntity = true;
    }

    @Override
    public boolean isNewEntity() {
        return newEntity;
    }

    @Override
    public void setNewEnity() {
        newEntity = true;
    }

    /**
     * add the field value by the first time as the original value
     *
     * @param field
     *            the file name
     * @param value
     *            the field value
     */
    private void addToOriginalValueMap(final String field, final Object value) {
        if (this.originalValueMap.containsKey(field)) {
            return;
        }
        this.originalValueMap.put(field, value);
    }

    /**
     * add the field value each time as the current value
     *
     * @param field
     *            the file name
     * @param value
     *            the field value
     */
    private void addToModifiedValueMap(final String field, final Object value) {
        modifiedValueMap.put(field, value);
    }

    @Override
    public Map<String, Object> getModifiedValueMap() {
        return modifiedValueMap;
    }

    @Override
    public Map<String, Object> getOriginalValueMap() {
        return originalValueMap;
    }

    /**
     * new Entity will be set to false modifiedEntity will be set to false modifiedValueMap will be cleared
     * originalValueMap will be cleared
     */
    @Override
    public void clearAllModifiedInfo() {
        doClearAllModifiedInfoRec(this);
    }

    /**
     * Recursively clear the info into all level of dynamic entity
     *
     * @param entity
     *            which need to be cleared the modified info
     */
    @SuppressWarnings("unchecked")
    private void doClearAllModifiedInfoRec(final DynamicEntity entity) {
        try {
            final EntityDefinition entityDef = EntityDefManager.getEntityDefinition(
                    entity.getEntityName(), entity.getEntityVersion());
            for (final FieldDefinition fieldDef : EntityDefModel.getEntityFieldDefinitions(entityDef)) {
                final Object fieldObj = entity.get(fieldDef.getFieldId());
                if (fieldObj instanceof DynamicEntity && fieldDef.getManaged()) {
                    doClearAllModifiedInfoRec((DynamicEntity) fieldObj);
                }
            }
            for (final FieldDefinition fieldDef : EntityDefModel.getChildrenFieldDefinitions(entityDef)) {
                final Object fieldObj = entity.get(fieldDef.getFieldId());
                if (fieldObj instanceof Collection) {
                    final Collection<DynamicEntity> childEntities = (Collection<DynamicEntity>) fieldObj;
                    final List<DynamicEntity> deleteEntities = new ArrayList<DynamicEntity>();
                    for (final DynamicEntity childEntity : childEntities) {
                        if (childEntity.isDeletedEntity()) {
                            // Add the child entity to the deleteEntities collection
                            deleteEntities.add(childEntity);
                            continue;
                        }
                        doClearAllModifiedInfoRec(childEntity);
                    }
                    childEntities.removeAll(deleteEntities);
                }
            }
            clearMetaFields(entity);
        } catch (final Exception e) {
            throw new RuntimeException("doClearAllModifiedInfoRec()", e);
        }
        ((DynamicEntityImp) entity).newEntity = false;
        ((DynamicEntityImp) entity).modifiedEntity = false;
        ((DynamicEntityImp) entity).modifiedValueMap.clear();
        ((DynamicEntityImp) entity).originalValueMap.clear();
    }

    /**
     * construct a entity which use the modifiedValueMap
     *
     * @return the DynamicEntity
     */
    @Override
    public DynamicEntity getModifiedEntity() {
        final DynamicEntity modifiedEntity = new DynamicEntityImp(this.getEntityName(), this.getEntityVersion());
        modifiedEntity.setId(this.getId());
        modifiedEntity.setEntityVersion(this.getEntityVersion());
        modifiedEntity.setRevision(this.getRevision());
        final Map<String, Object> mValueMap = getModifiedValueMap();
        for (final Map.Entry<String, Object> entry : mValueMap.entrySet()) {
            modifiedEntity.put(entry.getKey(), entry.getValue());
        }
        return modifiedEntity;
    }

    @Override
    public boolean isDeletedEntity() {
        return deletedEntity;
    }

    @Override
    public void setDeletedEntity() {
        deletedEntity = true;
    }

    @Override
    public void setUnDeleted() {
        deletedEntity = false;
    }

    @Override
    public Integer getEntityVersion() {
//        return ((Number) get(PTY_ENTITY_VERSION)).intValue();
        return this.entityVersion;
    }

    @Override
    public void setEntityVersion(final Integer entityVersion) {
        if (entityVersion != null) {
            this.entityVersion = entityVersion;
            super.put(PTY_ENTITY_VERSION, entityVersion);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#set(java.lang.String, java.lang.Object)
     */
    public void set(final String key, final Object value) {
        this.putValue(key, value);
    }

    @Override
    public boolean isInitiated() {
        return isInitiated;
    }

    /*
     * (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#setInitiated()
     */
    @Override
    public void setInitiated() {
        clearMyBatisFields(this);
        this.isInitiated = true;
    }

    @Override
    public String getReference() {
        return (String) this.get(PTY_REF_NO);
    }

    @Override
    public void clearModifiedFlag() {
        this.deletedEntity = false;
        this.modifiedEntity = false;
        this.newEntity = false;
    }

    /**
     * get the logger
     * @return the logger
     */
    public CNTLogger getLogger() {
        return logger;
    }

    @Override
    public String toString() {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append(entityName);
        try {
            if (EntityDefManager.getInstance().isInitiated()) {
                for (final Map.Entry<String, Object> entry : this.entrySet()) {
                    final String fieldId = entry.getKey();
                    final Object obj = entry.getValue();
                    if (obj instanceof DynamicEntity) {
                        final FieldDefinition fieldDef = EntityDefManager.getFieldDefinition(
                                entityName, getEntityVersion(), fieldId);
                        if (fieldDef != null && fieldDef.getManaged()) {
                            tsb.append(fieldId, obj);
                        } else {
                            final DynamicEntity e = ((DynamicEntity) obj);
                            final StringBuffer sb = new StringBuffer();
                            sb.append(e.getClass().getName())
                                .append("@").append(Integer.toHexString(System.identityHashCode(e)))
                                .append("[")
                                .append(e.getEntityName())
                                .append(SEPARATOR_COMMA).append(PTY_ID).append("=")
                                    .append(StringUtils.defaultString(e.getId()))
                                .append(SEPARATOR_COMMA).append(PTY_REF_NO).append("=")
                                    .append(StringUtils.defaultString(e.getReference()))
                                .append("]");
                            tsb.append(fieldId, sb);
                        }
                    } else {
                        tsb.append(fieldId, obj);
                    }
                }
            } else {
                tsb.append(this.entrySet());
            }
        } catch (final Throwable e) {
            // not to throw error
            logger.error("toString()", "Error in entity to string", e);
        }
        return tsb.toString();
    }

    /*
     * (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#copy()
     */
    @Override
    public DynamicEntity copy() {
        final DynamicEntity entity = this.clone();
        resetMeta4CloningEntity(entity, false);
        return entity;
    }

    /**
     * To modified status of the clone entity and its children entities
     *
     * @param cloningEntity
     *            the cloning entity.
     * @throws DataException
     *             if error occur
     */
    @SuppressWarnings("unchecked")
    private void resetMeta4CloningEntity(final DynamicEntity cloningEntity, final boolean forArchive) {
        try {
            ((DynamicEntityImp) cloningEntity).isInitiated = false;
            cloningEntity.clearModifiedFlag();
            cloningEntity.setNewEnity();
            if (!forArchive) {
                cloningEntity.setRevision(null);
                cloningEntity.setVersion(null);
                cloningEntity.put(EntityConstants.PTY_REF_NO, null);
                cloningEntity.put(EntityConstants.PTY_IS_LATEST, null);
            } else {
                cloningEntity.setRevision(cloningEntity.getRevision() - 1L);
            }
            cloningEntity.setId(null);
            final EntityDefinition entityDef = EntityDefManager.getEntityDefinition(cloningEntity.getEntityName(),
                    cloningEntity.getEntityVersion().intValue());
            for (final FieldDefinition fieldDef : entityDef.getFieldDefinitions()) {
                if (EntityDefManager.isFieldHasGeneratorDefined(fieldDef)) {
                    // reset all fields defined field value generator
                    if (!forArchive) {
                        cloningEntity.put(fieldDef.getFieldId(), null);
                    }
                } else if (EntityDefModel.isEntityType(fieldDef.getFieldType()) && fieldDef.getManaged()) {
                    final DynamicEntity childEntity = (DynamicEntity) cloningEntity.get(fieldDef.getFieldId());
                    if (childEntity != null) {
                        childEntity.put(fieldDef.getEntityLookupKey(), null);
                        resetMeta4CloningEntity(childEntity, forArchive);
                    }
                } else if (EntityDefModel.isChildrenType(fieldDef.getFieldType())) {
                    final Collection<DynamicEntity> childEntities = (Collection<DynamicEntity>) cloningEntity
                            .get(fieldDef.getFieldId());
                    if (childEntities == null) {
                        continue;
                    }
                    for (final DynamicEntity childEntity : childEntities) {
                        childEntity.put(fieldDef.getEntityLookupKey(), null);
                        resetMeta4CloningEntity(childEntity, forArchive);
                    }
                }
            }
            cloningEntity.setInitiated();
        } catch (final DataException e) {
            throw new RuntimeException("DataException happened in resetIdForClonedEntity()", e);
        }
    }

    /**
     * get String value
     */
    @Override
    public String getString(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof String)) {
                logger.debug("Getting string value on field [" + fieldId + "], convert [" + value + "] to string type");
            }
        }
        final StringConverter converter = (StringConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_STRING_TYPE);
        final String result = converter.doConvert(value);
        return result;
    }

    /**
     * get String value with default value
     */
    @Override
    public String getString(final String fieldId, final String defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof String)) {
                logger.debug("Getting string value on field [" + fieldId + "], convert [" + value + "] to string type");
            }
        }
        final StringConverter converter = (StringConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_STRING_TYPE);
        final String result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get DateTime value
     */
    @Override
    public DateTime getDateTime(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof DateTime)) {
                logger.debug("Getting dateTime value on field [" + fieldId + "], convert [" + value
                        + "] to dateTime type");
            }
        }
        final DateTimeConverter converter = (DateTimeConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_DATETIME_TYPE);
        final DateTime result = converter.doConvert(value);
        return result;
    }

    /**
     * get DateTime value with default value
     */
    @Override
    public DateTime getDateTime(final String fieldId, final DateTime defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof DateTime)) {
                logger.debug("Getting dateTime value on field [" + fieldId + "], convert [" + value
                        + "] to dateTime type");
            }
        }
        final DateTimeConverter converter = (DateTimeConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_DATETIME_TYPE);
        final DateTime result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get Long value
     */
    @Override
    public Long getLong(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof Long)) {
                logger.debug("Getting long value on field [" + fieldId + "], convert [" + value + "] to long type");
            }
        }
        final LongConverter converter = (LongConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_INTEGER_TYPE);
        final Long result = converter.doConvert(value);
        return result;
    }

    /**
     * get Long value with default value
     */
    @Override
    public Long getLong(final String fieldId, final Long defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof Long)) {
                logger.debug("Getting long value on field [" + fieldId + "], convert [" + value + "] to long type");
            }
        }
        final LongConverter converter = (LongConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_INTEGER_TYPE);
        final Long result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get BigDecimal value
     */
    @Override
    public BigDecimal getBigDecimal(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof BigDecimal)) {
                logger.debug("Getting bigDecimal value on field [" + fieldId + "], convert [" + value
                        + "] to bigDecimal type");
            }
        }
        final BigDecimalConverter converter = (BigDecimalConverter) ConverterFactory.getInstance().getConverter(
                FieldType.PREFIX_DECIMAL_TYPE);
        final BigDecimal result = converter.doConvert(value);
        return result;
    }

    /**
     * get BigDecimal value with default value
     */
    @Override
    public BigDecimal getBigDecimal(final String fieldId, final BigDecimal defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof BigDecimal)) {
                logger.debug("Getting bigDecimal value on field [" + fieldId + "], convert [" + value
                        + "] to bigDecimal type");
            }
        }
        final BigDecimalConverter converter = (BigDecimalConverter) ConverterFactory.getInstance().getConverter(
                FieldType.PREFIX_DECIMAL_TYPE);
        final BigDecimal result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get Boolean value
     */
    @Override
    public Boolean getBoolean(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof Boolean)) {
                logger.debug("Getting boolean value on field [" + fieldId + "], convert [" + value
                        + "] to boolean type");
            }
        }
        final BooleanConverter converter = (BooleanConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_BOOLEAN_TYPE);
        final Boolean result = converter.doConvert(value);
        return result;
    }

    /**
     * get Boolean value with default value
     */
    @Override
    public Boolean getBoolean(final String fieldId, final Boolean defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof Boolean)) {
                logger.debug("Getting boolean value on field [" + fieldId + "], convert [" + value
                        + "] to boolean type");
            }
        }
        final BooleanConverter converter = (BooleanConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_BOOLEAN_TYPE);
        final Boolean result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get byte[] value
     */
    @Override
    public byte[] getByteArray(final String fieldId) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof byte[])) {
                logger.debug("Getting byte[] value on field [" + fieldId + "], convert [" + value + "] to byte[] type");
            }
        }
        final ByteArrayConverter converter = (ByteArrayConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_BLOB_TYPE);
        final byte[] result = converter.doConvert(value);
        return result;
    }

    /**
     * get byte[] value with default value
     */
    @Override
    public byte[] getByteArray(final String fieldId, final byte[] defaultValue) {
        final Object value = get(fieldId);
        if (logger.isDebugEnable()) {
            if (value != null && !(value instanceof byte[])) {
                logger.debug("Getting byte[] value on field [" + fieldId + "], convert [" + value + "] to byte[] type");
            }
        }
        final ByteArrayConverter converter = (ByteArrayConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_BLOB_TYPE);
        final byte[] result = converter.doConvert(value, defaultValue);
        return result;
    }

    /**
     * get DynamicEntity value
     */
    @Override
    public DynamicEntity getEntity(final String fieldId) {
        final Object value = get(fieldId);
        final EntityConverter converter = (EntityConverter) ConverterFactory.getInstance(
                ).getConverter(FieldType.PREFIX_ENTITY_TYPE);
        final DynamicEntity result = converter.doConvert(value);
        return result;
    }

    /**
     * get Collection<DynamicEntity> value
     */
    @Override
    public Collection<DynamicEntity> getEntityCollection(final String fieldId) {
        final Object value = get(fieldId);
        final EntityCollectionConverter converter = (EntityCollectionConverter)
                                        ConverterFactory.getInstance().getConverter(
                FieldType.PREFIX_COLLECTION_TYPE);
        final Collection<DynamicEntity> result = converter.doConvert(value);
        return result;
    }

    /**
     * @param fieldId the field ID
     * @param dateStr the date String value
     */
    @Override
    public void setDate(final String fieldId, final String dateStr) {
        final String domainDateFormat = SystemConfigManager.getInstance().getConfigValue(
                EntityConstants.KEY_DATE_FORMAT, AuthenticationUtil.getUserWorkingDomainId());
        if (StringUtils.isEmpty(domainDateFormat)) {
            throw new RuntimeException("Fail to set date, lacking of domain date format with key: " + domainDateFormat);
        }
        final DateTime value = DateTime.parseToDateTime(dateStr, domainDateFormat);
        putValue(fieldId, value);
    }

    /**
     * @param fieldId the field ID
     * @param dateStr the date String value
     * @param format the date String format
     */
    @Override
    public void setDate(final String fieldId, final String dateStr, final String format) {
        final DateTime value = DateTime.parseToDateTime(dateStr, format);
        putValue(fieldId, value);
    }

    /**
     * @param fieldId the field ID
     * @param aYear the year integer value
     * @param aMonth the month integer value
     * @param aDay the day integer value
     */
    @Override
    public void setDate(final String fieldId, final Integer aYear, final Integer aMonth, final Integer aDay) {
        DateTime value = null;
        try {
            value = new DateTime(aYear, aMonth, aDay, 0, 0, 0, 0);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to set date with year, month and day");
        }
        putValue(fieldId, value);
    }

    /**
     * @param fieldId the field ID
     * @param dateStr the date time string
     */
    @Override
    public void setDateTime(final String fieldId, final String dateStr) {
        final String domainDateTimeFormat = SystemConfigManager.getInstance().getConfigValue(
                EntityConstants.KEY_DATE_TIME_FORMAT, AuthenticationUtil.getUserWorkingDomainId());
        if (StringUtils.isEmpty(domainDateTimeFormat)) {
            throw new RuntimeException(
                    "Fail to set date time, lacking of domain date time format with key: " + domainDateTimeFormat);
        }
        final DateTime value = DateTime.parseToDateTime(dateStr, domainDateTimeFormat);
        putValue(fieldId, value);
    }

    /**
     * @param fieldId the field ID
     * @param dateStr the date String value
     * @param format the date time String value
     */
    @Override
    public void setDateTime(final String fieldId, final String dateStr, final String format) {
        final DateTime value = DateTime.parseToDateTime(dateStr, format);
        putValue(fieldId, value);
    }

    /**
     * @param fieldId the field ID
     * @param aYear the year integer value
     * @param aMonth the month integer value
     * @param aDay the day integer value
     * @param aHour the hour integer value
     * @param aMin the min integer value
     * @param aSecond the second integer value
     */
    @Override
    public void setDateTime(final String fieldId, final Integer aYear, final Integer aMonth, final Integer aDay,
            final Integer aHour, final Integer aMin, final Integer aSecond) {
        DateTime value = null;
        try {
            value = new DateTime(aYear, aMonth, aDay, aHour, aMin, aSecond, 0);
        } catch (final Exception e) {
            throw new RuntimeException(
                   "Failed to set date time with year, month, day, hour, minute, second and nano second.");
        }
        putValue(fieldId, value);
    }

    /**
     * Get the header only level of the current modified entity document.
     * @return the header information of current entity
     */
    @Override
    public DynamicEntity getCurrentHeader() {
        final DynamicEntity result = this.cloneHeader();
        return result;
    }

    /**
     * Get the header only level of the original entity document.
     * @return the entity document before changed
     */
    @Override
    public DynamicEntity getOriginalHeader() {
        final DynamicEntity result = this.cloneHeader();
        result.putAll(this.getOriginalValueMap());
        return result;
    }

    /**
     * Shadow copy of the dynamic entity headers
     * @return the copy of dynamic entity header level.
     */
    private DynamicEntity cloneHeader() {
        try {
            final Map<String, Object> context = new HashMap<String, Object>();
            return clone(context, false);
        } catch (final DataException e) {
            throw new RuntimeException("cloneHeader()", e);
        }
    }
/*
    private void writeObject(final ObjectOutputStream out) throws IOException {
        clearAllModifiedInfo();
        out.defaultWriteObject();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        clearAllModifiedInfo();
    }
*/

    /**
     * get the flag of full entity
     */
    @Override
    public boolean isFullEntity() {
        return isFullEntity;
    }

    /**
     * set the flag of full entity
     */
    @Override
    public void setFullEntity(final boolean flag) {
      this.isFullEntity = flag;
    }

    private void clearMetaFields(final DynamicEntity e) {
        final List<String> metaDataFields = new ArrayList<String>();
        for (final String field : e.keySet()) {
            if (!isEntityField(e, field)) {
                metaDataFields.add(field);
            }
        }
        for (final String field : metaDataFields) {
            e.remove(field);
        }
    }

    /*
     * This is cause by a bug of Mybatis.
     * It will contains some fields from JDBC result set. (The field ID is in upper case)
     */
    private void clearMyBatisFields(final DynamicEntity e) {
        final List<String> metaDataFields = new ArrayList<String>();
        for (final String field : e.keySet()) {
            if (StringUtils.equals(StringUtils.upperCase(field), field)) {
                metaDataFields.add(field);
            }
        }
        for (final String field : metaDataFields) {
            e.remove(field);
        }
    }

    /*
     * deep cloned current entity with reset all UUIDs.
     */
    @Override
    public DynamicEntity copyForArchive() {
        final DynamicEntity cloneEntity = this.clone();
        resetMeta4CloningEntity(cloneEntity, true);
        return cloneEntity;
    }

    /*
     * The API to check the full entity is modified or not.
     * @return boolean
     */
    @Override
    public boolean isFullEntityModified() {
        if (isModifiedEntity()) {
            return true;
        }
        try {
            final EntityDefinition entityDefinition = EntityDefManager.getEntityDefinition(getEntityName(),
                    getEntityVersion());

            for (final FieldDefinition fieldDefinition : EntityDefModel.getChildrenFieldDefinitions(entityDefinition)) {
                final Collection<DynamicEntity> entities = getEntityCollection(fieldDefinition.getFieldId());
                if (CollectionUtils.isEmpty(entities)) {
                    continue;
                }
                for (final DynamicEntity e : entities) {
                    if (e.isNewEntity() || e.isDeletedEntity() || e.isFullEntityModified()) {
                        return true;
                    }
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException("isFullEntityModified()", e);
        }
        return false;
    }

    /**
     * get domain id
     */
    @Override
    public String getDomainId() {
        String domainId = null;
        domainId = getString(EntityConstants.PTY_DOMAIN_ID);
        if (StringUtils.isEmpty(domainId)) {
            domainId = AuthenticationUtil.getUserCurrentDomainId();
        }
        return domainId;
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#setDomainId(java.lang.String)
     */
    @Override
    public void setDomainId(final String domainId) {
        put(EntityConstants.PTY_DOMAIN_ID, domainId);
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#getHubDomainId()
     */
    @Override
    public String getHubDomainId() {
        return getString(EntityConstants.PTY_HUB_DOMAIN_ID);
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#setHubDomainId(java.lang.String)
     */
    @Override
    public void setHubDomainId(final String hubDomainId) {
        put(EntityConstants.PTY_HUB_DOMAIN_ID, hubDomainId);
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#isForReference()
     */
    @Override
    public boolean isForReference() {
        return getBoolean(EntityConstants.PTY_IS_FOR_REFERENCE, Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#setForReferenced(boolean)
     */
    @Override
    public void setForReferenced(final boolean isForReference) {
        put(EntityConstants.PTY_IS_FOR_REFERENCE, isForReference);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DynamicEntity copyByTransitiveFields(
            final Map<Integer, Object> pContext,
            final Collection<String> pTransitiveFields,
            final boolean isForReference) {
        try {
            // prepare new local variable to avoid NULL parameters
            Map<Integer, Object> context = pContext;
            if (context == null) {
                context = new HashMap<Integer, Object>();
            }
            final List<String> transitiveFields = new ArrayList<String>();
            if (pTransitiveFields != null) {
                transitiveFields.addAll(pTransitiveFields);
            }

            // Handle special value of transitive field
            if (transitiveFields.contains("NONE")) {
                // When "transitiveFields" = "NONE", means not top copy this entity and return NULL
                return null;
            } else if (transitiveFields.contains("REF")) {
                // When "transitiveFields" = "REF", means not to copy but return the current entity.
                return this;
            }

            // Not to copy for hcl/attachment/codelist module (return current entity)
            final String module = EntityDefManager.getModuleCodeByEntityName(getEntityName());
            if (NON_TRANSITIVE_MODULES.contains(module)) {
                return this;
            }

            final boolean transferAll = transitiveFields.contains("ALL");
            if (!transferAll) {
                // always clone "refNo" field
                transitiveFields.add(EntityConstants.PTY_REF_NO);
            }

            final DynamicEntityImp dei = new DynamicEntityImp(getEntityName(), getEntityVersion());
            context.put(System.identityHashCode(this), dei);

            final EntityDefinition entityDef = EntityDefManager.getEntityDefinition(
                    getEntityName(), getEntityVersion());
            for (final FieldDefinition fieldDef : entityDef.getFieldDefinitions()) {
                final String fieldId = fieldDef.getFieldId();

                if (!transferAll && !transitiveFields.contains(fieldId)) {
                    continue;
                }

                final Object value = this.get(fieldId);

                if (value instanceof Collection) {
                    // process child collection field recursively
                    final List<DynamicEntity> children = new ArrayList<DynamicEntity>();
                    for (final DynamicEntity de : (Collection<DynamicEntity>) value) {
                        // check if the entity already cloned
                        DynamicEntity eClone = (DynamicEntity) context.get(System.identityHashCode(de));
                        if (eClone == null) {
                            // Stop isForReference = 0 in collection level
                            eClone = ((DynamicEntityImp) de).copyByTransitiveFields(
                                    context, fieldDef.getTransitiveFieldLists(), Boolean.TRUE);
                        }
                        children.add(eClone);
                    }
                    dei.put(fieldId, children);
                } else if (EntityDefModel.isEntityType(fieldDef.getFieldType()) && value instanceof DynamicEntity) {
                    // process header entity field recursively
                    final DynamicEntity de = (DynamicEntity) value;
                    DynamicEntity eClone = (DynamicEntity) context.get(System.identityHashCode(de));
                    if (eClone == null) {
                        // Stop isForReference = 0 in sub-entity level
                        eClone = ((DynamicEntityImp) de).copyByTransitiveFields(
                                context, fieldDef.getTransitiveFieldLists(), Boolean.TRUE);
                    }
                    dei.put(fieldId, eClone);
                } else {
                    dei.put(fieldId, value);
                }
            }

            // reset system attributes
            dei.newEntity = Boolean.TRUE;
            dei.setForReferenced(isForReference);
            dei.setHubDomainId(AuthenticationUtil.getUserCurrentDomainId());
            dei.setInitiated();
            dei.setId(UUIDGenerator.getUUID());
            return dei;
        } catch (final Exception e) {
            throw new RuntimeException("copyForReference()", e);
        }
    }

    /* (non-Javadoc)
     * @see com.core.cbx.data.entity.DynamicEntity#resetMetaData()
     */
    @Override
    public void resetMetaData() {
        resetMeta4CloningEntity(this, false);
    }
}
