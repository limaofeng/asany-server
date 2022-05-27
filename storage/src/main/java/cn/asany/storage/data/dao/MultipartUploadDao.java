package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.MultipartUpload;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipartUploadDao extends JpaRepository<MultipartUpload, Long> {}
