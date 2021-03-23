package com.cm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * list集合转为string字符串工具类
 */
public class ListToStringTest {

    /**
     * 方法一 使用最多
     * @param list
     * @param separator
     * @return
     */
    public static String listToString1(List list, char separator) {
        return StringUtils.join(list.toArray(), separator);
    }

    /**
     * 方法二到方法四基本类似 都是循环拼接
     * @param list
     * @param separator
     * @return
     */
    public static String listToString2(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    // 方法三
    public static String listToString3(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    // 方法四
    public static String listToString4(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        System.out.println(ListToStringTest.listToString1(list, '+'));
        System.out.println(ListToStringTest.listToString2(list, '-'));
        System.out.println(ListToStringTest.listToString3(list, '*'));
        System.out.println(ListToStringTest.listToString4(list, '/'));
    }

}