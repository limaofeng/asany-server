/**
 * ************************************************************** Licensed to the Apache Software
 * Foundation (ASF) under one * or more contributor license agreements. See the NOTICE file *
 * distributed with this work for additional information * regarding copyright ownership. The ASF
 * licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not
 * use this file except in compliance * with the License. You may obtain a copy of the License at *
 * * http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to
 * in writing, * software distributed under the License is distributed on an * "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied. See the License for
 * the * specific language governing permissions and limitations * under the License. *
 * **************************************************************
 */
package cn.asany.email.user.component;

import cn.asany.email.mailbox.component.JPATransactionalMapper;
import cn.asany.email.user.domain.JamesSubscription;
import cn.asany.email.user.service.SubscriptionService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import org.apache.james.mailbox.exception.SubscriptionException;
import org.apache.james.mailbox.store.user.SubscriptionMapper;
import org.apache.james.mailbox.store.user.model.Subscription;

public class JPASubscriptionMapper extends JPATransactionalMapper implements SubscriptionMapper {

  private SubscriptionService subscriptionService;

  public JPASubscriptionMapper(EntityManagerFactory entityManagerFactory) {
    super(entityManagerFactory);
  }

  @Override
  public Subscription findMailboxSubscriptionForUser(String user, String mailbox) {
    return this.subscriptionService.findFindMailboxSubscriptionForUser(user, mailbox).orElse(null);
  }

  @Override
  public void save(Subscription subscription) throws SubscriptionException {
    try {
      getEntityManager().persist(subscription);
    } catch (PersistenceException e) {
      throw new SubscriptionException(e);
    }
  }

  @Override
  public List<Subscription> findSubscriptionsForUser(String user) throws SubscriptionException {
    return new ArrayList<>(this.subscriptionService.findSubscriptionsForUser(user));
  }

  @Override
  public void delete(Subscription subscription) throws SubscriptionException {
    this.subscriptionService.delete(((JamesSubscription) subscription).getId());
  }
}
