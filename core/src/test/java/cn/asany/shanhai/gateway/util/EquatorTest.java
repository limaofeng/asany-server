package cn.asany.shanhai.gateway.util;

import cn.asany.shanhai.gateway.bean.ServiceSchemaVersionPatch;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class EquatorTest {

    String SCHEMA_PATH = "/Users/limaofeng/Workspace/whir/kuafu/core/src/test/resources/";

    @Test
    void isEquals() {

    }

    @Test
    void kong() {
        GraphQLSchema.GraphQLSchemaBuilder builder = GraphQLSchema.builder();
        builder.schema("type Query {} type Mutation {} type Subscription {}");

        GraphQLSchema schema = builder.build();

        schema.removeType("Mutation");
        schema.removeType("Subscription");
//        System.out.println(schema.print());
    }

    @Test
    void dependencies() {
        GraphQLSchema.GraphQLSchemaBuilder prevBuilder = GraphQLSchema.builder();
        prevBuilder.schema("type Query {} type Mutation {} type Subscription {}");

        GraphQLSchema prevSchema = prevBuilder.build();

        GraphQLSchema.GraphQLSchemaBuilder xbuilder = GraphQLSchema.builder();
        xbuilder.schema(FileUtil.readFile(SCHEMA_PATH + "schema.text"));

        GraphQLSchema schema = xbuilder.build();

        String[] ignoreProperties = new String[]{"Department", "Role", "GrantPermission", "Position"};

        Set<String> dependencies = schema.dependencies("Query.viewer", ignoreProperties);

        System.out.println(dependencies.size());

        //        changelist

//        Set<String> all = new LinkedHashSet<>(ObjectUtil.toFieldList(items, "name", new ArrayList<String>()));

//        Set<String> intersection = Equator.intersection(dependencies, all);

//        System.out.println(intersection);
    }

    @Test
    void diff() {

        GraphQLSchema schema = SchemaUtils.loadSchema(FileUtil.readFile(SCHEMA_PATH + "schema.text"));

        GraphQLSchema newSchema = SchemaUtils.loadSchema(FileUtil.readFile(SCHEMA_PATH + "schema_new.text"));

        List<DiffObject> diffs = Equator.diff(schema, newSchema);

        List<ServiceSchemaVersionPatch> patches = SchemaUtils.diff(schema, newSchema);

        SchemaUtils.findDifference(diffs, "Mutation.login");

        Optional<DiffObject> typeMap = ObjectUtil.filter(diffs, "path", "/typeMap").stream().findAny();
        if (typeMap.isPresent()) {
            for (DiffObject diffObject : typeMap.get().getDiffObjects()) {
                if (diffObject.getStatus() == DiffObject.DiffStatus.D) {
                    schema.removeType((GraphQLObjectType) diffObject.getPrev());
                }
            }
        }
        Optional<DiffObject> queryType = ObjectUtil.filter(diffs, "path", "/queryType").stream().findAny();
        if (queryType.isPresent()) {
            DiffObject queryDiffObject = ObjectUtil.filter(queryType.get().getDiffObjects(), "path", "/queryType/fieldMap").stream().findAny().get();
            GraphQLObjectType query = (GraphQLObjectType) queryType.get().getPrev();
            List<DiffObject> queries = queryDiffObject.getDiffObjects();
            for (DiffObject diffObject : queries) {
                if (diffObject.getStatus() == DiffObject.DiffStatus.D) {
                    GraphQLField fieldDefinition = (GraphQLField) diffObject.getPrev();
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