package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.core.bean.Service;
import cn.asany.shanhai.core.bean.enums.InterfaceProtocol;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ServiceServiceTest {

    @Autowired
    private NameServerService nameServerService;

    @Test
    void save() {
        Service service = Service.builder()
            .code("")
            .name("")
            .endpoint("")
            .protocol(InterfaceProtocol.GraphQL)
            .build();
        this.nameServerService.save(service);
    }
}