package jpabook.jpashop.query.domain;

import java.util.ArrayList;
import java.util.List;

public class TTeam {

    public static class TableName {
        public static String name = "TEAM";
    }

    public static class FieldName {
        public static String code = "team_id";
        public static String name = "name";
    }

    private int code;
    private String name;

    private List<TMember> members = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TMember> getMembers() {
        return members;
    }

    public void setMembers(List<TMember> members) {
        this.members = members;
    }
}
