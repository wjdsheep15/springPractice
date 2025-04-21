package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( //자동으로 스프링빈을 꺼내 자동으로 스프링 빈에 등록
        basePackages = "hello.core.member", // 탐색할 패키지 root 지정 why? 지정하지 않으면 라이브러리까지 다 확인하기 때문에 시간이 많이 듬 그렇기 때문에 지정
        basePackageClasses = AutoAppConfig.class, // 현재 파일의 패키지를 기본으로 지정
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // AppConfig 설정을 제외하는 필터
)
public class AutoAppConfig {


}
