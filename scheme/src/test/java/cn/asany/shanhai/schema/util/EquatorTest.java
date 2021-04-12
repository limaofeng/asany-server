package cn.asany.shanhai.schema.util;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class EquatorTest {

    String SCHEMA_PATH = "/Users/limaofeng/Workspace/whir/kuafu/scheme/src/test/resources/";

    @Test
    void isEquals() {

    }

    @Test
    void diff() {
        GraphQLSchemaDefinition.GraphQLSchemaBuilder builder = GraphQLSchemaDefinition.builder();
        builder.schema(FileUtil.readFile(SCHEMA_PATH + "schema.text"));

        GraphQLSchemaDefinition schema = builder.build();

        GraphQLSchemaDefinition.GraphQLSchemaBuilder newBuilder = GraphQLSchemaDefinition.builder();
        newBuilder.schema(FileUtil.readFile(SCHEMA_PATH + "schema_new.text"));

        GraphQLSchemaDefinition newSchema = newBuilder.build();

        List<DiffObject> diffs = Equator.diff(schema, newSchema);
        Optional<DiffObject> typeMap = ObjectUtil.filter(diffs, "path", "/typeMap").stream().findAny();
        if (typeMap.isPresent()) {
            for (DiffObject diffObject : typeMap.get().getDiffObjects()) {
                if (diffObject.getStatus() == DiffObject.DiffStatus.D) {
                    schema.removeType((GraphQLTypeDefinition) diffObject.getPrev());
                }
            }
        }
        Optional<DiffObject> queryType = ObjectUtil.filter(diffs, "path", "/queryType").stream().findAny();
        if (queryType.isPresent()) {
            DiffObject queryDiffObject = ObjectUtil.filter(queryType.get().getDiffObjects(), "path", "/queryType/fieldMap").stream().findAny().get();
            GraphQLTypeDefinition query = (GraphQLTypeDefinition) queryType.get().getPrev();
            List<DiffObject> queries = queryDiffObject.getDiffObjects();
            for (DiffObject diffObject : queries) {
                if (diffObject.getStatus() == DiffObject.DiffStatus.D) {
                    GraphQLFieldDefinition fieldDefinition = (GraphQLFieldDefinition) diffObject.getPrev();
                    query.remove(fieldDefinition);
                    schema.removeField(query, fieldDefinition);
                }
            }
        }

        String xx = schema.print();

        System.out.println(xx);

    }


    @Test
    void setCompare() {

        Set result = new HashSet();
        Set set1 = new HashSet() {{
            add(1);
            add(3);
            add(4);
        }};
        System.out.println("set1 = " + set1.toString());

        Set set2 = new HashSet() {{
            add(1);
            add(2);
            add(3);
        }};
        System.out.println("set2 = " + set2.toString());

        System.out.println("交集：" + Equator.intersection(set1, set2));

        System.out.println("差集：" + Equator.differenceSet(set1, set2));
        System.out.println("差集：" + Equator.differenceSet(set2, set1));

        System.out.println("并集：" + Equator.union(set1, set2));
    }
}