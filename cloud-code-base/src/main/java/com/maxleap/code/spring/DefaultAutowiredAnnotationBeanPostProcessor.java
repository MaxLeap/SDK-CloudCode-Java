package com.maxleap.code.spring;

import com.maxleap.code.MLClassManager;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

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
}
