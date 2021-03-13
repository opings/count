package com.hupu.deep.comment.web.config;

import com.google.common.collect.Maps;
import com.hupu.foundation.TraceIdAware;
import com.hupu.foundation.result.SimpleResult;
import com.hupu.foundation.util.NetUtil;
import com.hupu.foundation.util.StringToDateConverter;
import com.hupuarena.msvfoundation.config.MsvConfig;
import com.hupuarena.msvfoundation.interceptor.MsvHandlerInterceptor;
import com.hupuarena.msvfoundation.log.holder.TraceLogRequestContextHolder;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
@Configuration
@EnableSwagger2
/**
 * @author zhuwenkang
 * @Time 2020年03月10日 22:48:00
 */
public class AppConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware, ApplicationRunner {


    @Setter
    private ApplicationContext applicationContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new MsvHandlerInterceptor(new MsvConfig()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }

    @Override
    public void run(ApplicationArguments args)  {

        RequestMappingHandlerAdapter requestMappingHandlerAdapter =
                applicationContext.getBean(RequestMappingHandlerAdapter.class);

        List<HandlerMethodReturnValueHandler> unmodifiableList = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new ResponseBodyWrapHandler(returnValueHandler));
            } else {
                list.add(returnValueHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }


    private static class ResponseBodyWrapHandler implements HandlerMethodReturnValueHandler {

        private HandlerMethodReturnValueHandler delegate;

        public ResponseBodyWrapHandler(HandlerMethodReturnValueHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            return delegate.supportsReturnType(returnType);
        }

        @Override
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            delegate.handleReturnValue(wrapReturnValue(returnValue, returnType), returnType, mavContainer, webRequest);
        }


        /**
         * 多返回值装饰
         *
         * @param returnValue
         * @return
         */
        private Object wrapReturnValue(Object returnValue, MethodParameter returnType) {
            if (returnValue instanceof TraceIdAware) {
                ((TraceIdAware) returnValue).setTraceId(TraceLogRequestContextHolder.getTraceNode().getTid() + ":" + NetUtil.getHostName());
            }
            if (returnType.getMethod().getName().equalsIgnoreCase("singleMatchStats")) {
                if (returnValue instanceof SimpleResult) {
                    SimpleResult simpleResult = (SimpleResult) returnValue;
                    if (simpleResult.getResult() == null) {
                        simpleResult.setResult(Maps.newHashMap());
                    }
                }
            }
            return returnValue;
        }
    }

}

