package cn.asany.email.data.rest;

import org.apache.james.queue.api.MailQueueFactory;
import org.apache.james.queue.api.ManageableMailQueue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAController {

  private final MailQueueFactory<ManageableMailQueue> mailQueueFactory;

  public TestAController(MailQueueFactory<ManageableMailQueue> mailQueueFactory) {
    this.mailQueueFactory = mailQueueFactory;
  }

  @GetMapping("/james/see")
  public String xx() {
    mailQueueFactory.listCreatedMailQueues();
    return "xxx";
  }
}
