package cn.netbuffer.apigateway.listener;

import cn.netbuffer.apigateway.api.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AppLoadedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLoadedListener.class);
    public static Map<String, Method> API = new HashMap<>();
    public static Map<String, Map<String, Object>> API_DECLARATION = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextStartedEvent) {
        ApplicationContext applicationContext = contextStartedEvent.getApplicationContext();
        if (applicationContext.getParent() == null) {
            LOGGER.info("load api:{}", initApi(applicationContext));
        }
    }

    public static Object getTarget(Object beanInstance) {
        if (!AopUtils.isAopProxy(beanInstance)) {
            return beanInstance;
        } else if (AopUtils.isCglibProxy(beanInstance)) {
            try {
                Field h = beanInstance.getClass().getDeclaredField("CGLIB$CALLBACK_0");
                h.setAccessible(true);
                Object dynamicAdvisedInterceptor = h.get(beanInstance);
                Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
                advised.setAccessible(true);
                Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
                return target;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Integer initApi(ApplicationContext applicationContext) {
        Map<String, Object> apiBeans = applicationContext.getBeansWithAnnotation(Api.class);
        for (Map.Entry api : apiBeans.entrySet()) {
            Object value = api.getValue();
            log.debug("{} isAopProxy:{}", value, AopUtils.isAopProxy(value));
            Class apiClass = getTarget(value).getClass();
            Api a = (Api) apiClass.getAnnotation(Api.class);
            if (a == null) {
                continue;
            }
            Method[] methods = apiClass.getDeclaredMethods();
            for (Method m : methods) {
                Api am = m.getAnnotation(Api.class);
                String name = null;
                if (am == null || StringUtils.isEmpty(am.name())) {
                    name = m.getName();
                } else {
                    name = am.name();
                }
                String apiName = a.name() + "." + name;
                API.put(apiName, m);
                Map method = new HashMap();
                method.put("method", m.getName());
                method.put("params", m.getParameterTypes());
                API_DECLARATION.put(apiName, method);
            }
        }
        return API.size();
    }
}
