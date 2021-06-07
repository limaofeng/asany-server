package cn.asany.storage.data.graphql;

import cn.asany.storage.core.FileUploadService;
import cn.asany.storage.core.UploadOptions;
import cn.asany.storage.data.bean.FileDetail;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @author limaofeng
 */
@Component
public class StorageGraphQLGraphQLMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private FileUploadService uploadService;

    public FileDetail upload(Part part, UploadOptions options, DataFetchingEnvironment env) throws IOException {
        return uploadService.upload(part, options);
    }

}
