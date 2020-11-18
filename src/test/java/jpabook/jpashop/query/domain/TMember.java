package jpabook.jpashop.query.domain;

public class TMember {
    public static class TableName {
        public static String name = "MEMBER";
    }

    public static class FieldName {
        public static String code = "member_id";
        public static String name = "name";
        public static String age = "age";
        public static String teamId = "team_id";
    }

    private int code;
    private String name;
    private int age;

    private TTeam team;

    public String aaa;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public TTeam getTeam() {
        return team;
    }

    public void setTeam(TTeam team) {
        this.team = team;
    }

    public static TMember createMember(int code, String name, int age) {
        TMember member = new TMember();
        member.setCode(code);
        member.setName(name);
        member.setAge(age);
        return member;
    }
}
