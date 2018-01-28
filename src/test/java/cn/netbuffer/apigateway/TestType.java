package cn.netbuffer.apigateway;

import org.junit.Test;

public class TestType {

    @Test
    public void testType(){
        Integer i=1;
        Long l=1L;
        String s="";
        System.out.println(i.getClass().getName());
        System.out.println(l.getClass().getName());
        System.out.println(s.getClass().getName());
    }
}
