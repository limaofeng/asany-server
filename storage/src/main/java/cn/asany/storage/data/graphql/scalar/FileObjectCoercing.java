package cn.asany.storage.data.graphql.scalar;

import cn.asany.storage.data.bean.FileDetail;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2020/3/7 7:44 下午
 */
@Slf4j
public class FileObjectCoercing implements Coercing<FileDetail, Object> {

    private String fileServerUrl;

    public FileObjectCoercing() {
        this.fileServerUrl = fileServerUrl;
    }

    @Override
    public Object serialize(Object input) throws CoercingSerializeException {
        return input instanceof FileDetail ? input : input;
    }

    @Override
    public FileDetail parseValue(Object input) throws CoercingParseValueException {
        String fileId = null;
        if (input instanceof String) {
            fileId = input.toString();
        }

        if (input instanceof StringValue) {
            fileId = ((StringValue) input).getValue();
        }

        if (fileId == null) {
            return null;
        }
//            HttpResponse<FileObject> response = Unirest.get(this.fileServerUrl + "/files/" + fileId).asObject(FileObject.class);
//            return response.getBody();
        return null;
    }

    @Override
    public FileDetail parseLiteral(Object input) throws CoercingParseLiteralException {
        return parseValue(input);
    }
}
