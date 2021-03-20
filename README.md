## 스프링 프레임워크, 스프링 부트
 
* 프레임워크(Framework)  
생산성을 높이기 위해 재사용 가능한 기능들, 표준화된 구조 등을 "추상화"한다. 기반 라이브러리 등으로 제공하는 환경, 반제품 모듈을 포함한다. 
구현하려는 애플리케이션과는 무관하지만 그 기초가 된다.
 
* 스프링 프레임워크(줄여서 스프링)  
자바 애플리케이션 개발을 위한 경량 프레임워크로 <b>IoC(Inversion of Control)</b> 원칙에 기반을 두고 
구성 컴포넌트들이 설계되어 있다. IoC는 크게 Dependency Lookup과 Dependency Injection으로 구분되는데 스프링에서는 <b>Dependency Injection(의존성 주입)</b>을 
IoC와 동일한 개념으로 사용된다.

* 일반적인 객체 생성 방법과 의존성 주입  
일반적인 객체 생성은 그 객체에 의존하는 클래스에서 new 연산자를 통해 생성한다. 예를 들어 Bar 라는 객체가 필요하다면
   ```
   Bar bar = new Bar();

   ```

   반면에 스프링에서는 객체 생성이 외부에서 이루어지고 객체 생성과 객체간 의존성 관리를 담당하는 "컨테이너"를 제공한다. 그래서 스프링이 구동되면서 
생성된 객체들을 다음과 같이 사용하면 된다.
   ```
   @Autowire
   private Bar bar;

   ```
   스프링에서는 애플리케이션을 구성하는 객체들을 "빈(bean)"이라고 하는데 그래서 스프링은 "IoC 컨테이너"를 통해 의존성을 관리해주는 프레임워크라고 말할 수 있다. 컨테이너가 의존성과 객체 생성을 
담당하려면 xml 방식으로 설정 파일을 작성하거나 JavaConfig를 사용하여 빈을 정의해주어야 한다. 

>왜 빈(bean)이라고 할까?<br/>
The motivation for using the name 'bean', as opposed to 'component' or 'object' is rooted in the origins of the Spring Framework itself (it arose partly as a response to the complexity of Enterprise JavaBeans).
   
* 스프링에서 제공하는 IoC 컨테이너  
   스프링 프레임워크가 제공하는 IoC 컨테이너는 크게 BeanFactory와 ApplicationContext로 구분할 수 있다. 스프링은 이 두 개의 인터페이스를 구현한 다수의 
   구현체들을 제공한다. 예를 들어서 BeanFactory의 구현체 중 하나인 `DefaultListableBeanFactory`를 사용하여 컨테이너를 생성할 수 있다.

   ```
   public void someMethod() {

      DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
      BeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
      reader.loadBeanDefinitions(new FileSystemResource("src/main/resources/ioc.xml"));

      FooMaker maker = (FooMaker) factory.getBean("fooMaker"); 
      maker.do();
   }
   ```
   `ioc.xml`은 다음과 같이 xml 방식으로 빈을 정의한 파일이다. 객체 생성을 직접하지 않고 단지 컨테이너에서 호출해서 쓸 수 있게 된다.

   ```
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

      <bean id="fooMaker" class="edu.study.examples.FooMaker"/>
   </beans>
   ```
   
* ApplicationContext  
   ApplicationContext는 BeanFactory보다 더 많은 기능들을 제공하는 인터페이스이다.  
   
   스프링 웹 애플리케이션(스프링 MVC)에서 사용하는 WebApplicationContext도 ApplicationContext를 확장한 것이다. 웹 애플리케이션은 톰캣과 같은 서버(서블릿 컨테이너라고도 한다)에 의해 구동되므로 서버의 ContextLoaderListener를 통해 ApplicationContext을 서버에 로드한다.
   
* 스프링 부트  
   스프링 설정은 반복적이고 복잡해서 번거로운 부분이 많이 있다. 이러한 부분을 자동화하고 비지니스 로직에 집중할 수 있도록 만든 것이 스프링 부트이다. 
   
   스프링 부트는 `@SpringBootApplication`에서 여러 가지 설정을 자동으로 잡아주고 `SpringApplication.run`은 ApplicationContext를 생성한다.
   다음은 스프링 부트 기본 실행코드이다. 
   
   ```
   @SpringBootApplication
   public class DemoApplication {

      public static void main(String[] args) {
          ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        
          IHelloWorldService hsb = (IHelloWorldService) ctx.getBean("helloWorldService");
          String str = hsb.sayHello();        
          System.out.println(str);        
      }
   
   }
   ```
   
   
   