/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.message.data.graphql.subscription;

import cn.asany.message.core.Notification;
import net.asany.jfantasy.graphql.publishers.FluxPublisher;
import org.springframework.stereotype.Component;

/**
 * 通知发布者
 *
 * @author limaofeng
 */
@Component
public class NotificationPublisher extends FluxPublisher<Notification> {}
