스프링 부트, 스프링 시큐리티, OAuth2를 기반으로 JWT 로그인을 구현한 예제입니다. 구글과 네이버 로그인 기능만이 구현되어 있습니다. 
JWT 유효시간은 300초로 설정되었고 자동 재로그인이나 권한 부분은 구현되어 있지 않습니다.

## 구현 내용

OAuth2의 인증 결과를 세션에 저장하는 대신 JWT 토큰을 생성하고 쿠키를 통해 사용자 에이전트(웹 브라우저)에 전송합니다. 
JWT를 쿠키에 저장하게 되면 웹 브라우저의 모든 요청에 포함되어 API 서버에 전달되고 서버는 JWT를 확인하여 해당 사용자의 요청을 처리할 수 있습니다.


## 실행 방법
 
1. 레포지토리에서 소스파일 받기  
  깃허브 레포지토리에서 소스파일을 받습니다. 백엔드는 스프링 부트, 프론트엔드는 react.js로 작성되어 있습니다. 프론트엔드 프로젝트는 [여기](https://github.com/boyd-dev/SimpleReactApp.git)를 참고하십시오.
   
   ```
   #back-end
   git clone https://github.com/boyd-dev/SimpleSpringBoot.git
   
   #front-end
   git clone https://github.com/boyd-dev/SimpleReactApp.git
   ```  

2. 프로젝트명 변경하기  
   이클립스(또는 STS)에서 프로젝트를 열기 전에 중복되는 프로젝트명이 있는지 확인합니다. 프로젝트명은 `demo-jwt`입니다.
   이미 동일한 이름의 프로젝트가 있는 경우 `settings.gradle`을 열어서 이름을 변경합니다.
   
   ```
   rootProject.name = 'demo-jwt'
   ```     
   
3. 이클립스에서 프로젝트 import 하기  
   이클립스 메뉴 File>Import>Gradle>Existing Gradle Porject를 실행합니다. Project root directory를 git clone으로 가져온 스프링 부트 프로젝트 디렉토리로 지정합니다.
   가져오기가 완료되면 이클립스에 프로젝트 탐색기에서 `demo-jwt`를 확인합니다. 
      

4. 설정 파일 변경하기(로컬환경 기준)  
   `application.yml` 파일을 열어서 설정을 변경합니다. `#Local` 부분에 있는 `client-id`와 `client-secret`을 인증 공급자인 구글과 네이버에서 확인하여 입력합니다.
    `redirect-uri`는 구글과 네이버에서 설정한 인증성공 후 호출되는 URL입니다. 각 인증 공급자의 설정을 `http://localhost:8080/oauth2/callback/{google or naver}`으로 설정합니다
    (다른 경로로 바꿀 수도 있지만 여기서는 `/oauth2/callback/`으로 정합니다).
    
    ```
    google:
        client-id: <Your client-id>
        client-secret: <Your client-secret>
        scope: profile, email
        redirect-uri: http://localhost:8080/oauth2/callback/google
        client-name: google                           
    naver:
        client-id: <Your client-id>
        client-secret: <Your client-secret>
        redirect-uri: http://localhost:8080/oauth2/callback/naver
        authorization-grant-type: authorization_code
        client-name: naver
    ``` 
    
5. 프론트 애플리케이션 프로젝트 위치 지정하기  
   `build.gradle` 파일을 열어서 git clone으로 받은 프론트엔드 애플리케이션의 프로젝트 위치를 지정합니다.
   
   ```
   def frontendDir = "<front-end application project directory>"
   
   ```  

6. 빌드하기  
   프로젝트 탐색창에서 `demo-jwt`를 선택하고 마우스 오른쪽 클릭하여 Gradle>Refresh Gradle Project를 실행하여 라이브러리들을 받습니다. 
   
     
   이클립스 메뉴 Window>Show View>Gradle>Gradle Tasks를 오픈합니다. 트리에서 `build` 폴더를 열어서 `build` 항목을 선택하고 마우스 오른쪽 클릭하여 `Run Gradle Tasks`를 실행합니다.
    

7. 정적 리소스 확인하기  
   스프링 부트 프로젝트가 빌드되면서 프론트엔드 애플리케이션도 함께 생성됩니다(프론트엔드 애플리케이션은 `/resources/static/` 하위로 복사됩니다). 스프링 부트 앱 실행 전에 `/resources/static/`를 F5로 리프레시 하여 
   리액트 빌드가 복사되었는지 확인합니다. 

8. 스프링 부트 애플리케이션 실행  
   `Boot Dashboard`에서 해당 프로젝트를 선택한 후 실행합니다. 웹 브라우저에서 `http://localhost:8080`에 접속하여 동작을 확인합니다. 🚀  
   
   <img src="https://github.com/boyd-dev/SimpleSpringBoot/blob/main/demo-jwt.PNG"/>   