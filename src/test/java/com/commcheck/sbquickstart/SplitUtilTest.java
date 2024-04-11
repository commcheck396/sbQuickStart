package com.commcheck.sbquickstart;

import com.commcheck.sbquickstart.utils.SplitUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SplitUtilTest {
    @Test
    public void testSplit() {
        List<String> result = SplitUtil.splitBySemicolon("");
//        System.out.println("start"+result+"end");
//        assert result.size() == 0;
//        assert result.get(0).equals("a");

    }

    @Test
    public void testSplitInt(){
        List<Integer> result = SplitUtil.splitBySemicolonInt("1");
        assert result.get(0) == 1;
    }

    @Test
    public void testPushBackInt(){
        String before = "1;2;3";
        int add = 4;
        String result = SplitUtil.pushBack(before, add);
        assert result.equals("1;2;3;4");
    }

    @Test
    public void testPushBackString(){
        String before = "a;b;c";
        String add = "d";
        String result = SplitUtil.pushBack(before, add);
        assert result.equals("a;b;c;d");
    }
}
