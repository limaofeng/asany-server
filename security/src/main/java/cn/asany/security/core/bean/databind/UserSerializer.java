package cn.asany.security.core.bean.databind;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cn.asany.security.core.bean.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (user == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(user.getId());
        }
    }
}
