# Spring

## 说到FactoryBean，BeanFactory和FactoryBean有什么区别？

`BeanFactory`定义了IOC容器的最基本形式并提供了IOC容器应遵守的的最基本的接口，也就是Spring IOC所遵守的最底层和最基本的编程规范，它只是个接口，并不是IOC容器的具体实现。

它的职责包括：实例化、定位、配置应用程序中的对象及建立这些对象间的依赖。

`FactoryBean`，一般情况下，Spring通过反射机制利用bean的class属性实例化Bean，然而在某些情况下，实例化Bean过程比较复杂，如果按照传统的方式，则需要在bean的定义中提供大量的配置信息，而配置这种方式的灵活性是受限的，这时采用编码的方式可能会是一个比较合适的方案，Spring为此提供了FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑。

## IOC创建流程

![1041678-c7f0029d95d2cdff](/Users/wenzhong/typora-pic/1041678-c7f0029d95d2cdff.png)

## Spring的AOP机制的理解

1. AOP面向切面编程，所谓切面就是对一类重复业务的抽象，例如事务。本来事务的操作耦合在各个业务层代码中，不好统一管理，这也叫做编程式事务，不过用的不多了。我们就可以通过AOP将事务定义成一个切面，然后定义对应的通知与切点，这样事务的管理变得更加清晰，代码也变得更加优雅。 
2. 关于AOP的实现，基于动态代理，动态代理的实现有两种，分别对应静态代理中的两种实现——组合/继承。一种通过组合的方式获取目标函数，一个通过继承目标类通过父类调用函数。

## Spring AOP，AspectJ，CGLIB 有点晕

AOP 代理则可分为`静态代理`和`动态代理`两大类。

`静态代理`是指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为`编译时增强`；

`动态代理`则在运行时借助于 JDK 动态代理、CGLIB 等在内存中“临时”生成 AOP 动态代理类，因此也被称为`运行时增强`。

![image-20200419163856535](/Users/wenzhong/typora-pic/image-20200419163856535.png)
链接：https://www.jianshu.com/p/fe8d1e8bd63e

## Spring Bean初始化过程

![1200](/Users/wenzhong/typora-pic/1200.png)

##  Spring Bean 的生命周期过程如下（方法级别）：

1. Spring 容器根据实例化策略对 Bean 进行实例化。
2. 实例化完成后，如果该 bean 设置了一些属性的话，则利用 set 方法设置一些属性。
3. 如果该 Bean 实现了 BeanNameAware 接口，则调用 `setBeanName()` 方法。
4. 如果该 bean 实现了 BeanClassLoaderAware 接口，则调用 `setBeanClassLoader()` 方法。
5. 如果该 bean 实现了 BeanFactoryAware接口，则调用 `setBeanFactory()` 方法。
6. 如果该容器注册了 BeanPostProcessor，则会调用`postProcessBeforeInitialization()` 方法完成 bean 前置处理
7. 如果该 bean 实现了 InitializingBean 接口，则调用`afterPropertiesSet()` 方法。
8. 如果该 bean 配置了 init-method 方法，则调用 init-method 指定的方法。
9. 初始化完成后，如果该容器注册了 BeanPostProcessor 则会调用 `postProcessAfterInitialization()` 方法完成 bean 的后置处理。
10. 对象完成初始化，开始方法调用。
11. 在容器进行关闭之前，如果该 bean 实现了 DisposableBean 接口，则调用 `destroy()` 方法。
12. 在容器进行关闭之前，如果该 bean 配置了 destroy-mehod，则调用其指定的方法。
13. 到这里一个 bean 也就完成了它的一生。

## Spring bean 初始化过程，如何解决循环依赖问题

1. spring核心点主要是IOC和AOP，核心技术分别是`反射`和`代理`。 
2. bean的声明周期可以分为：根据配置生成bean对象 -> 为bean传入参数 -> 实现了aware接口可以拿到IOC容器 -> 实现beanpostProcessor接口，可以在bean完成创建的前后去做一些操作 -> 最后还可以配置init-method会调用对应的函数 -> 如果配置了destory则会在bean被回收的时候调用对应函数。
3. 关于bean的循环依赖问题，单例作用域的bean，通过构造器注入时循环依赖会报错，因为创建实例对象时无法完成。而通过set/get注入不会报错，因为先创建了实例，spring会缓存一下创建好的bean去注入到对应依赖的bean中。

使用三级缓存解决循环依赖的问题，`三级缓存`也就是三个Map集合类：

**singletonObjects**：第一级缓存，里面放置的是实例化好的单例对象；

**earlySingletonObjects**：第二级缓存，里面存放的是提前曝光的单例对象；

**singletonFactories**：第三级缓存，里面存放的是要被实例化的对象的对象工厂。

[原文链接](https://blog.csdn.net/itmrchen/article/details/90201279)

![img](/Users/wenzhong/typora-pic/4634752-0d505a800f0711f7.png)

## Spring的循环依赖如何解决？为什么要三级缓存？

```java
protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
        Object exposedObject = bean;
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                    SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                    exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
                }
            }
        }
        return exposedObject;
    }
```

在将三级缓存放入二级缓存的时候，会判断是否有`SmartInstantiationAwareBeanPostProcessor`这样的后置处理器，换句话说这里是给用户提供接口扩展的，所以采用了三级缓存。

## 面试官：“Spring是如何解决的循环依赖？”

答：Spring通过三级缓存解决了循环依赖，其中一级缓存为单例池（`singletonObjects`），二级缓存为早期曝光对象`earlySingletonObjects`，三级缓存为早期曝光对象工厂（`singletonFactories`）。

当A、B两个类发生循环引用时，在A完成实例化后，就使用实例化后的对象去创建一个对象工厂，并添加到三级缓存中，如果A被AOP代理，那么通过这个工厂获取到的就是A代理后的对象，如果A没有被AOP代理，那么这个工厂获取到的就是A实例化的对象。

当A进行属性注入时，会去创建B，同时B又依赖了A，所以创建B的同时又会去调用getBean(a)来获取需要的依赖，此时的getBean(a)会从缓存中获取：

第一步，先获取到三级缓存中的工厂；

第二步，调用对象工工厂的getObject方法来获取到对应的对象，得到这个对象后将其注入到B中。紧接着B会走完它的生命周期流程，包括初始化、后置处理器等。

当B创建完后，会将B再注入到A中，此时A再完成它的整个生命周期。至此，循环依赖结束！

## 面试官：“为什么要使用三级缓存呢？二级缓存能解决循环依赖吗？”

答：如果要使用二级缓存解决循环依赖，意味着所有Bean在实例化后就要完成AOP代理，这样违背了Spring设计的原则，Spring在设计之初就是通过`AnnotationAwareAspectJAutoProxyCreator`这个后置处理器来在Bean生命周期的最后一步来完成AOP代理，而不是在实例化后就立马进行AOP代理。

参考文章：[讲一讲Spring中的循环依赖](https://mp.weixin.qq.com/s/kS0K5P4FdF3v-fiIjGIvvQ)

## Spring的事务传播机制

[参考文章](https://www.toutiao.com/i6820276793514131972)

`REQUIRE`：加入该事务，类比母公司员工；

`REQUIRE_NEW`：开启新事物，类比外派人员；

`NESTED`：嵌套，类比子公司员工。

## @Transactional 实现原理

1. 事务开始时，通过AOP机制，生成一个代理connection对象，并将其放入 DataSource 实例的某个容器中，该容器与 DataSourceTransactionManager 相关。
   在接下来的整个事务中，客户代码都应该使用该 connection 连接数据库，执行所有数据库命令。
   [不使用该 connection 连接数据库执行的数据库命令，在本事务回滚的时候得不到回滚]
   （物理连接 connection 逻辑上新建一个会话session； DataSource 与 TransactionManager 配置相同的数据源）
2. 事务结束时，回滚在第1步骤中得到的代理 connection 对象上执行的数据库命令，然后关闭该代理 connection 对象。（事务结束后，回滚操作不会对已执行完毕的SQL操作命令起作用）

## Spring MVC核心架构

1. 服务启动时，将Dispatcherservlet注入到tomcat中。 

2. tomcat工作线程接收http请求后，将请求转发给Dispatcherservlet。 

3. Dispatcherservlet会查找有@Controller注解标记的类，然后根据uri找到对应目标Controller。 

4. 然后在controller中再次找到对应的方法，最后进行一系列的调用。 

5. 处理完毕后，分为两种方式返回：

   一是前端页面放在后端工程时，可以返回对应的页面模板名字，然后springmcv使用模板技术进行渲染后返回。

   二是前后端分离以后，直接返回前端所需JSON串即可。

## SpringBoot的启动原理

![Spring boot启动原理解析](/Users/wenzhong/typora-pic/61e773491fc8467dbe1fdfcf4e6c339f.jpeg)

### **总览：**

上图为SpringBoot启动结构图，我们发现启动流程主要分为三个部分：

- 第一部分进行SpringApplication的初始化模块，配置一些基本的环境变量、资源、构造器、监听器；
- 第二部分实现了应用具体的启动方案，包括启动流程的监听模块、加载配置环境模块、及核心的创建上下文环境模块；
- 第三部分是自动化配置模块，该模块作为springboot自动配置核心，在后面的分析中会详细讨论。在下面的启动程序中我们会串联起结构中的主要功能。

### **启动：**

每个SpringBoot程序都有一个主入口，也就是main方法，main里面调用SpringApplication.run()启动整个spring-boot程序，该方法所在类需要使用@SpringBootApplication注解，以及@ImportResource注解(if need)，@SpringBootApplication包括三个注解，功能如下：

- @EnableAutoConfiguration：SpringBoot根据应用所声明的依赖来对Spring框架进行自动配置。
- @SpringBootConfiguration(内部为@Configuration)：被标注的类等于在spring的XML配置文件中(applicationContext.xml)，装配所有bean事务，提供了一个spring的上下文环境。
- @ComponentScan：组件扫描，可自动发现和装配Bean，默认扫描SpringApplication的run方法里的Booter.class所在的包路径下文件，所以最好将该启动类放到根包路径下。

**或者**

1. 从`spring.factories`配置文件中**加载`EventPublishingRunListener`对象**，该对象拥有`SimpleApplicationEventMulticaster`属性，即在SpringBoot启动过程的不同阶段用来发射内置的生命周期事件;
2. **准备环境变量**，包括系统变量，环境变量，命令行参数，默认变量，`servlet`相关配置变量，随机值以及配置文件（比如`application.properties`）等;
3. 控制台**打印SpringBoot的`bannner`标志**；
4. **根据不同类型环境创建不同类型的`applicationcontext`容器**，因为这里是`servlet`环境，所以创建的是`AnnotationConfigServletWebServerApplicationContext`容器对象；
5. 从`spring.factories`配置文件中**加载`FailureAnalyzers`对象**,用来报告SpringBoot启动过程中的异常；
6. **为刚创建的容器对象做一些初始化工作**，准备一些容器属性值等，对`ApplicationContext`应用一些相关的后置处理和调用各个`ApplicationContextInitializer`的初始化方法来执行一些初始化逻辑等；
7. **刷新容器**，这一步至关重要。比如调用`bean factory`的后置处理器，注册`BeanPostProcessor`后置处理器，初始化事件广播器且广播事件，初始化剩下的`单例bean`和SpringBoot创建内嵌的`Tomcat`服务器等等重要且复杂的逻辑都在这里实现，主要步骤可见代码的注释，关于这里的逻辑会在以后的spring源码分析专题详细分析；
8. **执行刷新容器后的后置处理逻辑**，注意这里为空方法；
9. **调用`ApplicationRunner`和`CommandLineRunner`的run方法**，我们实现这两个接口可以在spring容器启动后需要的一些东西比如加载一些业务数据等;
10. **报告启动异常**，即若启动过程中抛出异常，此时用`FailureAnalyzers`来报告异常;
11. 最终**返回容器对象**，这里调用方法没有声明对象来接收。