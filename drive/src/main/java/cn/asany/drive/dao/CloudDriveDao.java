package cn.asany.drive.dao;

import cn.asany.drive.domain.CloudDrive;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudDriveDao extends AnyJpaRepository<CloudDrive, Long> {}
