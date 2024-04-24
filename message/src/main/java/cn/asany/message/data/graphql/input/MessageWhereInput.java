package cn.asany.message.data.graphql.input;

import cn.asany.message.api.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * Message Where Input
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageWhereInput extends WhereInput<MessageWhereInput, Message> {}
