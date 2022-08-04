package cn.asany.workflow.screen.service;

import java.util.ArrayList;
import java.util.List;
import cn.asany.pm.field.bean.IssueField;
import cn.asany.pm.field.bean.IssueFieldConfigurationItem;
import cn.asany.pm.field.dao.IssueFieldConfigurationItemDao;
import cn.asany.pm.field.dao.IssueFieldDao;
import cn.asany.pm.screen.bean.FieldToScreen;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.bean.IssueScreenTabPane;
import cn.asany.pm.screen.dao.FieldToScreenDao;
import cn.asany.pm.screen.dao.IssueScreenDao;
import cn.asany.pm.screen.dao.IssueScreenTabPaneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: FieldToScreenService @Description: 将字段添加至页面(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FieldToScreenService {

  @Autowired private FieldToScreenDao fieldToScreenDao;

  @Autowired private IssueScreenDao issueScreenDao;

  @Autowired private IssueFieldDao issueFieldDao;

  @Autowired private IssueScreenTabPaneDao issueScreenTabPaneDao;
  @Autowired private IssueFieldConfigurationItemDao itemDao;

  /**
   * @ClassName: FieldToScreenService @Description: 将字段添加到界面
   *
   * @param screen 页面id
   * @param field 字段id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean createIssueScreenField(Long screen, Long field) {
    FieldToScreen fieldToScreen = this.setFieldToScreen(screen, null, field);
    FieldToScreen save = fieldToScreenDao.save(fieldToScreen);
    if (save != null) {
      return true;
    }
    return false;
  }

  /**
   * @ClassName: FieldToScreenService @Description: 删除已分配到界面的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenField(Long screen, Long field) {
    List<FieldToScreen> all =
        fieldToScreenDao.findAll(
            Example.of(
                FieldToScreen.builder()
                    .screen(IssueScreen.builder().id(screen).build())
                    .field(
                        IssueFieldConfigurationItem.builder()
                            .field(IssueField.builder().id(field).build())
                            .build())
                    .build()));
    if (all.size() > 0) {
      fieldToScreenDao.deleteAll(all);
      return true;
    }
    return false;
  }

  /**
   * @ClassName: FieldToScreenService @Description: 根据页面id，查询该页面拥有的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueField> getIssueFields(Long id) {
    // 根据页面id，查询该页面拥有的字段
    List<FieldToScreen> all =
        fieldToScreenDao.findAll(
            Example.of(
                FieldToScreen.builder().screen(IssueScreen.builder().id(id).build()).build()));
    List<IssueField> issueFields = new ArrayList<>();
    if (all.size() > 0) {
      for (FieldToScreen list : all) {
        // 根据字段id，查询该字段是否存在
        IssueField issueField = issueFieldDao.getOne(list.getField().getId());
        issueFields.add(issueField);
      }
      return issueFields;
    }
    return null;
  }

  /**
   * @ClassName: FieldToScreenService @Description: 根据TabPane的id，查询该TabPane拥有的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<FieldToScreen> getTabPaneFields(Long id) {
    return fieldToScreenDao.findAll(
        Example.of(
            FieldToScreen.builder().tabPane(IssueScreenTabPane.builder().id(id).build()).build()));
  }

  /**
   * @ClassName: FieldToScreenService @Description: 将字段添加到界面中的TabPane
   *
   * @param screen 页面id
   * @param tabPane tabPane的id
   * @param field 字段的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean createIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    FieldToScreen fieldToScreen = this.setFieldToScreen(screen, tabPane, field);
    FieldToScreen save = fieldToScreenDao.save(fieldToScreen);
    if (save != null) {
      return true;
    }
    return false;
  }

  /** 给FieldToScreen设置值 */
  public FieldToScreen setFieldToScreen(Long screen, Long tabPane, Long field) {
    FieldToScreen fieldToScreen = new FieldToScreen();
    if (screen != null) {
      // 根据页面id，查询页面是否存在
      IssueScreen issueScreen = issueScreenDao.findById(screen).orElse(null);
      if (issueScreen != null) {
        fieldToScreen.setScreen(issueScreen);
      }
    }
    if (tabPane != null) {
      // 根据tabPane的id,查询tabPane是否存在
      IssueScreenTabPane issueScreenTabPane = issueScreenTabPaneDao.findById(tabPane).orElse(null);
      if (issueScreenTabPane != null) {
        fieldToScreen.setTabPane(issueScreenTabPane);
      }
    }
    if (field != null) {
      // 根据字段id，查询字段是否存在
      IssueField issueField = issueFieldDao.findById(field).orElse(null);
      if (issueField != null) {
        fieldToScreen.setField(IssueFieldConfigurationItem.builder().field(issueField).build());
      }
    }
    return fieldToScreen;
  }

  /**
   * @ClassName: FieldToScreenService @Description: 删除已分配到TabPane的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    List<FieldToScreen> all =
        fieldToScreenDao.findAll(
            Example.of(
                FieldToScreen.builder()
                    .screen(IssueScreen.builder().id(screen).build())
                    .field(
                        IssueFieldConfigurationItem.builder()
                            .field(IssueField.builder().id(field).build())
                            .build())
                    .tabPane(IssueScreenTabPane.builder().id(tabPane).build())
                    .build()));
    if (all.size() > 0) {
      fieldToScreenDao.deleteAll(all);
      return true;
    }
    return false;
  }

  public List<IssueFieldConfigurationItem> getTabPaneFieldItem(Long id) {
    List<FieldToScreen> all =
        fieldToScreenDao.findAll(
            Example.of(
                FieldToScreen.builder()
                    .tabPane(IssueScreenTabPane.builder().id(id).build())
                    .build()));
    List<IssueFieldConfigurationItem> stageFields = new ArrayList<>();
    if (all.size() > 0) {
      for (FieldToScreen list : all) {
        IssueFieldConfigurationItem item = itemDao.findById(list.getField().getId()).orElse(null);
        stageFields.add(item);
      }
      return stageFields;
    }
    return null;
  }
}
