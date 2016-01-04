package com.maxleap.code.spring;

import com.maxleap.code.MLClassManager;
import com.maxleap.code.MLException;
import com.maxleap.code.MLHandler;
import com.maxleap.code.impl.Loader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * User：David Young
 * Date：15/12/30
 */
public class DefaultAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {

  @Override
  public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition,Class beanType, String beanName) {
    if (MLClassManager.class.isAssignableFrom(beanType)) {//执行MLClassManager类型的自动装载，根据泛型类型来得到对应的beanName
      Class argValue = (Class)beanDefinition.getConstructorArgumentValues().getGenericArgumentValues().get(0).getValue();
      super.postProcessMergedBeanDefinition(beanDefinition,beanType,argValue.getName());
    } else {
      super.postProcessMergedBeanDefinition(beanDefinition, beanType, beanName);
    }
  }

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
    //为添加了@Function注解的类注册function/job
    defineFunctionWhenTargetOnType(bean);

    //为添加了@Function注解的方法注册function/job
    Method[] methods = bean.getClass().getMethods();
    for (Method method : methods) {
      defineFunctionWhenTargetOnMethod(bean, method);
    }
    return super.postProcessAfterInstantiation(bean, beanName);
  }

  //为添加了@Function注解的类注册function/job
  private void defineFunctionWhenTargetOnType(Object bean){
    Annotation[] annotations = bean.getClass().getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof Function) {
        if (!(bean instanceof MLHandler)) throw new MLException("your function Class["+bean.getClass().getName()+"] must be implement MLHandler.");
        Function function = (Function)annotation;
        String functionName = function.value();
        Loader.defineFunction(functionName, (MLHandler) bean);
      } else if (annotation instanceof Job) {
        if (!(bean instanceof MLHandler)) throw new MLException("your job Class["+bean.getClass().getName()+"] must be implement MLHandler.");
        Job job = (Job)annotation;
        String jobName = job.value();
        Loader.defineJob(jobName, (MLHandler) bean);
      }
    }
  }

  //为添加了@Function注解的方法注册function/job
  private void defineFunctionWhenTargetOnMethod(Object bean,Method method){
    Annotation[] methodAnnotations = method.getAnnotations();
    for (Annotation annotation : methodAnnotations) {
      if (annotation instanceof Function) {
        if(!(method.getReturnType() == MLHandler.class)) throw new MLException("your function method["+method.toGenericString()+"] must be return an MLHandler Type");
        Function function = (Function)annotation;
        String functionName = function.value();
        Object result;
        try {
          result = method.invoke(bean,null);
        } catch (Exception e) {
          throw new MLException(e);
        }
        Loader.defineFunction(functionName,(MLHandler)result);

      } else if (annotation instanceof Job){
        if(!(method.getReturnType() == MLHandler.class)) throw new MLException("your job method["+method.toGenericString()+"] must be return an MLHandler Type");
        Job job = (Job)annotation;
        String jobName = job.value();
        Object result;
        try {
          result = method.invoke(bean,null);
        } catch (Exception e) {
          throw new MLException(e);
        }
        Loader.defineJob(jobName, (MLHandler) result);
      }
    }
  }
}
