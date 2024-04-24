package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.MultipartUploadChunk;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipartUploadChunkDao extends AnyJpaRepository<MultipartUploadChunk, String> {}
