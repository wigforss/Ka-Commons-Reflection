package org.kasource.commons.reflection.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

public class ClassMapTest {

    @Test
    public void getEmptyMap() {
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertThat(classMap.get(Integer.class), is(nullValue()));
    }

    @Test
    public void getInteger() {
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        map.put(Number.class, "Number");
        map.put(Object.class, "Object");
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertThat(classMap.get(Integer.class), equalTo("Number"));
    }

    @Test
    public void getAttributeList() {
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        map.put(List.class, "List");
        map.put(Object.class, "Object");
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertThat(classMap.get(AttributeList.class), equalTo("List"));
    }
}
