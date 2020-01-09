package org.kasource.commons.reflection.collection;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class PackageMapTest {


    @Test
    public void get() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("javax", "javax");
        map.put("javax.xml", "xml");
        map.put("javax.xml.bind", "bind");
        PackageMap<String> packageMap = new PackageMap<String>(map);

        assertThat(packageMap.get(XmlAdapter.class), equalTo("bind"));

    }
}
