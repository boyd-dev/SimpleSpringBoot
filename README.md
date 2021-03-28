## 스프링 MVC, Security
 
* 백엔드 에플리케이션  
백엔드 애플리케이션을 개발하기 위해 스프링 부트를 사용합니다. 일단 스프링 MVC와 로그인/인증에 관련된 다음 스타터를 추가하기로 합니다. 

   ```
   dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-security'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation 'org.springframework.security:spring-security-test'
   }
   
   ```
   레거시 스프링 프레임워크에서는 스프링 MVC와 Security 설정은 복잡하고 번거로운 일이었지만 스프링 부트에서는 스타터를 추가하면 되고 설정을 바꾸기 위한 작업도 훨씬 수월합니다.
   
* 스프링 Security  
   스프링 Security는 이름에서 알 수 있듯이 스프링 기반의 애플케이션에 보안 기능, 즉 로그인, 인증, 권한 등의 기능을 추가할 때 사용할 수 있는 
   라이브러리입니다. 이들 기능은 대부분 자바의 웹 기술 중의 하나인 "필터"에 의해서 제공됩니다. 원래 레거시 스프링에서는 다음과 같이 web.xml에서 
   `DelegatingFilterProxy` 필터 설정을 하여 Security를 적용합니다.  
   
   ```
   <filter>
       <filter-name>springSecurityFilterChain</filter-name>
       <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
   </filter>
   <filter-mapping>
       <filter-name>springSecurityFilterChain</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
   ```
   
   `DelegatingFilterProxy`를 통해 "springSecurityFilterChain"이라는 이름의 `FilterChainProxy` 빈에 필터 처리를 위임합니다. 이후 모든 요청들은 Security의 필터들을 거치게 됩니다.
   대표적인 필터들은 다음과 같습니다. 필터는 순서가 중요합니다.
   
   ```
   ChannelProcessingFilter,
   SecurityContextPersistenceFilter,
   ConcurrentSessionFilter, 
   UsernamePasswordAuthenticationFilter,
   SecurityContextHolderAwareRequestFilter,
   RememberMeAuthenticationFilter,
   AnonymousAuthenticationFilter,
   ExceptionTranslationFilter,
   FilterSecurityInterceptor
   ```
   이런 필터들을 커스터마이징하지 않는다면 모든 필터들이 적용되고 필터의 동작에 의해 통과되지 못하는 요청은 처리되지 않게 되는 것입니다. 
   공식 [문서](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)의 그림을 보면 이해하는데 도움이 될 것 같습니다.
   
   <img src="https://github.com/kate-foo/SimpleSpringBoot/blob/webapp/sec-chain.PNG"/>
   
* WebSecurityConfigurerAdapter  
Security의 기본 설정을 하려면 `WebSecurityConfigurerAdapter`를 상속받고 `@EnableWebSecurity`을 사용합니다.
그리고 `configure` 메소드를 override 합니다. 예를 들어 다음과 같이 설정을 바꿀 수 있습니다.

   ```
   @Configuration
   @EnableWebSecurity
   public class MySecurityConfig extends WebSecurityConfigurerAdapter {

       @Override
       protected void configure(HttpSecurity http) throws Exception {        
            http
              .authorizeRequests()
                 .anyRequest().authenticated()
                 .and()
              .formLogin()
                 .defaultSuccessUrl("/main.do", true);      
       }
   }
   ``` 
   위의 설정을 xml 방식으로 바꾸면 다음과 같습니다.
   
   ```
   <security:http auto-config="true" use-expressions="true">
       <security:intercept-url pattern="/**" access=“authenticated" />
       <security:form-login default-target-url="/main.do" always-use-default-target="true" />       
   </security:http>
   ```
   
* Spring MVC  
spring-boot-starter-web 스타터를 추가하면 레거시 스프링 MVC에서 추가해주었던 MVC 라이브리러들이 자동으로 적용됩니다. 예를 들어 
spring-webmvc.jar 같은 의존성 라이브러리들이 포함되고 별도로 다른 설정을 하지 않아도 컨트롤러, 서비스 클래스 등을 작성해서 웹 애플리케이션을 
만들 수 있습니다. 예를 들어 다음과 같은 `DispatcherServlet` 설정은 이미 적용되어 있습니다.

   ```
   <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>        
        <load-on-startup>1</load-on-startup>
   </servlet>
   ```    
   MVC 흐름을 간단히 나타내면 다음과 같습니다.
   
   <img src="https://github.com/kate-foo/SimpleSpringBoot/blob/webapp/spring-mvc.PNG"/>
   
   스프링 부트에서는 핸들러 매핑 인터페이스의 디폴트 구현체로 `RequestMappingHandlerMapping`이 설정되어 있습니다. 컨트롤러 클래스의 메소드에 `@RequestMapping`를 붙여서 요청을 처리하게 됩니다. 최종적인 화면을 만들어주는 
   뷰 리졸버는 `InternalResourceViewResolver`입니다. 
   
   MVC와 관련된 기본 설정은 `spring-boot-starter-web`이 추가되었을 때 적용되는 것이고 만약에 사용자 설정을 적용하기 위해서는 `WebMvcConfigurer`라는 인터페이스를 구현해야 합니다.  
   
   기본 설정을 어느 정도 유지하기 위해서는 `@EnableWebMvc`를 붙여주게 됩니다. MVC 설정은 복잡하기도 하고 번거롭기 때문에 관례적으로, 흔히 사용되는 설정을 자동으로 적용하도록 되어 있습니다.
   이런 패턴을 "Convention over Configuration" 이라고 합니다. 모든 설정을 개발자가 처음부터 하는 것이 아니라 일반적은 설정을 적용하고 필요에 의해 사용자 설정을 할 수 있도록 하는 것입니다.
   
   ```
   @Configuration
   @EnableWebMvc
   public class MyMvcConfig implements WebMvcConfigurer {
      
       @override
       ...
   }
   ```
   그런데 기본 설정을 override하는 경우에는 일부 기본 설정이 사라지기 때문에 WebMvcConfigurer 구현시 최소한의 설정을 작성해주어야 합니다(아마도 WebMvcConfigurer 인터페이스에 구현된 default 메소드가 없는 경우). 예를 들어 defaultViewResolver는 
   `InternalResourceViewResolver`이지만 WebMvcConfigurer의 구현체를 만들게 되면 defaultViewResolver가 등록되지 않으므로 꼭 작성해주어야 합니다.
   
   