package com.hrilke.project.config;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hrilke.project.OOP.MyWrite;
import com.hrilke.project.beans.UserBean;
import com.hrilke.project.interceptor.CacheInterceptor;
import com.hrilke.project.interceptor.CheckLoginInterceptor;
import com.hrilke.project.interceptor.CheckWriterInterceptor;
import com.hrilke.project.interceptor.TopMenuInterceptor;
import com.hrilke.project.mapper.BoardMapper;
import com.hrilke.project.mapper.ReplyMapper;
import com.hrilke.project.mapper.TopMapper;
import com.hrilke.project.mapper.UserMapper;
import com.hrilke.project.service.BoardService;
import com.hrilke.project.service.TopService;

import lombok.RequiredArgsConstructor;

// Spring MVC 프로젝트에 관련된 설정을 하는 클래스
@Configuration
// Controller 어노테이션이 셋팅되어 있는 클래스를 Controller로 등록
@EnableWebMvc
// 스캔할 패키지를 지정
@ComponentScan("com.hrilke.project.controller")
@ComponentScan("com.hrilke.project.service")
@ComponentScan("com.hrilke.project.dao")
@PropertySource("/WEB-INF/properties/db.properties")
@RequiredArgsConstructor
@EnableAsync 
public class ServletAppContext implements WebMvcConfigurer {
	
	@PostConstruct
	public void init() {
		System.out.println("컨테이너 시작");
	}

	@Value("${db.classname}")
	private String db_classname;

	@Value("${db.url}")
	private String db_url;

	@Value("${db.username}")
	private String db_username;

	@Value("${db.password}")
	private String db_password;

//  불변성 보장!
	@Qualifier("loginUserBean")
	private final UserBean loginUserBean;

//	@RequiredArgsConstructor를 적용하기위해 final로 설정하면 순환참조 에러 발생!
//	생성자 주입 방식에서 스프링이 객체를 생성할 때 주입받아야 하는데 빈이 아직 생성되지 않았을 가능성이 있다
	private TopService topService;
	private BoardService boardService;

//	세터 주입 방식을 사용해서 컨테이너가 객체를 생성한 다음 주입받도록 하자!
	@Autowired
	public void setTopService(TopService topService) {
		this.topService = topService;
	}

	@Autowired
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	// Controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		// 정적 파일의 경로를 매핑
		registry.addResourceHandler("/**").addResourceLocations("/resources/");

	}

	// 데이터베이스 접속 정보를 관리하는 Bean : org.apache.commons.dbcp2.BasicDataSource로 만들자
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		return source;
	}

	// 쿼리문과 접속 정보를 관리하는 객체
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}

	// 쿼리문 실행을 위한 객체(Mapper 관리)
	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<TopMapper> getTopMapper(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<TopMapper> factoryBean = new MapperFactoryBean<TopMapper>(TopMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<ReplyMapper> getReplyMapper(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<ReplyMapper> factoryBean = new MapperFactoryBean<ReplyMapper>(ReplyMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<MyWrite> getMyWrite(SqlSessionFactory factory) throws Exception {
		MapperFactoryBean<MyWrite> factoryBean = new MapperFactoryBean<MyWrite>(MyWrite.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	// 인터셉터 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);

		// 파일경로가 있던 없던 모든요청에 대해서 이 탑 메뉴 인터셉터를 통과하게
		TopMenuInterceptor topMenuInterceptor = new TopMenuInterceptor(topService, loginUserBean);
		InterceptorRegistration reg1 = registry.addInterceptor(topMenuInterceptor);
		reg1.addPathPatterns("/**");

		// 이 경로로 들어오면 로그인되어있는지 안되어있는지 인터셉터가 검사!
		InterceptorRegistration reg2 = registry.addInterceptor(new CheckLoginInterceptor(loginUserBean));
		reg2.addPathPatterns("/user/modify", "/user/logout", "/board/*");
		// 이 경로는 제외
		reg2.excludePathPatterns("/board/main", "/board/searchList", "/board/read");

		// 수정 삭제를 잘못된 접근으로 이용할 시
		CheckWriterInterceptor checkWriterInterceptor = new CheckWriterInterceptor(loginUserBean, boardService);
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriterInterceptor);
		reg3.addPathPatterns("/board/modify", "/board/delete");

		// cache 처리
		CacheInterceptor cacheInterceptor = new CacheInterceptor();
		InterceptorRegistration reg4 = registry.addInterceptor(cacheInterceptor);
		reg4.addPathPatterns("/board/read");

	}

	// 자바 방식으로 스프링세팅시 @PropertySource("/WEB-INF/properties/db.properties") 와
	// 메세지 등록코드와 충돌이 나서 인식이 안되므로 프로퍼티소스등록과 메세지소스등록을 구분해서 별도로 관리해주는 코드 작성
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// 메세지 등록
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setBasenames("/WEB-INF/properties/error_message");
		return res;
	}

	// 파일 업로드를 위해 빈 정의
	// 이것만 해주면 안되고 멀티파트세팅에 대한 관련된 정보도 세팅해주어야 한다 SpringConfigClass로 이동
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

}

//DispatcherServlet의 동작 과정

// HTTP 프로토콜로 들어오는 모든 요청을 가장 [ 먼저 ] 받아 
// 적합한 컨트롤러에 위임해주는 [ 프론트 컨트롤러 ]

//		1.클라이언트의 request가 오면 DispatcherServlet이 먼저 받아서 HandlerMapping 에게 보내줌
//		2.HandlerMapping은 가장 적합한 Controller를 찾아서 
//		3.다시 DispatcherServlet으로 보낸 후 HandlerAdapter에게 보냄
//		4.HandlerAdapter는 HandlerMapping이 찾아준 Controller안에 가장 적합한 메서드를 찾아줌
//		5.Controller가 기능을 수행
//     			[이 사이 DB도 왔다갔다]
//		6.수행 한 후 결과를 가지고 DispatcherServlet 와서 ViewResolver에게 감
//		7.ViewResolver가 결과에 가장 적합한 JSP를 찾아줌 
//		8.View가 클라이언트에게 response함
 
//         |              Web Context             |
//		          |           Spring Context      |
//    요청 -> 필터 -> 디스파쳐서블릿 -> 인터셉터 -> 컨트롤러


// 핸들러 어댑터가 컨트롤러로 요청을 넘기기 전에 공통적인 전처리 과정

//   요청에 매칭되는 인터셉터들도 실행을 시키고 @RequestParam  @RequestBody  @PathVariable 으로 
//   파라미터를 준비하는 ArgumentResolver도 실행하는 등의 다양한 공통 작업들이 수행됨!
//   전처리 작업들이 완료되면 파라미터 값들과 함께 컨트롤러로 요청을 위임
