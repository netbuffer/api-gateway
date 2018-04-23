package cn.netbuffer.apigateway;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class TestUriBuild {

    @Test
    public void testUriBuild(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("example.com").path("/hotels/{hotel}/bookings/{booking}").build()
                .expand("42", "21")
                .encode();
        System.out.println(uriComponents);
    }
}
