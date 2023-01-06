package cn.asany.message.data.service;

import cn.asany.message.data.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  @Autowired private MessageDao messageDao;
}
