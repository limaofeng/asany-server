package cn.asany.weixin.framework.session;

import org.jfantasy.framework.jackson.JSON;
import cn.asany.weixin.ApplicationTest;
import cn.asany.weixin.framework.core.Openapi;
import cn.asany.weixin.framework.factory.WeixinSessionFactory;
import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import cn.asany.weixin.framework.message.content.Menu;
import cn.asany.weixin.framework.message.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("dev")
public class WeixinSessionTest {


    private final static Log LOG = LogFactory.getLog(WeixinSessionTest.class);

    @Autowired
    private WeixinSessionFactory weixinSessionFactory;

    @Test
    public void getOauth2User() throws Exception {
        try {
            WeixinSession weixinSession = WeixinSessionUtils.saveSession(weixinSessionFactory.openSession("wx958ba7d1684203b3"));
            Openapi openapi = weixinSession.getOpenapi();
            User user = openapi.getUser("021FujDQ14SUH61aBLBQ12wyDQ1FujD2");
            LOG.debug(JSON.serialize(user));
        } finally {
            WeixinSessionUtils.closeSession();
        }
    }

    @Test
    public void refreshMenu() throws Exception {
        WeixinSession weixinSession = WeixinSessionUtils.saveSession(weixinSessionFactory.openSession("wx958ba7d1684203b3"));
        weixinSession.refreshMenu();
    }

    @Test
    public void clearMenu() throws Exception {
        WeixinSession weixinSession = WeixinSessionUtils.saveSession(weixinSessionFactory.openSession("wx958ba7d1684203b3"));
        weixinSession.clearMenu();
    }

    @Test
    public void getMenu() throws Exception{
        WeixinSession weixinSession = WeixinSessionUtils.saveSession(weixinSessionFactory.openSession("wx958ba7d1684203b3"));
        List<Menu> menus = weixinSession.getMenus();
        System.out.println(menus);
    }

}