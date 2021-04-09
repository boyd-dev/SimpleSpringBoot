## 프론트엔드 애플리케이션과의 결합
 
* 프론트엔드 애플리케이션  
  리액트와 같은 프론트엔드 전용 애플리케이션 라이브러리를 사용하는 경우 빌드 후에 스프링 부트의 정적 리소스 디렉토리 `/resources/static`으로 배포합니다.
  
  <img src="https://github.com/kate-foo/SimpleSpringBoot/blob/oauth2/frontapp.PNG"/>
  
  이를 위해 [gradle Node.js 플러그인](https://github.com/node-gradle/gradle-node-plugin) `com.github.node-gradle.node`을 사용할 수 있습니다.
  이 플러그인에서 제공하는 npm 또는 yarn 태스크를 사용하여 프론트엔드 애플리케이션을 빌드할 수 있습니다. 빌드 결과물(chunk)을 스프링 부트 정적 리소스 디렉토리로 복사해주는 태스크도 
  추가해주면 백엔드와 함께 패키징될 수 있습니다.
   
   ```   
   def frontendDir = "C:/Users/kate-foo/myapp"

   task copyFrontend(type: Copy) {
      from "$frontendDir/build"
      into "$projectDir/src/main/resources/static"       
   }

   // copy frontend app chunk to static directory after yarn_build task
   copyFrontend.dependsOn(yarn_build)

   compileJava.dependsOn(copyFrontend)
   
   node {
      nodeProjectDir = file("$frontendDir")
   }
   ``` 