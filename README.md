## 스프링 프레임워크, 스프링 부트
 
* 프레임워크(Framework)  
생산성을 높이기 위해 재사용 가능한 기능들, 표준화된 구조 등을 "추상화"합니다. 기반 라이브러리 등으로 제공하는 환경, 반제품 모듈을 포함합니다. 
프레임워크는 구현하려는 애플리케이션과는 무관하지만 그 기초로 하여 개발을 하게 됩니다.
 
* 스프링 프레임워크(줄여서 스프링)  
자바 애플리케이션 개발을 위한 경량 프레임워크로 <b>IoC(Inversion of Control)</b> 원칙에 기반을 두고 
구성 컴포넌트들이 설계되어 있습니다. IoC는 크게 Dependency Lookup과 Dependency Injection으로 구분되는데 스프링에서는 <b>Dependency Injection(의존성 주입)</b>을 
IoC와 동일한 개념으로 사용하기도 합니다.

* 일반적인 객체 생성 방법과 의존성 주입  
일반적인 객체 생성은 그 객체에 의존하는 클래스에서 new 연산자를 통해 생성합니다. 예를 들어 Bar 라는 객체가 필요하다면
   ```
   Bar bar = new Bar();

   ```

   반면에 스프링에서는 객체 생성이 외부에서 이루어지고 객체 생성과 객체간 의존성 관리를 담당하는 "컨테이너"를 제공합니다. 그래서 스프링에서는 
생성된 객체들을 다음과 같이 사용합니다.
   ```
   @Autowired
   private Bar bar;

   ```
   스프링에서는 애플리케이션을 구성하는 객체들을 "빈(bean)"이라고 하는데 프레임워크에서 "빈(bean) 컨테이너"를 제공하면서 의존성을 관리해준다고 말할 수 있습니다. 컨테이너에 객체 생성과 
   의존성 규칙을 적용하려면 xml 또는 JavaConfig를 사용하여 빈을 정의해주어야 합니다. 

>왜 빈(bean)이라고 할까?<br/>
The motivation for using the name 'bean', as opposed to 'component' or 'object' is rooted in the origins of the Spring Framework itself (it arose partly as a response to the complexity of Enterprise JavaBeans).
   
* 스프링에서 제공하는 IoC 컨테이너  
   스프링 프레임워크가 제공하는 IoC 컨테이너는 크게 BeanFactory와 ApplicationContext로 구분할 수 있겠습니다. 스프링은 이 두 개의 인터페이스를 구현한 다수의 
   구현체들을 제공합니다. 예를 들어서 BeanFactory의 구현체 중 하나인 `DefaultListableBeanFactory`를 사용하여 컨테이너를 만들 수 있습니다.

   ```   
     DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
     BeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
     reader.loadBeanDefinitions(new FileSystemResource("src/main/resources/ioc.xml"));

     FooMaker maker = (FooMaker) factory.getBean("fooMaker"); 
     maker.do();
   
   ```
   위의 예제에서 `ioc.xml`은 다음과 같이 xml 방식으로 빈을 정의한 파일이 되겠습니다. 객체 생성을 직접하지 않고 컨테이너에서 호출하거나 "주입"을 받아서 쓸 수 있게 됩니다.

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
   ApplicationContext는 BeanFactory보다 더 많은 기능들을 제공하는 인터페이스입니다.  
   
   스프링 웹 애플리케이션(스프링 MVC)에서 사용하는 WebApplicationContext도 ApplicationContext를 확장한 것입니다. 웹 애플리케이션은 톰캣과 같은 서버(서블릿 컨테이너라고도 한다)에 의해 구동되므로 
   ContextLoaderListener를 통해 ApplicationContext을 서버에 로드합니다. 스프링의 ContextLoaderListener는 자바의 웹 애플리케이션 스펙에 있는 javax.servlet.ServletContextListener의 구현체입니다.
   
* 스프링 부트  
   스프링 설정은 반복적이고 복잡해서 번거로운 부분이 많이 있습니다. 이러한 부분을 자동화하고 비지니스 로직에 집중할 수 있도록 만든 것이 스프링 부트라고 할 수 있습니다. 
   
   스프링 부트는 `@SpringBootApplication`에서 여러 가지 설정을 자동으로 잡아주고 `SpringApplication.run`은 ApplicationContext를 생성합니다.
   다음은 스프링 부트 기본 실행코드입니다.
   
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
   
   스프링 부트 애플리케이션이 실행되면 자동으로 기본적인 설정이 적용되고 ApplicationContext가 만들어지면서 정의된 빈들을 로드하게 됩니다. 
   
   또 개발자가 여러 의존성 라이브러리들을 찾아서 버전에 맞게 설정해주는 수고를 덜어주기 위해 다양한 Starter 패키지들을 제공합니다. 예를 들어 스프링 MVC 관련된 패키지들을 
   받기 위해서 관련된 여러 패키지들을 직접 찾아서 추가하는 것이 아니라 `spring-boot-starter-web`만 설치하면 되겠습니다.
   
   Starter 리스트는 [여기](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-starter)를 참조하면 됩니다.
   
* 다른 브랜치     
   - 스프링 부트 웹 애플리케이션 [webapp](https://github.com/kate-foo/SimpleSpringBoot/tree/webapp)  
   - 스프링 부트 OAuth2 적용 [oauth2](https://github.com/kate-foo/SimpleSpringBoot/tree/oauth2)
   
   
   