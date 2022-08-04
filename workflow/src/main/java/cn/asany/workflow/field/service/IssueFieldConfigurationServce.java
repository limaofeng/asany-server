package cn.asany.workflow.field.service;

import cn.asany.pm.field.bean.*;
import cn.asany.pm.field.bean.enums.FieldCategory;
import cn.asany.pm.field.dao.IssueFieldConfigurationDao;
import cn.asany.pm.field.dao.IssueFieldConfigurationSchemeDao;
import cn.asany.pm.field.dao.IssueFieldDao;
import cn.asany.pm.field.dao.IssueFieldValueDao;
import cn.asany.pm.field.graphql.model.IssueFieldValueInput;
import cn.asany.pm.issue.bean.Issue;
import cn.asany.pm.screen.bean.FieldToScreen;
import cn.asany.pm.screen.dao.FieldToScreenDao;
import cn.asany.pm.workflow.bean.WorkflowSchedule;
import cn.asany.pm.workflow.dao.IssueWorkflowStepTransitionDao;
import cn.asany.pm.workflow.dao.WorkflowScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssueFieldConfigurationServce {

    @Autowired
    private IssueFieldConfigurationDao fieldConfigurationDao;
    @Autowired
    private IssueFieldConfigurationSchemeDao fieldConfigurationSchemeDao;
    @Autowired
    private IssueFieldDao issueFieldDao;
    @Autowired
    private IssueFieldValueDao valueDao;
    @Autowired
    private IssueWorkflowStepTransitionDao transitionDao;
    @Autowired
    private WorkflowScheduleDao scheduleDao;
    @Autowired
    private FieldToScreenDao fieldToScreenDao;



    public List<IssueFieldValue> findByIssue(Issue issue) {
        return valueDao.findByIssue(issue);
    }



    public List<IssueFieldConfiguration> getFieldConfigurations() {
        return fieldConfigurationDao.findAll();
    }

    public List<IssueFieldConfigurationScheme> getFieldConfigurationSchemes() {
        return fieldConfigurationSchemeDao.findAll();
    }

    public IssueFieldConfiguration getFieldConfiguration(Long id) {
        return fieldConfigurationDao.findById(id).orElse(null);
    }

    public IssueFieldConfigurationScheme getFieldConfigurationScheme(Long id) {
        return fieldConfigurationSchemeDao.findById(id).orElse(null);
    }

    /**
     * @ClassName: IssueFieldConfigurationServce
     * @Description: 保存自定义字段值
     * @param  issue 对应的问题
     *
     * @author limaofeng@msn.com
     * @date 2022/7/28 9:12
     *
     */
    public Boolean issueProject(Issue issue,Long action, List<IssueFieldValueInput> values){
        if (action == null) {
            return this.saveFieldValue(issue,values);
        }else {
            Boolean value = this.saveFieldValue(issue,values);
            if (value == true) {
                //向记录表中添加数据
                WorkflowSchedule workflowSchedule = new WorkflowSchedule();
                workflowSchedule.setProject(issue.getProject());
                workflowSchedule.setIssue(issue);
                workflowSchedule.setAssignee(issue.getAssignee());
                if(transitionDao.findById(action).orElse(null) != null) {
                    workflowSchedule.setTransition(transitionDao.findById(action).orElse(null));
                }
                scheduleDao.save(workflowSchedule);
                return true;
            }
            return false;
        }
    }

    /**
     * @ClassName: IssueFieldConfigurationServce
     * @Description: TODO
     * @author limaofeng@msn.com
     * @date 2022/7/28 9:12
     *
     */
    public Boolean saveFieldValue(Issue issue, List<IssueFieldValueInput> values){
        //查询全部自定义字段
        List<IssueField> issueFields = issueFieldDao.findAll(Example.of(IssueField.builder().category(FieldCategory.custom).build()));
        if (values != null && values.size() > 0) {
            //过滤提交的自定义字段
            for (IssueField field : issueFields) {
                IssueFieldValue fieldValue = new IssueFieldValue();
                Optional<IssueFieldValueInput> any = values.stream().filter(item -> item.getName().equals(field.getName())).findAny();
                if (any.isPresent()) {
                    //List<IssueFieldValue> all = valueDao.findAll(Example.of(IssueFieldValue.builder().issueField(IssueField.builder().name(any.get().getName()).build()).build()));
                    fieldValue.setIssue(issue);
                    fieldValue.setValue(any.get().getValue());
                    /*if (all.size() > 0) {
                        fieldValue.setIssueField(all.get(0).getIssueField());
                    }*/
                    fieldValue.setIssueField(field);
                    valueDao.save(fieldValue);
                }

            }
            return true;
        }
        return false;
    }

    public Integer order(Long id) {
        List<FieldToScreen> all = fieldToScreenDao.findAll(Example.of(FieldToScreen.builder().field(IssueFieldConfigurationItem.builder().id(id).build()).build()));
        if (all.size() <= 0){
            return  null;
        }
        return Integer.valueOf(String.valueOf(all.get(0).getOrder()));
    }
}
