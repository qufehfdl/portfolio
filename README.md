<h3>😊 블로그형 개인 프로젝트 </h3>
<div align="center">
	<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/Spring-Framework-%236DB33F?style=flat&logo=spring&logoColor=white" />
	<img src="https://img.shields.io/badge/-JavaScript%20-%23F7DF1E?style=flat&logo=javascript&logoColor=white" />
	<img src="https://img.shields.io/badge/-Ajax-%23F7DF1E?style=flat&logo=Ajax&logoColor=white" />
	<img src="https://img.shields.io/badge/-jQuery-%230769AD?style=flat&logo=jQuery&logoColor=white" />
	<img src="https://img.shields.io/badge/-MySQL-%234479A1?style=flat&logo=MySQL&logoColor=white" />
	<img src="https://img.shields.io/badge/Mybitis-007396?style=flat&logo=Java&logoColor=white" />
</div>

 
 - AWS를 이용해 배포 [📝🔗](http://54.180.140.215:8080/Project/home)<br>
 - 다음 프로젝트 : Vue.js + Spring Boot 를 사용해서 사진커뮤니티 웹앱 제작중! [🥲🔗](https://github.com/qufehfdl/PhotoBox)
 <hr>
<h2>🌈주요 로직</h2>

1. 트래픽 처리
	- 비동기 작업처리를 위한 ThreadPool 구현 : ThreadPoolTaskExecutor 설정[📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/config/ThreadPoolConfig.java#L19)
	- execute(new Runnable(){...}) 익명이너클래스를 사용한<br> 비동기 처리로 ThreadPool을 사용 실행 속도 비교! [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/ThreadPoolController.java#L24)
	- --
	- @Async를 이용한 비동기 처리[📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/service/concurrent/AsyncService.java#L12)
	- SimpleAsyncTaskExecutor / 내가 정의한 ThreadPool 확인[📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/AsyncController.java#L32)
	- --
   	- Interceptor를 이용해서 cache-control 시간 설정 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/interceptor/CacheInterceptor.java#L16)
	- 캐시 유효시간 내에는 메모리 캐시 사용 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/resources/upload/cache2.png)
	- 캐시 유효시간이 초과되면 요청 시 if-modified-since를 보내서<br> 서버측 데이터가 갱신되었는지
	  Last-Modified와 비교 검증!  갱신 되지 않았다면 304 상태코드! [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/resources/upload/cache3.png)
      
2. 멀티쓰레드 동시성 문제
    - ThreadLocal 사용 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/ThreadLocalController.java#L21)
    - Atomic 사용 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/AtomicInstanceController.java#L22)
    - synchronized 키워드 사용 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/SynchronizedController.java#L12)

3. 동기 / 비동기 사용한 댓글 처리
    - Ajax로 데이터 전송 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/WEB-INF/views/board/read.jsp#L16)
    - @PathVariable로 받아 처리 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/ReplyController.java#L39)
    - 🔎실제 작동 [📝🔗](http://54.180.140.215:8080/Project/board/read?board_category=5&content_num=1)

4. 공공API를 활용해 배포한 사이트에 날씨 데이터 출력
    - RestTemplate을 사용 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/RestAPIController.java#L47)
    - Ajax 이용해 메인 화면에 출력 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/WEB-INF/views/home.jsp#L14)
    - 🔎실제 작동 [📝🔗](http://54.180.140.215:8080/Project/home)
      
5. 유효성 검사 : 회원가입,로그인 등등 (아래는 회원가입 관련 유효성검사 입니다)
    - 제한 범위 설정 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/beans/UserBean.java#L13)
    - Validator 추가 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/validation/UserValidator.java#L16)
    - 🔎실제 작동 [📝🔗](http://54.180.140.215:8080/Project/user/join)

6. 인터셉터 처리 : 사용자가 URL을 직접 작성하고 접근하거나 로그인이 아닌 상태에서 접근할 수 없는곳에 접근 할 때
    - preHandle 생성 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/interceptor/CheckLoginInterceptor.java#L21)
    - Interceptor 등록 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/config/ServletAppContext.java#L161)
    - 🔎실제 작동 [📝🔗](http://54.180.140.215:8080/Project/user/modify)
  
<h2>🌈기억나는 문제 해결 or 에러</h2>

1. @RequiredArgsConstructor 사용시 순환참조 에러
    - 생성자 주입 방식에서 스프링이 객체를 생성할 때 주입받아야 하는데 빈이 아직 생성되지 않았으므로<br>
    setter 주입 방식으로 변경 😊BeanLifeCycle !! [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/config/ServletAppContext.java#L70)
<hr>

2. 파일 업로드시 DB의 에러 (Mybatis 부적합한 열유형 1111)
    - DB컬럼에 파일이름을 저장, 사용자가 파일을 올리지 않을 수도 있으므로 null을 허용했지만 <br> Mybatis에서 막음
       null을 허용하는 컬럼을 사용 할때는 반드시 타입을 명시하자 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/service/BoardService.java#L60)
<hr>

3.  HTTP Message Converter / Argument Resolver
    - JSON으로 요청이 올 때 자료형 타입이 맞지 않아 에러가 발생!! [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/HTTP_Test_Controller.java#L30)
    - 자세한 원리[📝🔗](https://github.com/qufehfdl/Study/blob/main/SpringMVC#L155) 
<hr>

4. Atomic 클래스와 ThreadLocal의 차이 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/config/RootAppContext.java#L42)

<hr>

5. ajax로 여러 데이터를 받는 방법
    - onclick 이벤트에 자바스크립트 매개변수로 다수의 데이터 넘김 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/WEB-INF/views/board/read.jsp#L91)
    - 매개 변수로 받아서 처리 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/webapp/WEB-INF/views/board/read.jsp#L16)
<hr>

6. Mybatis의 @SelectKey를 사용해서 Controller에서 온 데이터를 교체해 다시 Controller에 반환시키는 방법
    - 받아온 데이터를 insert가 실행 되기 전에 먼저 실행 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/mapper/BoardMapper.java#L18)
    - 반환된 데이터를 이용해서 방금 작성한글을 바로 볼 수 있는 기능을 구현!
<hr>

7. DeadLock 발생
    - 데드락 상황 발생 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/SynchronizedController.java#L25)
    - 해결 방법 [📝🔗](https://github.com/qufehfdl/portfolio/blob/main/Project/src/main/java/com/hrilke/project/controller/concurrent/SynchronizedController.java#L66)
<hr>

<h2>🌈공부 정리</h2>

1. JAVA [📝🔗](https://velog.io/@hth1991/JAVA-%EA%B8%B0%EB%B3%B8)

2. JSP [📝🔗](https://velog.io/@hth1991?tag=jsp)

3. Spring MVC [📝🔗](https://velog.io/@hth1991/Spring-MVC)

4. Spring [📝🔗](https://velog.io/@hth1991/Spring)

5. 그 외 공부 [📝🔗](https://velog.io/@hth1991/%EC%97%AC%EB%9F%AC%EA%B0%80%EC%A7%80-%EA%B3%B5%EB%B6%80)
