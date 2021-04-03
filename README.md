## 스프링 Security OAuth2
 
* OAuth2 클라이언트  
   OAuth2는 애플리케이션 사용 권한에 관한 업계 표준으로, 애플리케이션이 사용자들의 정보와 권한을 관리하는 것을 간소화시켜주는 표준 프로토콜이라고 할 수 있습니다. 
   >OAuth 2.0 is the industry-standard protocol for authorization. OAuth 2.0 focuses on client developer simplicity while providing specific authorization flows for web applications, desktop applications, mobile phones, and living room devices.
   
   OAuth2에서는 애플리케이션 사용을 허락하는 방식, grant type에 따라 몇 가지로 나눌 수 있습니다. 
   
   - Authorization code
   - Implicit
   - Client Credentials
   
   웹 애플리케이션에 많이 사용되는 방식은 Authorization code 방식입니다. Authorization code 방식에서 인증과 권한 허용과 부여에 따른 주체들은 다음과 같습니다.
   
   - Resource owner
   - Client
   - Authorization server
   - Resource server  
   
   <img src="https://github.com/kate-foo/SimpleSpringBoot/blob/oauth2/oauth2.PNG"/>
   
   여기서 "Resource owner"는 사용자를 말하고 "Client"는 애플리케이션을 뜻합니다.  
   
   Authorization/Resource 서버 자체를 만들 수도 있고 그것을 이용하는 클라이언트를 만들 수도 있습니다. 이 글에서는 클라이언트를 만드는 것을 중심으로 합니다. 클라이언트는 
   웹 애플리케이션이라고 생각하면 되겠습니다. 사용자가 애플리케이션의 사용 권한을 받게 되는 흐름은 다음과 같습니다.
   
1. 애플리케이션은 사용자가 자신의 애플리케이션을 사용할 권한을 확인하기 위해(구글이나 페이스북 같은 제 3자로부터) 정보 조회를 요청합니다.  
2. 사용자가 애플리케이션에게 자신의 정보를 조회하는 권한을 허락합니다. 이 과정에서, 예를 들어 구글의 로그인 폼이 표시됩니다.
3. 사용자는 제공되는 로그인 폼에서 로그인을 하여 자신임을 인증받습니다. 이것은 애플리케이션이 자신의 정보를 볼 수 있도록 허락하는 과정입니다.
4. 애플리케이션은 제 3자로부터 해당 사용자 정보를 조회할 수 있는 액세스 토큰(access token)을 리턴받습니다.
5. 애플리케이션은 액세스 코드를 사용하여 사용자 정보를 확인하고 서비스를 제공합니다.
6. 원칙적으로 액세스 코드만 있으면 사용자 정보를 누구나 볼 수 있으므로 액세스 코드에는 만료 시간이 있습니다. 만료된 액세스 코드는 사용할 수 없습니다.
7. 액세스 토큰이 만료되면 갱신 토큰(refresh token)을 사용하여 액세스 토큰을 재발급 받아 애플리케이션을 계속 사용할 수 있습니다.
   
* Spring Security OAuth2  
  스프링에서는 OAuth2 기능을 제공하기 위한 여러 가지 라이브러리를 제공합니다. 이전에 많이 사용되던 라이브러리는 다음과 같은 것들이 있었습니다.
  
  - org.springframework.security.oauth:spring-security-oauth2
  
  스프링 부트에서는 다음 라이브러리를 사용했었습니다.
  
  - org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure
  
  과거형으로 적은 이유는 현재 이들 라이브러리들은 deprecated 되거나 maintenance 모드가 되었기 때문입니다. 앞으로 OAuth2 기능은 Spring Security 프로젝트 안으로 
  통합되어 개발될 것이라고 합니다. 이러한 방향에 대한 자세한 사항은 다음 글을 참조하시기 바랍니다.
  
  [https://projects.spring.io/spring-security-oauth/docs/Home.html](https://projects.spring.io/spring-security-oauth/docs/Home.html)

  여러 레포와 자료들이 이전 라이브러리들을 기반으로 작성되어 있어서 혼란스러운 부분들이 있을 것 같습니다. 예를 들어 관련된 어노테이션이나 클래스를 사용하면 다음과 같이 deprecated 되었다는 사실을 
  알 수 있습니다.
  
  ~~@EnableOAuth2Client~~  
  ~~OAuth2ClientContextFilter~~  
  ~~OAuth2ClientAuthenticationProcessingFilter~~
  
* Spring Boot OAuth2    
  
  스프링 부트는 이러한 복잡한 상황을 간단하게 해결해 주는 스타터 라이브러리가 있습니다. 그래서 스타터만 추가하면 OAuth2 클라리언트 기능을 쉽게 추가할 수 있게 됩니다.  

   ```
   implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
   ```
    