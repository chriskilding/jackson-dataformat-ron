package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RONMapperTest {

    @Test
    public void testWriteStruct() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Person person = new Person();
        person.setGivenName("Joe");
        person.setFamilyName("Bloggs");
        String ron = mapper.writeValueAsString(person);
        assertEquals("(familyName:\"Joe\",givenName:\"Bloggs\")", ron);
    }

    @Test
    public void testWriteEnum() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Foo foo = Foo.Bar;
        String ron = mapper.writeValueAsString(foo);
        assertEquals("Bar", ron);
    }

    @Test
    public void testWriteArray() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        int[] array = {1, 2};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[1,2]", ron);
    }

    @Test
    public void testWriteMap() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", 2);
        String ron = mapper.writeValueAsString(map);
        assertEquals("{\"foo\":1,\"bar\":2}", ron);
    }

    public static class Person {

        private String givenName;
        private String familyName;


        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }
    }

    public enum Foo {
        Bar, Baz
    }
}
