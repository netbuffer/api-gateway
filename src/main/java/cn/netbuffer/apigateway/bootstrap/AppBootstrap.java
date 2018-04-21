package cn.netbuffer.apigateway.bootstrap;

import cn.netbuffer.apigateway.exception.ApiException;
import cn.netbuffer.apigateway.handler.IApiExceptionHandler;
import cn.netbuffer.apigateway.listener.AppLoadedListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class AppBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppBootstrap.class);

    @Resource
    private ApplicationContext applicationContext;
    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @GetMapping("api")
    public Map apis() {
        return AppLoadedListener.API_DECLARATION;
    }

    @GetMapping
    public Object api(@RequestParam(value = "method", required = false) String method,
                      @RequestParam(value = "param", required = false) String param) {
        LOGGER.info("invoke api method:{},param:{}", method, param);
        if (!check(method, param)) {
            return "invalid args";
        }
        try {
            return invoke(method, param);
        } catch (ApiException e) {
            return applicationContext.getBean(IApiExceptionHandler.class).handle(e);
        }
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Object api(@RequestBody Map data) {
        if (!check(data.get("method"), data.get("param"))) {
            return "invalid args";
        }
        return api(data.get("method").toString(), JSON.toJSONString(data.get("param")));
    }

    private boolean check(Object method, Object param) {
        if (StringUtils.isEmpty(method) || StringUtils.isEmpty(param)) {
            return false;
        }
        return true;
    }

    private Object invoke(String method, String param) {
        Method api = AppLoadedListener.API.get(method);
        if (api == null) {
            throw new ApiException(String.format("[%s] doesn't exist", method));
        }
        JSONObject jsonParam = JSONObject.parseObject(param);
        List objects = new ArrayList<>();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(api);
        Class[] paramTypes = api.getParameterTypes();
        for (int i = 0, len = paramNames.length; i < len; i++) {
            objects.add(castParamType(paramTypes[i], jsonParam.get(paramNames[i])));
        }
        try {
            return api.invoke(applicationContext.getBean(getBaseApi(method)), objects.toArray());
        } catch (IllegalAccessException e) {
            LOGGER.error("err:{}", e);
            throw new ApiException(String.format("[%s] error", method));
        } catch (InvocationTargetException e) {
            LOGGER.error("err:{}", e);
            throw new ApiException(String.format("[%s] error", method));
        }
    }

    private Object castParamType(Class valueType, Object value) {
        LOGGER.info("convert value[{}] to type[{}]", value, valueType);
        if (valueType.isPrimitive()) {
            return ConvertUtils.convert(value, valueType);
        } else {
            return JSON.parseObject(value.toString(), valueType);
        }
    }

    private String getBaseApi(String name) {
        int index = name.lastIndexOf(".");
        return name.substring(0, index);
    }
}
