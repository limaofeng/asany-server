package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.utils.UploadUtils;
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
    private UploadService uploadService;

    public FileDetail upload(Part part, UploadOptions options, DataFetchingEnvironment env) throws IOException {
        FileObject object = UploadUtils.partToObject(part);
        return (FileDetail) uploadService.upload(object, options);
    }

}
