package cn.asany.drive.dao;

import cn.asany.drive.domain.CloudDrive;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudDriveDao extends JpaRepository<CloudDrive, Long> {}
