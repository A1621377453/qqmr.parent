package com.qingqingmr.qqmr.web;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author crn
 * @datetime 2018-07-23 15:54:01
 */
public class SetTest {
    @Test
    public void test1(){
        Set<String> set = new HashSet<>();
        set.add("s1");
        set.add("s2");
        set.add("s3");
        set.add("s4");
        set.add("s5");
        set.forEach((s)-> System.out.println(s));
    }
    @Test
    public void test2(){
        Set<String> set = new LinkedHashSet<>();
        set.add("s4");
        set.add("s3");
        set.add("s2");
        set.add("s5");
        set.add("s1");
        set.forEach((s)-> System.out.println(s));
    }
    @Test
    public void test3(){
        Set<String> set = new TreeSet<>();
        set.add("s5");
        set.add("s2");
        set.add("s1");
        set.add("s4");
        set.add("s3");
        set.forEach((s)-> System.out.println(s));
    }
}
