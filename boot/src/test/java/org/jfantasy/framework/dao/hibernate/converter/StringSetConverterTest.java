package org.jfantasy.framework.dao.hibernate.converter;

import org.junit.jupiter.api.Test;

import java.util.Set;

class StringSetConverterTest {

    private StringSetConverter converter = new StringSetConverter();

    @Test
    void convertToEntityAttribute() {
        Set<String> names = converter.convertToEntityAttribute("[\"limaofeng\", \"huangli\"]");
        System.out.println(names);
    }
}