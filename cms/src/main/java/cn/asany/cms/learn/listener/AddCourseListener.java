package cn.asany.cms.learn.listener;

import cn.asany.cms.learn.event.CourseCreatedEvent;
import cn.asany.cms.learn.service.CourseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AddCourseListener implements ApplicationListener<CourseCreatedEvent> {

  //    @GrpcClient("GLOBAL")
  //    private MessageServiceGrpc.MessageServiceBlockingStub messageService;
  @Autowired protected Environment environment;
  @Autowired private CourseService courseService;

  @Override
  public void onApplicationEvent(@NotNull CourseCreatedEvent event) {
    //        String messageServerUrl = environment.getProperty("COURSE_MESSAGE_SERVER_URL");
    //        long agentId =
    // Long.parseLong(Objects.requireNonNull(environment.getProperty("AGENTID")));
    //        //获取课程的id
    //        String courseId = event.getCourseId();
    //        //根据课程id，获取课程学习范围
    //        Course course = courseService.findById(Long.valueOf(courseId));
    //        //接收手机号集合，根据手机号发送通知
    //        List<String> userList = new ArrayList<>();
    //        if (CollectionUtils.isNotEmpty(course.getLearnerScope())) {
    //            //course.getLearnerScope() 返回的不是LearnerScope的list，而是Persistentbag,JPA懒加载导致。
    //            for(Object obj : course.getLearnerScope()){
    //                String scope = (String) obj;
    //                Set <String> mobiles =
    // securityGrpcInvoke.getSelection(SecurityScope.newInstance(scope).getType(),
    // SecurityScope.newInstance(scope).getValue()).stream()
    //                        .filter(item -> StringUtil.isNotBlank(item.getMobile()))
    //                        .map(Employee::getMobile).collect(Collectors.toSet());
    //                    userList.addAll(mobiles);
    //            }
    //        } else {
    //            //根据组织id，查询全部该组织的全部人员 TODO
    //            // securityGrpcInvoke.getSelection(SecurityType.organization,
    // resource).stream().map(item -> userList.add(String.valueOf(item.getMobile())));
    //        }
    //        if (userList.isEmpty()) {
    //            return;
    //        }
    //        //给必修人员发送钉钉通知
    //        Message.MassageUsersResponse message =
    // messageService.sendMessageUsers(Message.MassageUsersRequest.newBuilder()
    //                .addAllEmployeeIds(userList)
    //                .setAgentId(agentId)
    //                .setMessage(Message.Messages.newBuilder()
    //                        .setMsgType(Message.MsgType.link)
    //                        .setLinkMessage(Message.LinkMessage.newBuilder()
    //                                .setMessageUrl(messageServerUrl +
    // course.getId()+"?learnerType=compulsory")
    //                                .setTitle("您有一门课程需要学习")
    //                                .setText(course.getName())
    //                                .build())
    //                        .build())
    //                .build());
  }
}
