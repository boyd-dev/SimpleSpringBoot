## 스프링 Security OAuth2 + JWT
 
* JWT(Json Web Token)  
   JWT는 서버측의 키로 전자서명되어 클라이언트에 전달되는 json 토큰입니다. 전자서명이 되므로 JWT에 저장되는 데이터(payload)의 위변조 여부를 
   검사할 수 있습니다.
   
   JWT는 세션을 사용하지 않는 환경에서 사용자의 정보를 유지하고 식별할 수 있는 수단을 제공합니다. 흔히 웹 애플리케이션에서 사용자가 로그인을 하면 세션 객체를 사용하는데 이는 
   서버에서 해당 세션을 메모리에 유지해야 된다는 것을 의미합니다. JWT를 이용하게 되면 JWT의 payload에 사용자의 "claim"을 저장하여 주고받게 되고 
   서버에서는 전자서명으로 이 내용을 검사하여 "인증/권한"을 부여받은 사용자임을 확인할 수 있으므로 세션을 이용하지 않아도 됩니다.
   
* OAuth2와 JWT 함께 적용하기  
   OAuth2를 통해 전달받은 인증 결과를 세션에 저장하는 대신 
   JWT 토큰을 생성하고 쿠키를 통해 사용자 에이전트(웹 브라우저)에 전송합니다. JWT를 쿠키에 저장하게 되면 이후 웹 브라우저의 모든 요청에 포함되어 서버에 전달되고 
   서버는 JWT를 확인하여 해당 사용자의 인증/권한 정보에 따라 서비스를 제공할 수 있습니다.
   
   OAuth2와 JWT를 결합하면 먼저 구글이나 페이스북 같은 인증 공급자로부터 부여받는 "접근 토큰(Access token)"과 애플리케이션이 자체적으로 발급하는 토큰, JWT가 있게 됩니다.
   이 두가지가 혼용되어 "접근" 토큰이라고 말할 수 있으므로 이를 구별할 필요가 있습니다.
   
1. 사용자가 최초로 애플리케이션에 접속하면 OAuth2 인증을 통해 접근 토큰을 발급 받아 리소스 서버로부터 사용자 정보를 취득합니다.
2. 애플리케이션은 취득한 사용자 정보를 활용하여 JWT에 사용자 식별 정보를 넣고 쿠키를 생성하여 사용자 에이전트에 전송합니다.
3. 웹 브라우저에 서버로부터 받은 쿠키가 저장되고 이후 모든 요청에 쿠키가 함께 전송됩니다.

   Spring Security의 설정은 다음과 같습니다.
   
   ```
   http
      .authorizeRequests()
          .antMatchers("/index.html").permitAll()
          .antMatchers("/static/**").permitAll()
          .antMatchers("/oauth2Login").permitAll()
          .anyRequest().authenticated()
          .and()
      .csrf().disable()
      .oauth2Login()
          .loginPage("/oauth2Login")
              .redirectionEndpoint()
                 .baseUri("/oauth2/callback/*")
                 .and()
              .userInfoEndpoint().userService(customOAuth2UserService())
                 .and()                  
              .successHandler(customOAuth2SuccessHandler)                    
                 .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
          .logout()
              .deleteCookies("JSESSIONID")
              .logoutSuccessHandler(customLogoutSuccessHandler)
              .and()  
          .addFilterBefore(jwtAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class); 
   ``` 
   
   JWT에서는 세션을 이용하지 않으므로 `.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)`으로 설정합니다. 그러나 세션 자체는 생성됩니다.
   
             
   Security의 기본 설정을 변경하므로 직접 구현해 주여야 하는 핸들러들이 있습니다. 특히 다수의 OAuth2 공급자를 이용할 수 있으므로(예를 들어 구글과 네이버), 각 공급자로 부터 
   전달받은 사용자 정보를 통일된 사용자 객체로 만들어줄 필요가 있습니다. 그래서 `.userInfoEndpoint().userService(customOAuth2UserService())` 처럼 커스터마이징을 할 필요가 있습니다.
   
   
   이렇게 만들어진 사용자 정보를 가지고 JWT를 만들어주는 일은 `.successHandler(customOAuth2SuccessHandler)`에서 수행합니다. 자동 설정된 핸들러를 사용할 수 없으므로 `customOAuth2SuccessHandler`에서 JWT를 만들어서 
   웹 브라우저로 쿠키를 보냅니다. JWT를 저장하는 쿠키는 프론트엔드의 자바스크립트가 읽을 수 없도록 `HttpOnly` 쿠키로 저장합니다. 
   
   
   프론트엔드에서 백엔드의 인증/권한을 받았다는 것을 인식해야 하는데 쿠키를 읽지 못하도록 했기 때문에 `HttpOnly=false`인 쿠키 하나를 더 
   전송하도록 합니다. 이 쿠키는 사용자 정보를 가지고 있지 않고 다만 JWT를 받았는지 여부(플래그)만 가지고 있습니다. 그리고 프론트엔드 애플리케이션에서 
   사용자 정보를 표시하기 위해서 즉시 API 호출을 수행합니다.   
   
   <img src="https://github.com/kate-foo/SimpleSpringBoot/blob/oauth2-jwt/cookie.PNG"/>
   
   아래는 react.js를 사용한 프론트엔드 코드의 예시입니다. 백엔드 API 호출을 위해 `axios` 라이브러리를 사용했습니다. `signIn` 여부만을 체크하고 
   다시 `/api/hello/`를 통해 사용자의 `nickname`을 가져오게 됩니다. 
   
   ```
    const [signIn, setSignIn] = useState(false);
    const [nickname, setNickname] = useState("");
    
    useEffect(() => {
        const v = getCookie("foo-app-jwt-flag");
        if (JSON.parse(v) === true) {
            setSignIn(true);
        }
    }, []);
    
    useEffect(() => {
       
        if (signIn) {
            axios.post("/api/hello/").then((response) => {
                setNickname(response.data);
            }, (error) => {console.log(error)});
        }
        
    }, [signIn]);
   ```
      
   
   이후 웹 브라우저의 모든 요청에는 쿠키에 저장된 JWT가 서버로 전송됩니다. 
   
   
   서버에서는 JWT를 검사하기 위한 별도의 필터가 필요합니다. 그래서 
   다음과 같이 JWT 필터를 작성하여 스프링 필터 체인에 삽입합니다. `.addFilterBefore(jwtAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class)`
   
   <b>JWT 필터가 추가되면 `permitAll`로 설정값들이 기존의 Security 필터 체인은 통과하지만 JWT 필터는 통과하지 못하게 되므로 문제가 발생합니다. 그래서 JWT 필터는 특정 URL에 대해서만 적용될 수 있도록 해야 합니다.</b>
   
   벡엔드의 컨트롤러에서 사용자 정보를 참조하려면 `SecurityContextHolder`를 사용합니다. 예를 들어 다음과 같이 사용할 수 있겠습니다.
         
   ```
   if (!principal.getName().isEmpty()) {
        OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        nickname = user.getAttribute("nickname").toString();
            
        if (logger.isDebugEnabled()) {
            logger.debug(nickname);
        }       
   }       
   ```  
   