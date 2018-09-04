package com.qingqingmr.qqmr.domain;

/**
 * @author crn
 * @datetime 2018-07-19 11:12:12
 */
public class TestClass {
    public TestClass() {
    }

    public static TestClass getInstance() {
        return TestEnum.instance.getEnum();
    }

    public enum TestEnum {
        instance;

        TestEnum() {

        }

        public TestClass getEnum() {
            return new TestClass();
        }
    }

    public static void main(String[] args) {
        System.out.println("当前类TestClass的main方法输出："+new TestClass());
    }
}
