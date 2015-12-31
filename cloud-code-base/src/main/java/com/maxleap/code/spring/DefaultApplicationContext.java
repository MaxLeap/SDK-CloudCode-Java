package com.maxleap.code.spring;

import com.maxleap.code.MLClassManager;
import com.maxleap.code.MLClassManagerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 重写ApplicationContext相关方法，以便在加载用户的xml配置文件前可以先注入需要的资源并让MLClassManager可以自动装载
 * User：David Young
 * Date：15/12/30
 */
public class DefaultApplicationContext extends ClassPathXmlApplicationContext {

  public DefaultApplicationContext() {
  }

  public DefaultApplicationContext(ApplicationContext parent) {
    super(parent);
  }

  public DefaultApplicationContext(String configLocation) throws BeansException {
    super(configLocation);
  }

  public DefaultApplicationContext(String... configLocations) throws BeansException {
    super(configLocations);
  }

  public DefaultApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
    super(configLocations, parent);
  }

  public DefaultApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
    super(configLocations, refresh);
  }

  public DefaultApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) throws BeansException {
    super(configLocations, refresh, parent);
  }

  public DefaultApplicationContext(String path, Class<?> clazz) throws BeansException {
    super(path, clazz);
  }

  public DefaultApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
    super(paths, clazz);
  }

  public DefaultApplicationContext(String[] paths, Class<?> clazz, ApplicationContext parent) throws BeansException {
    super(paths, clazz, parent);
  }

  @Override
  protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    //替换默认的自动装载，使之能自动装载MLClassManager
    BeanDefinition autoWiredDefine = beanFactory.getBeanDefinition(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME);
    autoWiredDefine.setBeanClassName(DefaultAutowiredAnnotationBeanPostProcessor.class.getName());
//    DefaultListableBeanFactory reg = (DefaultListableBeanFactory) beanFactory;
//    reg.registerBeanDefinition(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, autoWiredDefine);
    //注入所有的MLClassManager
    for (Map.Entry<Class<?>,MLClassManager> entry:MLClassManagerFactory.getManagerMap().entrySet()) {
      registerBeanDefinition(beanFactory,entry.getKey());
    }
    super.prepareBeanFactory(beanFactory);
  }

  //注入MLClassManager(工厂方式注入)
  private static void registerBeanDefinition(ConfigurableListableBeanFactory beanFactory,Class clazz){
    BeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClassName(MLClassManagerFactory.class.getName());
    beanDefinition.setFactoryMethodName("getManager");
    beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
    DefaultListableBeanFactory reg = (DefaultListableBeanFactory) beanFactory;
    reg.registerBeanDefinition(clazz.getName(), beanDefinition);
  }
}
