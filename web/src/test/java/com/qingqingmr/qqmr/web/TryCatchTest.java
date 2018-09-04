/*
package com.qingqingmr.qqmr.web;

*/
/**
 * @author crn
 * @datetime 2018-07-23 09:48:23
 *//*

public class TryCatchTest {
    public static void main(String[] args) {

        System.out.println(test1());

        System.out.println(test2());

        */
/*System.out.println(test3());

        System.out.println(test4());*//*


    }


    private static int test1() {
        int i = 1;
        try {
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            i = 0;
        }
        return i;
    }


    private static int test2() {
        int i = 1;
        try {
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            i = 0;
            return i;
        }
    }


    private static User test3() {
        User user = new User("u1");
        try {
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            user = new User("u2");
        }
        return null;
    }


    private static User test4() {
        User user = new User("u1");
        try {
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            user.setName("u2");
        }
        return null;
    }
}
*/
