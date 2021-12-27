package cn.zhuguoqing.operationLog.bean.dto;


import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/15 16:39
 * @Desription: 建造者模式的DiffDTO, 后期有扩展, 直接继承该类还能在 IDiffProcessor 类的参数中进行传递
 * @Version: 1.0
 */
@Getter
@Slf4j
public class DiffDTO {

    /**
     * exmple: product.g_prod_spu,|必传|
     */
    private String schemaTableName;

    private String schema;

    private String table;

    /**
     * 键 example where {键} = 123;
     */
    private String keyName;

    /**
     * 值 example:where id = {值}
     */
    private List<String> keyValue;

    /**
     * SQL查询后拼接的内容,|可以不传|,example:where id = 1 and {is_deleted = 0};
     */
    private String appendSQLAfterWhere;

    /**
     * 记录的字段名,|可以不传|
     */
    private String diffName;

    /**
     * 关于什么的信息,|可以不传|,
     */
    private String informationAboutWhat;

    /**
     * 需要记录的字段,|可以不传|,直接使用数据库的字段
     */
    private List<String> includeRecordClms;

    /**
     * 不需要记录的字段,|可以不传|,直接使用数据库的字段
     */
    private List<String> excludeRecordClms;

    /**
     * 自定义关于字段的名称的函数,|可以不传|
     */
    private Map<String, Function<String, String>> customCommentFunctionMap;

    /**
     * 自定义关于字段的值的函数,|可以不传|
     */
    private Map<String, Function<String, String>> customValueFunctionMap;

    private DiffDTO(Builder builder) {
        this.schemaTableName = builder.schemaTableName;
        this.schema = builder.schema;
        this.table = builder.table;
        this.keyName = builder.keyName;
        this.keyValue = builder.keyValue;
        this.appendSQLAfterWhere = builder.appendSQLAfterWhere;
        this.diffName = builder.diffName;
        this.includeRecordClms = builder.includeRecordClms;
        this.excludeRecordClms = builder.excludeRecordClms;
        this.informationAboutWhat = builder.informationAboutWhat;
        this.customCommentFunctionMap = builder.customCommentFunctionMap;
        this.customValueFunctionMap = builder.customValueFunctionMap;
    }

    public static class Builder {

        private static final String KEY_NAME = "id";

        private DiffType diffType;

        private String schemaTableName;

        private String schema;

        private String table;

        private String keyName = KEY_NAME;

        private List<String> keyValue;

        private String appendSQLAfterWhere;

        private String diffName;

        private List<String> includeRecordClms;

        private List<String> excludeRecordClms;

        private String informationAboutWhat;

        private final Map<String, Function<String, String>> customCommentFunctionMap = new HashMap<>(8);

        private final Map<String, Function<String, String>> customValueFunctionMap = new HashMap<>(8);

        public DiffDTO build() {
            try {
                // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
                if (Objects.isNull(diffType)) {
                    diffType = DiffType.SINGLE_UPDATE;
                }
                if (StringUtils.isEmpty(schema)) {
                    throw new IllegalArgumentException("schemaTableName cannot be null");
                }
                if (StringUtils.isEmpty(table)) {
                    throw new IllegalArgumentException("schemaTableName cannot be null");
                }
                if (StringUtils.isEmpty(appendSQLAfterWhere)) {
                    appendSQLAfterWhere = " ";
                } else {
                    if (!appendSQLAfterWhere.trim().startsWith("and")) {
                        appendSQLAfterWhere = " and " + appendSQLAfterWhere;
                    }
                }
                if (diffType.getType().equals(DiffType.LIST_ADD_DELETE.getType()) && StringUtils.isEmpty(diffName)) {
                    throw new IllegalArgumentException("当使用list_add_delete的diff时候 diffName不能为空");
                }
                if (!CollectionUtils.isEmpty(includeRecordClms) && !CollectionUtils.isEmpty(excludeRecordClms)) {
                    throw new IllegalArgumentException("includeRecordClms与excludeRecordClms不能同时存在");
                }
            } catch (Exception e) {
                log.error("--OperationLog--DiffDTO build error", e);
            }
            return new DiffDTO(this);
        }

        /**
         * DiffType |必传|
         *
         * @param diffType
         * @return
         */
        public Builder setDiffType(DiffType diffType) {
            try {
                if (Objects.isNull(diffType)) {
                    throw new IllegalArgumentException("diffType cannot be null");
                }
                this.diffType = diffType;
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setDiffType error", e);
            }
            return this;
        }

        /**
         * exmple: product.g_prod_spu,|必传|
         *
         * @param schemaTableName
         * @return
         */
        public Builder setSchemaTableName(String schemaTableName) {
            try {
                if (StringUtils.isEmpty(schemaTableName)) {
                    throw new IllegalArgumentException("schemaTableName cannot be null");
                }
                if (!schemaTableName.contains(".")) {
                    throw new IllegalArgumentException("schemaTableName的格式需要设置为:schemaA.tableB");
                }
                String[] split = schemaTableName.split("\\.");
                if (split.length != 2) {
                    throw new IllegalArgumentException("schemaTableName的格式需要设置为:schemaA.tableB");
                }
                this.schemaTableName = schemaTableName;
                schema = split[0];
                table = split[1];
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setSchemaTableName error", e);
            }
            return this;
        }

        /**
         * 键 example:where {键} = 123
         *
         * @param keyName
         * @return
         */
        public Builder setKeyName(String keyName) {
            try {
                if (StringUtils.isEmpty(keyName)) {
                    throw new IllegalArgumentException("keyName cannot be null");
                }
                this.keyName = keyName;
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setKeyName error", e);
            }
            return this;
        }

        /**
         * 值 example:where id = {值}
         *
         * @param keyValue
         * @return
         */
        public Builder setKeyValue(String... keyValue) {
            try {
                if (Objects.isNull(keyValue)) {
                    throw new IllegalArgumentException("keyValue cannot be null");
                }
                if (keyValue.length == 0) {
                    throw new IllegalArgumentException("keyValue size cannot be 0");
                }
                this.keyValue = new ArrayList<>(Arrays.asList(keyValue));
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setKeyValue error", e);
            }
            return this;
        }

        /**
         * SQL查询后拼接的内容,|可以不传|,example:where id = 1 and {is_deleted = 0};
         *
         * @param appendSQLAfterWhere
         * @return
         */
        public Builder setAppendSQLAfterWhere(String appendSQLAfterWhere) {
            try {
                if (StringUtils.isEmpty(appendSQLAfterWhere)) {
                    throw new IllegalArgumentException("appendSQLAfterWhere cannot be null");
                }
                this.appendSQLAfterWhere = appendSQLAfterWhere;
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setAppendSQLAfterWhere error", e);
            }
            return this;
        }

        /**
         * 记录的字段名,|可以不传|
         *
         * @param diffName
         * @return
         */
        public Builder setDiffName(String diffName) {
            try {
                if (StringUtils.isEmpty(diffName)) {
                    throw new IllegalArgumentException("diffName cannot be null");
                }
                this.diffName = diffName;
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setDiffName error", e);
            }
            return this;
        }

        /**
         * 需要记录的字段,|可以不传|,直接使用数据库的字段
         *
         * @param includeRecordClm
         * @return
         */
        public Builder setIncludeRecordClms(String... includeRecordClm) {
            try {
                if (includeRecordClm == null) {
                    throw new IllegalArgumentException("includeClms cannot be null");
                }
                if (includeRecordClm.length == 0) {
                    throw new IllegalArgumentException("includeClms size cannot be 0");
                }
                this.includeRecordClms = new ArrayList<>(Arrays.asList(includeRecordClm));
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setIncludeRecordClms error", e);
            }
            return this;
        }

        /**
         * 不需要记录的字段,|可以不传|,直接使用数据库的字段
         *
         * @param excludeRecordClm
         * @return
         */
        public Builder setExcludeRecordClms(String... excludeRecordClm) {
            try {
                if (excludeRecordClm == null) {
                    throw new IllegalArgumentException("excludeClms cannot be null");
                }
                if (excludeRecordClm.length == 0) {
                    throw new IllegalArgumentException("excludeClms size cannot be 0");
                }
                this.excludeRecordClms = new ArrayList<>(Arrays.asList(excludeRecordClm));
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setExcludeRecordClms error", e);
            }
            return this;
        }

        /**
         * 关于什么的信息,|可以不传|,
         * example:关于[规格编码为:194276537401347]的限价
         *
         * @param informationAboutWhat
         * @return
         */
        public Builder setInformationAboutWhat(String informationAboutWhat) {
            try {
                if (StringUtils.isEmpty(informationAboutWhat)) {
                    throw new IllegalArgumentException("informationAboutWhat cannot be null");
                }
                this.informationAboutWhat = informationAboutWhat;
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- setInformationAboutWhat error", e);
            }
            return this;
        }

        /**
         * 自定义的函数,|可以不传|
         *
         * @param columnName
         * @param function
         * @return
         */
        public Builder addCustomFunction(CustomFunctionType type, Function<String, String> function, String... columnName) {
            try {
                if (Objects.isNull(columnName)) {
                    throw new IllegalArgumentException("columnName cannot be null");
                }
                if (Objects.isNull(function)) {
                    throw new IllegalArgumentException("function cannot be null");
                }
                if (Objects.isNull(type)) {
                    type = CustomFunctionType.VALUE;
                }
                if (type.getType().equals(CustomFunctionType.KEY)) {
                    for (String s : columnName) {
                        customCommentFunctionMap.put(s, function);
                    }

                } else {
                    for (String s : columnName) {
                        customValueFunctionMap.put(s, function);
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error("--OperationLog-- addCustomFunction error", e);
            }
            return this;
        }


    }
}
