## 실행 방법
 
1. 레포지토리에서 소스파일 받기  
  깃허브 레포지토리에서 소스파일을 받습니다.
   
   ```
   #back-end
   git clone
   
   #front-end
   git clone
   ```  

2. 프로젝트명 변경하기  
   이클립스(또는 STS)에서 프로젝트를 열기 전에 중복되는 프로젝트명이 있는지 확인합니다. 프로젝트명은 `demo-jwt`입니다.
   이미 동일한 이름의 프로젝트가 있는 경우 `settings.gradle`을 열어서 이름을 변경합니다.
   
   ```
   rootProject.name = 'demo-jwt'
   ```     
   
3. 이클립스에서 프로젝트 import 하기  
   이클립스 메뉴 File > Import > Gradle > Existing Gradle Porject를 실행합니다. Project root directory를 git clone으로 가져온 디렉토리로 지정합니다.
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
   이클립스 메뉴 Window>Show View>Gradle>Gradle Tasks를 오픈합니다. 트리에서 `build` 폴더를 열어서 `build` 항목을 선택하고 마우스 오른쪽 클릭하여 `Run Gradle Tasks`를 실행합니다.
    

7. 정적 리소스 확인하기
   빌드를 통해 프론트엔드 애플리케이션도 함께 빌드되어 `/resources/static/` 하위에 생성됩니다. 해당 폴더를 열어서(F5로 리프레시 후) 확인합니다. 

8. 스프링 부트 애플리케이션 실행
   `Boot Dashboard`에서 해당 프로젝트를 선택한 후 실행합니다. 웹 브라우저에서 `http://localhost:8080`에 접속하여 동작을 확인합니다.

      