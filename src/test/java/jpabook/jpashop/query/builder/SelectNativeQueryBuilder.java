package jpabook.jpashop.query.builder;

public class SelectNativeQueryBuilder {

    private final StringBuilder query;

    private SelectNativeQueryBuilder() {
        this.query = new StringBuilder();
    }

    public static SelectNativeQueryBuilder createBy(String entityName) {
        try {
            Class entity = Class.forName("jpabook.jpashop.query.domain." + entityName);
            entity.getDeclaredField("");
        } catch (Exception e) {
            System.out.println("e = " + e.toString());
        }

        SelectNativeQueryBuilder builder = new SelectNativeQueryBuilder();

        return builder;
    }

    public static SelectNativeQueryBuilder createBy() {
        try {
            Class c = Class.forName("jpabook.jpashop.query.domain.TMember");
            c.getDeclaredField("name");
        } catch (Exception e) {
            System.out.println("e = " + e.toString());
        }
        SelectNativeQueryBuilder builder = new SelectNativeQueryBuilder();
        return builder;
    }

    public SelectNativeQueryBuilder selectFrom(String tableName) {
        query.append("SELECT * FROM ").append(tableName).append(" a ");
        return this;
    }

    public SelectNativeQueryBuilder where(String fieldName, Object value) {
        query.append("WHERE a.")
                .append(fieldName)
                .append(" = '")
                .append(value)
                .append("' ");
        return this;
    }

    public SelectNativeQueryBuilder innerJoin(String tableName, String firstFieldName, String lastFieldName) {
        query.append("INNER JOIN ")
                .append(tableName)
                .append(" b ON a.")
                .append(firstFieldName)
                .append(" = b.")
                .append(lastFieldName)
                .append(" ");
        return this;
    }

    public SelectNativeQueryBuilder leftJoin() {
        return this;
    }

    public SelectNativeQueryBuilder rightJoin() {
        return this;
    }

    public String build() {
        query.deleteCharAt(query.length() - 1);
        query.append(";");
        return query.toString();
    }
}
