package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.MultipartUpload;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipartUploadDao extends AnyJpaRepository<MultipartUpload, Long> {}
