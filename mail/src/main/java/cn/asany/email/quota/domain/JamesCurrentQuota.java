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
package cn.asany.email.quota.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.james.core.quota.QuotaCount;
import org.apache.james.core.quota.QuotaSize;

@Entity(name = "CurrentQuota")
@Table(name = "JAMES_QUOTA_CURRENTQUOTA")
public class JamesCurrentQuota {

  @Id
  @Column(name = "CURRENTQUOTA_QUOTAROOT")
  private String quotaRoot;

  @Column(name = "CURRENTQUOTA_MESSAGECOUNT")
  private long messageCount;

  @Column(name = "CURRENTQUOTA_SIZE")
  private long size;

  public JamesCurrentQuota() {}

  public JamesCurrentQuota(String quotaRoot, long messageCount, long size) {
    this.quotaRoot = quotaRoot;
    this.messageCount = messageCount;
    this.size = size;
  }

  public QuotaCount getMessageCount() {
    return QuotaCount.count(messageCount);
  }

  public QuotaSize getSize() {
    return QuotaSize.size(size);
  }

  @Override
  public String toString() {
    return "JpaCurrentQuota{"
        + "quotaRoot='"
        + quotaRoot
        + '\''
        + ", messageCount="
        + messageCount
        + ", size="
        + size
        + '}';
  }
}
