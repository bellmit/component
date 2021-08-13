package com.lyloou.component.security.loginvalidator.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.lyloou.component.security.loginvalidator.UserContextHolder;
import com.lyloou.component.security.loginvalidator.UserManager;
import com.lyloou.component.security.loginvalidator.annotation.IgnoreValidateLogin;
import com.lyloou.component.security.loginvalidator.annotation.ValidateLogin;
import com.lyloou.component.security.loginvalidator.exception.ValidateLoginException;
import com.lyloou.component.security.loginvalidator.properties.TokenProperties;
import com.lyloou.component.security.loginvalidator.service.TokenService;
import com.lyloou.component.security.loginvalidator.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author lilou
 */
@Aspect
@Component
public class ValidateLoginAspect {

    @Autowired
    TokenProperties tokenProperties;
    @Autowired
    TokenService validateLoginService;

    /**
     * 拦截ValidateLogin 和 IgnoreValidateLogin 注解
     */
    @Pointcut("@annotation(com.lyloou.component.security.loginvalidator.annotation.ValidateLogin)" +
            "||@within(com.lyloou.component.security.loginvalidator.annotation.ValidateLogin)" +
            "||@annotation(com.lyloou.component.security.loginvalidator.annotation.IgnoreValidateLogin)" +
            "||@within(com.lyloou.component.security.loginvalidator.annotation.IgnoreValidateLogin)"
    )
    private void pointCutMethod() {
    }


    // 原文链接：https://blog.csdn.net/java_faep/article/details/104005399
    @Around("pointCutMethod()")
    public Object preHandle(ProceedingJoinPoint pjp) throws Throwable {

        if (hasAnnotation(pjp, IgnoreValidateLogin.class)) {
            return pjp.proceed();
        }

        if (!hasAnnotation(pjp, ValidateLogin.class)) {
            return pjp.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final TokenProperties tokenProperties = SpringUtil.getBean(TokenProperties.class);

        //从header中获取token
        String authorizationToken = request.getHeader(tokenProperties.getAuthorizationHeaderName());
        if (!checkTokenPrefix(authorizationToken)) {
            throw new ValidateLoginException("无效的Header头，没提供");
        }

        String token = getRawToken(authorizationToken);

        // 解析 jwt token
        Jws<Claims> jws = JwtUtils.parserJwtToken(token, tokenProperties.getSecretKey());

        // token 是否过期（通过缓存来判断，不用通过这个来判断了，可以更灵活，如踢人、登出等操作）
        if (JwtUtils.isExpired(jws)) {
            throw new ValidateLoginException("token已经过期");
        }

        // 缓存中存的是键值对：token -> userInfoStr
        final String userInfoJsonStr = validateLoginService.getUserInfo(authorizationToken);
        if (userInfoJsonStr == null) {
            throw new ValidateLoginException("token已经过期");
        }

        final String userId = jws.getBody().get(UserManager.X_USER_ID, String.class);
        final String userName = jws.getBody().get(UserManager.X_USER_NAME, String.class);
        final String userIp = request.getRemoteHost();
        final HashMap<String, String> map = new HashMap<>();
        map.put(UserManager.X_USER_ID, userId);
        map.put(UserManager.X_USER_NAME, userName);
        map.put(UserManager.X_USER_IP, userIp);
        map.put(UserManager.X_USER_INFO, userInfoJsonStr);

        UserContextHolder.getInstance().setContext(map);
        final Object proceed;
        try {
            proceed = pjp.proceed();
        } finally {
            UserContextHolder.getInstance().clear();
        }

        return proceed;
    }

    private boolean hasAnnotation(ProceedingJoinPoint pjp, Class<? extends Annotation> clazz) throws NoSuchMethodException {


        MethodSignature ms = (MethodSignature) pjp.getSignature();
        final Class<?> targetClass = pjp.getTarget().getClass();

        // 方法上得注解
        Method targetMethod = targetClass.getDeclaredMethod(ms.getName(), ms.getMethod().getParameterTypes());
        Annotation methodAnnotation = targetMethod.getAnnotation(clazz);
        if (methodAnnotation != null) {
            return true;
        }

        // 类上得注解
        final Annotation clazzAnnotation = targetClass.getDeclaredAnnotation(clazz);
        if (clazzAnnotation != null) {
            return true;
        }


        // 如果类上面没有注解，则获取接口上或此方法的注解
        Class<?>[] inters = targetClass.getInterfaces();
        for (Class<?> inter : inters) {
            if (inter.getDeclaredAnnotation(clazz) != null) {
                return true;
            }

            Method targetInterMethod = inter.getDeclaredMethod(ms.getName(), ms.getMethod().getParameterTypes());
            methodAnnotation = targetInterMethod.getAnnotation(clazz);
            if (methodAnnotation != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * 截取得到真实的 token 串（没有前缀的token）
     *
     * @param authorizationToken authorization token
     * @return 结果
     */
    private String getRawToken(String authorizationToken) {
        return StrUtil.subAfter(authorizationToken, tokenProperties.getAuthorizationHeaderPrefix(), true).trim();
    }

    /**
     * 判断是否是系统中登录后签发的token
     *
     * @param authorizationToken authorization token
     * @return 结果
     */
    private boolean checkTokenPrefix(String authorizationToken) {
        if (StrUtil.isBlank(authorizationToken)) {
            return false;
        }
        return authorizationToken.startsWith(tokenProperties.getAuthorizationHeaderPrefix());
    }


}