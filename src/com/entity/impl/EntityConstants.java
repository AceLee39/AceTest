// Copyright (c) 1998-2013 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.39
// ============================================================================
// CHANGE LOG
// CNT.5.0.039 : 2013-04-15, randy.huang, CNT-8960
// CNT.5.0.038 : 2013-03-22, aron.tang, CNT-8654
// CNT.5.0.037 : 2013-02-22, colin.guo, CNT-8273
// CNT.5.0.037 : 2013-02-19, mark.lin, revision for CNT-8271
// CNT.5.0.036 : 2013-01-18, colin.guo, CNT-7813
// CNT.5.0.035 : 2012-12-29, Real.Ji, CNT-7509
// CNT.5.0.034 : 2012-12-10, wilson.lun, CNT-7207
// CNT.5.0.031 : 2012-10-29, Randy.Huang, CNT-5733
// CNT.5.0.029 : 2012-09-24, aron.tang, CNT-6098
// CNT.5.0.029 : 2012-09-18, tim.chan, CNT-6030
// CNT.5.0.029 : 2012-09-17, raymond.chiu, CNT-5998
// CNT.5.0.027 : 2012-08-25, aron.tang,  CNT-5662
// CNT.5.0.025 : 2012-08-07, aron.tang, CNT-5402
// CNT.5.0.022 : 2012-07-17, aron.tang, CNT-5030
// CNT.5.0.018 : 2012-05-31, Andy.Li, CNT-4141
// CNT.5.0.1 : 2012-05-09, andy.li, Creation CNT-3696
// ============================================================================
package com.core.cbx.data.entity;

/**
 * @author andy.li
 *
 */
public interface EntityConstants {

    String PTY_ID = "id";
    String PTY_REVISION = "revision";
    String PTY_ENTITY_NAME = "entityName";
    String PTY_ENTITY_VERSION = "entityVersion";
    String PTY_VERSION = "version";
    String PTY_REF_NO = "refNo";
    String PTY_DOMAIN_ID = "domainId";
    String PTY_IS_LATEST = "isLatest";
    String PTY_CREATED_ON = "createdOn";
    String PTY_CREATE_USER = "createUser";
    String PTY_UPDATED_ON = "updatedOn";
    String PTY_UPDATE_USER = "updateUser";
    String PTY_STATUS = "status";
    String PTY_DOC_STATUS = "docStatus";
    String PTY_EDITING_STATUS = "editingStatus";
    String PTY_LOCKING_STATUS = "lockingStatus";
    String PTY_INTERNAL_STATUS = "internalStatus";
    String PTY_INTERNAL_SEQ_NO = "internalSeqNo";
    String PTY_WORKING_DOMAIN_ID = "workingDomainId";
    String PTY_INTEGRATION_STATUS = "integrationStatus";
    String PTY_INTEGRATION_NOTE = "integrationNote";
    String PTY_INTEGRATION_SOURCE = "integrationSource";

    String PTY_PARENT_ENTITY = "parentEntity";
    String PTY_FIELD_ID = "fieldId";

    String PTY_META_DATA_COUNT = "metaDataCount";

    String PTY_HUB_DOMAIN_ID = "hubDomainId";
    String PTY_IS_FOR_REFERENCE = "isForReference";

    String ATTR_ADDED = "added";
    String ATTR_DELETED = "deleted";
    String ATTR_MODIFIED = "modified";
    String ATTR_RESTRICTION = "restriction";
    String ATTR_ORDER_BY = "orderBy";
    String ATTR_WHERE_CLAUSE = "whereClause";
    String ATTR_VIEW_ID = "viewId";
    String ATTR_USER = "user";
    String ATTR_USER_ID = "userId";
    String ATTR_SELECTED_FIELDS = "selectFields";
    String ATTR_DENIED_FIELDS = "deniedFields";
    String ATTR_CUSTOM_FIELDS = "customFields";
    String ATTR_SQL = "sql";
    String ATTR_SELECT_COUNT_ONLY = "selectCountOnly";

    String COLUMN_NAME_ID = "ID";
    String COLUMN_NAME_DOMAIN_ID = "DOMAIN_ID";
    String COLUMN_NAME_REVISION = "REVISION";
    String COLUMN_NAME_REF_ENTITY_NAME = "REF_ENTITY_NAME";

    String SEPARATOR_DOT = ".";
    String SEPARATOR_COMMA = ",";
    String SEPARATOR_UNDERSCORE = "_";
    String SEPARATOR_HYPHEN = "-";
    String SEPARATOR_DOLLAR_SIGN = "$";
    String SEPARATOR_NUMBER_SIGN = "#";
    String SEPARATOR_DOUBLE_QUOTE = "\"";
    String SEPARATOR_EQ = "=";
    String SEPARATOR_AT = "@";
    String SEPARATOR_DOUBLE_COLON = "::";
    String SEPARATOR_COLON = ":";
    String SEPARATOR_LEFT_BRACKET = "(";
    String SEPARATOR_RIGHT_BRACKET = ")";
    String SEPARATOR_SPACE = " ";
    String SEPARATOR_SLASH = "/";

    String KEY_DATE_FORMAT = "ui.format.date";
    String KEY_DATE_TIME_FORMAT = "ui.format.datetime";

    String ENCODING_UTF8 = "UTF-8";

    public interface DocStatus {
        String ACTIVE = "active";
        String INACTIVE = "inactive";
        String CANCELED = "canceled";
    }

    public interface EditingStatus {
        String DRAFT = "draft";
        String PENDING = "pending";
        String CONFIRMED = "confirmed";
        String INVALID = "invalid";
    }

    public interface LockingStatus {
        String LOCKED = "$locked";
        String UNLOCKED = "$unlocked";
        String LOCKED_BY_OTHERS = "$lockedByOthers";
    }

    public interface InternalStatus {
        String NEW = "$new";
        String LIST = "$list";
    }
}
