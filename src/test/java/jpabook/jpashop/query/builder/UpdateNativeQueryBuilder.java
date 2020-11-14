package jpabook.jpashop.query.builder;

import java.util.Map;

public class UpdateNativeQueryBuilder {

    private final StringBuilder query;

    private UpdateNativeQueryBuilder() {
        query = new StringBuilder();
    }

    public static UpdateNativeQueryBuilder createBy() {
        UpdateNativeQueryBuilder builder = new UpdateNativeQueryBuilder();
        return builder;
    }

    public UpdateNativeQueryBuilder update(String tableName) {
        query.append("UPDATE ")
                .append(tableName)
                .append(" SET ");
        return this;
    }

    public UpdateNativeQueryBuilder value(String fieldName, Object value) {
        query.append(fieldName)
                .append(" = '")
                .append(value)
                .append("' ");
        return this;
    }

    public UpdateNativeQueryBuilder values(Map<String, Object> values) {
        for (String s : values.keySet()) {
            query.append(s)
                    .append(" = '")
                    .append(values.get(s))
                    .append("' ");
        }
        return this;
    }

    public UpdateNativeQueryBuilder where(String fieldName, Object value) {
        query.append("WHERE ")
                .append(fieldName)
                .append(" = '")
                .append(value)
                .append("' ");
        return this;
    }

    public String build() {
        query.deleteCharAt(query.length() - 1);
        query.append(";");
        return query.toString();
    }
}
