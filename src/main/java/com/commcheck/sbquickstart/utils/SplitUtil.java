package com.commcheck.sbquickstart.utils;

import java.util.List;
import java.util.stream.Collectors;

public class SplitUtil {
    public static String pushBack(String list, String element){
        return list + ";" + element;
    }

    public static String pushBack(String list, Integer element){
        return list + ";" + element;
    }

    public static List<String> splitBySemicolon(String list){
        return List.of(list.split(";"));
    }

    public static List<Integer> splitBySemicolonInt(String list){
        return List.of(list.split(";")).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

}
