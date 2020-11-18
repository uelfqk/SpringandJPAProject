package jpabook.jpashop.query;

import jpabook.jpashop.query.builder.UpdateNativeQueryBuilder;
import jpabook.jpashop.query.domain.TMember;
import jpabook.jpashop.query.domain.TTeam;
import jpabook.jpashop.query.builder.SelectNativeQueryBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class MakeNativeQueryTest {

//    @PersistenceContext
//    private EntityManager em;

    @Test
    @DisplayName(value = "리플렉션 테스트")
    void reflectionTest() throws Exception {
        try {
            Class entity = Class.forName("jpabook.jpashop.query.domain.TMember");
            Object o = entity.newInstance();

            Method setName = entity.getDeclaredMethod("setName", String.class);
            setName.invoke(o, "string");

            Field name = entity.getDeclaredField("name");
            name.setAccessible(true);
            Object findName = name.get(o);

//            name.set(entity, 1);

            System.out.println("name = " + findName);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }


    @Test
    void makeNativeQueryBasicSelect() {
        // given
        String result = "";

        //when
        result = SelectNativeQueryBuilder
                .createBy()
                .selectFrom(TMember.TableName.name)
                .where(TMember.FieldName.code, 0)
                .build();

        //then
        Assertions
                .assertThat(result)
                .isEqualTo("SELECT * FROM MEMBER a WHERE a.member_id = '0';");
    }

    @Test
    void makeNativeQueryInnerJoin() {
        //given
        String result = "";

        //when
        result = SelectNativeQueryBuilder
                .createBy()
                .selectFrom(TMember.TableName.name)
                .innerJoin(TTeam.TableName.name, TMember.FieldName.code, TTeam.FieldName.code)
                .where(TMember.FieldName.teamId, 0)
                .build();

        //then
        Assertions
                .assertThat(result)
                .isEqualTo("SELECT * FROM MEMBER a INNER JOIN TEAM b ON a.member_id = b.team_id WHERE a.team_id = '0';");
    }

    @Test
    void makeNativeQueryUpdateBasic() {
        //given
        String result = "";

        //when
        result = UpdateNativeQueryBuilder
                .createBy()
                .update(TMember.TableName.name)
                .value(TMember.FieldName.name, "root")
                .where(TMember.FieldName.code, 0)
                .build();

        //then
        Assertions.assertThat(result).isEqualTo("UPDATE MEMBER SET name = 'root' WHERE member_id = '0';");
    }

    public boolean sendQuery(String query) {
        System.out.println("query = " + query);
        return true;
    }

    @Test
    void queryTest() {
        //given

        String select = SelectNativeQueryBuilder
                .createBy()
                .selectFrom(TMember.TableName.name)
                .where(TMember.FieldName.code, 0)
                .build();

        sendQuery(select);

        //when
        String update = UpdateNativeQueryBuilder
                .createBy()
                .update(TMember.TableName.name)
                .value(TMember.FieldName.name, "root")
                .build();

        sendQuery(update);

        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put(TMember.FieldName.name, "root");
                put(TMember.FieldName.age, 15);
            }
        };

        String updates = UpdateNativeQueryBuilder
                .createBy()
                .update(TMember.TableName.name)
                .values(map)
                .where(TMember.FieldName.code, 0)
                .build();

        sendQuery(updates);

        //then

        Assertions.assertThat(select).isEqualTo("SELECT * FROM MEMBER a WHERE a.member_id = '0';");
        Assertions.assertThat(update).isEqualTo("UPDATE MEMBER SET name = 'root';");
        Assertions.assertThat(updates).isEqualTo("UPDATE MEMBER SET name = 'root' age = '15' WHERE member_id = '0';");
    }
}
