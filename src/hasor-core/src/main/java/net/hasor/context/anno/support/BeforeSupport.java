/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.context.anno.support;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.hasor.context.ApiBinder;
import net.hasor.context.AppContext;
import net.hasor.context.anno.Before;
import net.hasor.context.matcher.AopMatchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.google.inject.matcher.Matcher;
/**
 * ֧��Beanע�⹦�ܡ�
 * @version : 2013-4-8
 * @author ������ (zyc@hasor.net)
 */
class BeforeSupport {
    private GetContext getContext = null;
    //
    public BeforeSupport(ApiBinder apiBinder, GetContext getContext) {
        this.getContext = getContext;
        Matcher<Object> matcher = AopMatchers.annotatedWith(Before.class);//
        apiBinder.getGuiceBinder().bindInterceptor(matcher, matcher, new BeforeInterceptor());
    }
    /*-------------------------------------------------------------------------------------*/
    /*������*/
    private class BeforeInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            List<Class<? extends MethodInterceptor>> list = new ArrayList<Class<? extends MethodInterceptor>>();
            Method targetMethod = invocation.getMethod();
            //
            Before beforeAnno = targetMethod.getDeclaringClass().getAnnotation(Before.class);
            if (beforeAnno != null) {
                for (Class<? extends MethodInterceptor> interType : beforeAnno.value())
                    if (interType != null)
                        list.add(interType);
            }
            beforeAnno = targetMethod.getAnnotation(Before.class);
            if (beforeAnno != null) {
                for (Class<? extends MethodInterceptor> interType : beforeAnno.value())
                    if (interType != null)
                        list.add(interType);
            }
            //2.��ȡ������
            AppContext appContext = getContext.getAppContext();
            return new BeforeChainInvocation(appContext, list, invocation).invoke(invocation);
        }
    }
}