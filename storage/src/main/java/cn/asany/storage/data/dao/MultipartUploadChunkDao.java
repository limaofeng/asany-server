package cn.asany.storage.data.dao;

import cn.asany.storage.data.bean.MultipartUploadChunk;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipartUploadChunkDao extends JpaRepository<MultipartUploadChunk, String> {}
