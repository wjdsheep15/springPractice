# Spring Practice
2025. 03 ~ 진행중
### 스프링을 왜 공부하는가?
스프링으로 프로젝트를 벌써 4개를 진행했지만 아직도 스프링에 대한 이해도가 떨어지고 사용하고 있지만 정작 이 어노테이션을 왜 사용하는지 모르기때문에 기초부터 공부할 필요성을 느꼈습니다.
그렇기에 스프링 공부에 가장 대중적이고 유명한 김영환 선생님의 강의를 보고 이해한 것을 정리 및 실습으로 스프링 이해와 클린 코드 짜는 방법 그리고 TDD로 하는 방식을 배워나갈 겁니다.

## Spring 정리
조금씩 작성해 나갈 것


# 8. 의존관계 자동 주입
### 다양한 의존관계 주입 방법
의존관계 주입은 크개 4가지
- 생성자 주입
- 수정자 주입(setter 주입)
- 필드 주입
- 일반 메서드 주입


### 생성자 주입 (요즘 많이 사용)
- 이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법이다.
- 지금까지 우리가 진행했던 방법이 바로 생성자 주입이다.
- 특징
    - 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
      -*불변, 필수* 의존관계에 사용
  ```java
  @Component  
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;  
    private final DiscountPolicy discountPolicy;  
  
    @Autowired  
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {  
        this.memberRepository = memberRepository;  
        this.discountPolicy = discountPolicy;  
    }
    }...
	
	```

```
> private final 은 무조건 값이 있어야한다.
> ```java
> private final MemberRepository memberRepository;  
private final DiscountPolicy discountPolicy;  
>
>@Autowired  
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {  
    this.memberRepository = memberRepository;  
    this.discountPolicy = discountPolicy;  
}
```

* 생성자가 하나일때는 @Autowired는 생약이 가능하다.
```java
@Component  
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;


    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

### 수정자 주입
- setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법이다.
- 특징
    - ****선택**, **변경**** 가능성이 있는 의존관계에 사용
    - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.

```java
@Component
public class OrderServiceImpl implements OrderService {

	private MemberRepository memberRepository;
	private DiscountPolicy discountPolicy;

	@Autowired(required = false) // 필수가 아니니 뺄 수 있다.
	public void setMemberRepository(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Autowired
	public void setDiscountPolicy(DiscountPolicy discountPolicy) {
		this.discountPolicy = discountPolicy;
	}
}

```
> 참고: `@Autowired` 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 주입할 대상이 없어도 동작하게 하려면 `@Autowired(required = false)`로 지정하면 된다.

> 참고: 자바빈 프로퍼티, 자바에서는 과거부터 필드의 값을 직접 변경하지 않고, setXxx, getXxx 라는 메서드를통해서 값을 읽거나 수정하는 규칙을 만들었는데, 그것이 자바빈 프로퍼티 규약이다. 더 자세한 내용이 궁금하면자바빈 프로퍼티로 검색해보자


****자바빈** **프로퍼티** **규약** **예시****
```java
class Data {

	private int age;

	public void setAge(int age) {

		this.age = age;
	}

	public int getAge() {

		return age;

	}

}
```

### 필드 주입
- 이름 그대로 필드에 바로 주입하는 방법
- 특징
    - 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다.
    - DI 프레임워크가 없으면 아무것도 할 수 없다.
    - 사용하지 말자!
        - 애플리케이션의 실제 코드와 관계 없는 테스트 코드
        - 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용

```java
@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private DiscountPolicy discountPolicy;

}
```

### 일반 메서드 주입
- 일반 메서드를 통해서 주입 받을 수 있다.
- 특징
    - 한번에 여러 필드를 주입 받을 수 있다.
    - 일반적으로 잘 사용하지 않는다.
```java
@Component  
public class OrderServiceImpl implements OrderService {  
  
    private  MemberRepository memberRepository;  
    private  DiscountPolicy discountPolicy;
  
	@Autowired  
	public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {  
	    this.memberRepository = memberRepository;  
	    this.discountPolicy = discountPolicy;  
	}
}

```

### 옵션처리
주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
`@Autowired` 만 사용하면 required 옵션의 기본값이 true로 되어있다. 자동 주입 대상이 없으면 오류가 발생한다.

자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다.
- `@Autowired(required=false)` : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
- `org.springframework.lang.@Nullable` : 자동 주입할 대상이 없으면 null이 입력된다.
- `Optional<>` : 자동 주입할 대상이 없으면 `Optional.empty` 가 입력된다.

## 생성자 주입을 선택해라!
최근 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장함
****불변****
- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 오히려 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안된다.(불변해야 한다.)
- 수정자 주입을 사용하면, setXxx 메서드를 public으로 열어두어야 한다.
- 누군가 실수로 변경할 수 도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
- 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.
 
setter로 생성자 주입을 사용하면 다음처럼 주입 데이터를 누락 했을 때 **컴파일 오류**가 발생한다

### final **키워드****

생성자 주입을 사용하면 필드에 `final` 키워드를 사용할 수 있다. 그래서 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다. 다음 코드를 보자.
- 기억하자! ****컴파일** **오류는** **세상에서** **가장** **빠르고**, **좋은** **오류다**!**
## 롬복과 최신 트랜드
막상 개발ㅇ르 해보면 대부분 불변이다. 생성자는 final 키워드를 사용
그런데 생성자도 만들고 주입 받은 값을 대입하는 코드


```gradle
plugins {  
    id 'java'  
    id 'org.springframework.boot' version '3.4.2'  
    id 'io.spring.dependency-management' version '1.1.7'  
}  
  
group = 'hello'  
version = '0.0.1-SNAPSHOT'  
  
java {  
    toolchain {  
       languageVersion = JavaLanguageVersion.of(21)  
    }}  
  
//lombok 설정 추가 시작  
configurations {  
    compileOnly {  
       extendsFrom annotationProcessor  
    }  
}  
//lombok 설정 추가 끝  
  
repositories {  
    mavenCentral()  
}  
  
dependencies {  
    implementation 'org.springframework.boot:spring-boot-starter'  
  
    //lombok 라이브러리 추가 시작  
    compileOnly 'org.projectlombok:lombok'  
    annotationProcessor 'org.projectlombok:lombok'  
    testCompileOnly 'org.projectlombok:lombok'  
    testAnnotationProcessor 'org.projectlombok:lombok'  
//lombok 라이브러리 추가 끝  
  
    testImplementation 'org.springframework.boot:spring-boot-starter-test'  
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'  
}  
  
tasks.named('test') {  
    useJUnitPlatform()  
}
```


`@RequiredArgsConstructor` final 붙은 필드를 모아서 생성자를 자동으로 만들어줌

## 조회 빈이 2개 이상 - 문제
DiscountPolicy의 하위 타입인 FixDiscountPolicy, RateDiscountPolicy 둘다 스프링 빈으로 등록
@component를 붙여준다.
=> @Autowired로 인해서 NoUniqueBeanDefinitionException 오류가 발생
하나의 빈을 기대했으나 두개의 빈이 발견

하위 타입으로 지정하면 DIP 위배하고 유연성이 떨어진다.

## @Autowired 필드 명 , @Quilifier, @Primar

조회 대상 빈이 2개 이상일 때 해결 방법
- @Autowired 필드 명 매칭
- @Qualifier => @Qualifier 끼리 매칭 => 빈 이름 매칭
- @Primary 사용

@Autowired  필드 명 매칭
```java
@Autowired
private DiscountPolicy rateDiscountPolicy;
```
필드 명이 rateDiscountPolicy이므로 정상 주입

@Autowired 매칭 정리
- 타입 매칭
- 타입 매칭의 결과가 2개 이상일 때 필드 명, 파라미터 명으로 빈 이름 매칭

### @Qualifier 사용
@Qualifier는 주입시 구분자 붙여주는 방식, 빈 이름을 변경하는 것이 아님
```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}
```

생성자 자동 주입 예시
```java
@Autowired
public OrderServiceImple(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
	this.memberRepository = memberRepository;
	this.discountPolicy = discountPolicy;
}
```
@ Qualifier 정리
- @Qualifier 끼리 매칭
- 빈 이름 매칭
- NoSuchBeanDefinitionException 예외 발생

### @Primary  사용
@Autowired 시에 여러 빈이 매칭되면 `@Primary` 가 우선권을 가진다.

`rateDiscountPolicy` 가 우선권을 가지도록 하자.
```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {}

```

**@Primary, @Qualifier **활용****
@Primary 활용
- 자주 사용하는 메인 데이터베이스의 커넥션

@Qualifier 활용
- 서브 데이터베이스

****우선순위****
`@Primary` < `@Qualifier`

## 애노테이션 직접 만들기
`@Qualifier("mainDiscountPolicy")` 문자로 적으면 컴파일시 타입 체크가 안된다. 따라서 컴파일시 타입 체크를 하기 위해 애노테이션을 만들어서 사용하면 된다.

```java
// 애노테이션 생성
package hello.core.annotataion;

import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {

}

// 애노테이션 적용
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {}

//생성자 자동 주입

@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
	this.memberRepository = memberRepository;
	this.discountPolicy = discountPolicy;
}

//수정자 자동 주입
@Autowired
public DiscountPolicy setDiscountPolicy(@MainDiscountPolicy DiscountPolicy discountPolicy) {
	this.discountPolicy = discountPolicy;
}
```

- 애노테이션에는 상속이라는 개념이 없다.
- @Qualifier 뿐만 아니라 다른 애노테이션들도 함께 조합해서 사용할 수 있다.

## 조회한 빈이 모두 필요할 때 , List, Map
의도적으로 해당 타입의 스프링 빈이 다 필요한 경우 => 예) 클라이언트가 해당 할인 종류(rate, fix) 선택할 수 있을때
****전략패턴 사용****
```java
package hello.core.autowired;  
  
import hello.core.AppConfig;  
import hello.core.AutoAppConfig;  
import hello.core.discount.DiscountPolicy;  
import hello.core.member.Grade;  
import hello.core.member.Member;  
import org.assertj.core.api.Assertions;  
import org.junit.jupiter.api.Test;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.annotation.AnnotationConfigApplicationContext;  
import org.springframework.context.annotation.Configuration;  
  
import java.lang.annotation.Annotation;  
import java.util.List;  
import java.util.Map;  
  
import static org.assertj.core.api.Assertions.*;  
  
public class AllBeanTest {  
  
    @Test  
    void findAllBean() {  
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);  
  
        DiscountService discountService = ac.getBean(DiscountService.class);  
        Member member = new Member(1L, "userA", Grade.VIP);  
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");  
  
        assertThat(discountService).isInstanceOf(DiscountService.class);  
        assertThat(discountPrice).isEqualTo(1000);  
  
        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");  
        assertThat(rateDiscountPrice).isEqualTo(2000);  
  
    }  
    static class DiscountService {  
        private final Map<String, DiscountPolicy> policyMap;  
        private final List<DiscountPolicy> policies;  
  
        public DiscountService(Map<String, DiscountPolicy> policyMap,  
                               List<DiscountPolicy> policies) {  
            this.policyMap = policyMap;  
            this.policies = policies;  
            System.out.println("policyMap = " + policyMap);  
            System.out.println("policies = " + policies);  
        }  
        public int discount(Member member, int price, String discountCode) {  
            DiscountPolicy discountPolicy = policyMap.get(discountCode);  
            return discountPolicy.discount(member, price);  
        }    }}
```
**로직** **분석****
- DiscountService는 Map으로 모든 `DiscountPolicy` 를 주입받는다. 이때 `fixDiscountPolicy`,`rateDiscountPolicy` 가 주입된다.
- `discount ()` 메서드는 discountCode로 "fixDiscountPolicy"가 넘어오면 map에서`fixDiscountPolicy` 스프링 빈을 찾아서 실행한다. 물론 “rateDiscountPolicy”가 넘어오면`rateDiscountPolicy` 스프링 빈을 찾아서 실행한다.

****주입** **분석****
- `Map<String, DiscountPolicy>` : map의 키에 스프링 빈의 이름을 넣어주고, 그 값으로`DiscountPolicy` 타입으로 조회한 모든 스프링 빈을 담아준다.

- `List<DiscountPolicy>` : `DiscountPolicy` 타입으로 조회한 모든 스프링 빈을 담아준다.
- 만약 해당하는 타입의 스프링 빈이 없으면, 빈 컬렉션이나 Map을 주입한다.

****참고** - **스프링** **컨테이너를** **생성하면서** **스프링** **빈** **등록하기****

스프링 컨테이너는 생성자에 클래스 정보를 받는다. 여기에 클래스 정보를 넘기면 해당 클래스가 스프링 빈으로 자동 등록된다.

`new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);`

이 코드는 2가지로 나누어 이해할 수 있다.
- `new AnnotationConfigApplicationContext()` 를 통해 스프링 컨테이너를 생성한다.
- `AutoAppConfig.class` , `DiscountService.class` 를 파라미터로 넘기면서 해당 클래스를 자동으로 스프링 빈으로 등록한다.

정리하면 스프링 컨테이너를 생성하면서, 해당 컨테이너에 동시에 링 빈으로 자동 등록한다.

## 자동, 수동의 올바른 실무 운영 기준
****편리한 자동 기능을 기본으로 사용하자****
스프링은 `@Component` 뿐만 아니라 `@Controller` , `@Service`, `@Repository` 처럼 계층에 맞추어 일반적인 애플리케이션 로직을 자도으로 스캔할 수 있도록 지원
일반적으로 객체를 생성하고 빈에 주입을 일일이 하는 것은 상당히 번거롭다.
=> 자동으로 빈 등록을 사용해도 OCP, DIP를 지킬 수 있다.

****그러면 수동 빈 등록은 언제 사용하면 좋을까?****
애플리케이션은 크게 업무 로직과 기술 지원 로직으로 나눈다.
- 업무로직 빈: 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리등이 모두 업무 로직이다. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
- 기술 지원 빈: 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.

- 업무 로직은 자동 기능을 적극 사용
- 기술 지원 로직은 수동 빈 등록

애플리케이션에 광범위하게 영향을 미치는 기술 지원 객체는 수동 빈으로 등록해서 딱 설정 정보에 바로 나타나게 하는 것이 유지보수 하기 좋다.

# 빈 생명주기 콜백
## 빈 생명주기 콜백 시작
데이터베이스 커넥셔 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 종료하는 작업을 진행하려면, 객체의 초기화와 종료 작업이 필요하다.

NetworkClient 객체 생성
```java
package hello.core.lifecycle;  
    
import org.springframework.beans.factory.DisposableBean;  
import org.springframework.beans.factory.InitializingBean;  
  
public class NetworkClient {  
  
    private String url;  
  
    public NetworkClient() {  
        System.out.println("생성자 호출, url = " + url);  
    }  
    public void setUrl(String url) {  
        this.url = url;  
    }  
    //서비스 시작시 호출  
    public void connect() {  
        System.out.println("connect: " + url);  
    }  
    public void call(String message) {  
        System.out.println("call: " + url + " message = " + message);  
    }  
    //서비스 종료시 호출  
    public void disconnect() {  
        System.out.println("close " + url);  
    }  
}
```
스프링 환경설정과 실행
```java
package hello.core.lifecycle;  
  
import org.junit.jupiter.api.Test;  
import org.springframework.context.ConfigurableApplicationContext;  
import org.springframework.context.annotation.AnnotationConfigApplicationContext;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
public class BeanLifeCycleTest {  
  
    @Test  
    public void lifeCycleTest() {  
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);  
        NetworkClient client = ac.getBean(NetworkClient.class);  
        ac.close();  
    }  
    @Configuration  
    static class LifeCycleConfig {  
   
        @Bean  
        public NetworkClient networkClient() {  
            NetworkClient networkClient = new NetworkClient();  
            networkClient.setUrl("http://hello-spring.dev");  
            return networkClient;  
        }    
    }
}
```

실행해보면 다음과 같은 이상한 결과가 나온다.

```

생성자 호출, url = null

connect: null

call: null message = 초기화 연결 메시지

```

생성자 부분을 보면 url 정보 없이 connect가 호출되는 것을 확인할 수 있다.
너무 당연한 이야기이지만 객체를 생성하는 단계에는 url이 없고, 객체를 생성한 다음에 외부에서 수정자 주입을 통해서 `setUrl()` 이 호출되어야 url이 존재하게 된다.

스프링 빈은 간단하게 다음과 같은 라이프사이클을 가진다.

****객체** **생성**** ****의존관계** **주입****

스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료된다. 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출해야 한다. 그런데 개발자가 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?

****스프링은** **의존관계** **주입이** **완료되면** **스프링** **빈에게** **콜백** **메서드를** **통해서** **초기화** **시점을** **알려주는** **다양한** **기능을** **제공****한다. 또한 ****스프링은** **스프링** **컨테이너가** **종료되기** **직전에** **소멸** **콜백****을 준다. 따라서 안전하게 종료 작업을 진행할 수 있다.

****스프링** **빈의** **이벤트** **라이프사이클****

****스프링** **컨테이너** **생성**** => ****스프링** **빈** **생성**** => ****의존관계** **주입**** => ****초기화** **콜백****  => ****사용**** => ****소멸전** **콜백**** => ****스프링** **종료****
- ****초기화** **콜백****: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
- ****소멸전** **콜백****: 빈이 소멸되기 직전에 호출

스프링은 다양한 방식으로 생명주기 콜백을 지원한다.

****참고**: **객체의** **생성과** **초기화를** **분리하자**.**
생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다. 반면에 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는등 무거운 동작을 수행한다.
따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는 객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이 유지보수 관점에서 좋다. 물론 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우

스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원한다.
- 인터페이스
- 설정 정보에 초기화 메서드, 종료 메서드 지정
- @PostConstruct, @PreDestory 애노테이션 지원

## 인터페이스 InitializingBean, DisposableBean
```java
  
public class NetworkClient implements InitializingBean, DisposableBean {

	@Override
	public void afterPropertieSet() throws Exception {
		connect();
		call("초기화 연결 메시지");
	}

	@Override
	public void destroy() throws Exception {
		disConnect();
	}
}
```

****출력** **결과****

```

생성자 호출, url = null

NetworkClient.afterPropertiesSet

connect: http://hello-spring.dev

call: http://hello-spring.dev message = 초기화 연결 메시지

13:24:49.043 [main] DEBUG

org.springframework.context.annotation.AnnotationConfigApplicationContext -

Closing NetworkClient.destroy

close + http://hello-spring.dev
```


출력 결과를 보면 초기화 메서드가 주입 완료 후에 적절하게 호출 된 것을 확인할 수 있다.
그리고 스프링 컨테이너의 종료가 호출되자 소멸 메서드가 호출 된 것도 확인할 수 있다.

초기화, 소멸 인터페이스 단점
- 이 인터페이스는 스프링 전용 인터페이스다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
- 초기화, 소멸 메서드의 이름을 변경할 수 없다.
- 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.

## 빈 등록 초기화, 소멸 메서드 지정
@Bean(initMethod = "init", destroyMethod = "close")

```java
  
public class NetworkClient{


	public void init() {
		connect();
		call("초기화 연결 메시지");
	}

	public void close() {
		disConnect();
	}
}
```

```java
   @Configuration  
    static class LifeCycleConfig {  
  
        @Bean(initMethod = "init", destroyMethod = "close")  
        public NetworkClient networkClient() {  
            NetworkClient networkClient = new NetworkClient();  
            networkClient.setUrl("http://hello-spring.dev");  
            return networkClient;  
        }    }
```
- 메서드 이름을 자유롭게 줄 수 있다.
- 스프링 빈이 스프링 코드에 의존하지 않는다.
- 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.

## 애노테이션 @PostConstruct, @PreDestory

```java
  
public class NetworkClient{

	@PostConstruct
	public void init() {
		connect();
		call("초기화 연결 메시지");
	}

	@PreDestory
	public void close() {
		disConnect();
	}
}
```

```java
   @Configuration  
    static class LifeCycleConfig {  
  
        @Bean
        public NetworkClient networkClient() {  
            NetworkClient networkClient = new NetworkClient();  
            networkClient.setUrl("http://hello-spring.dev");  
            return networkClient;  
        }    }
```
`@PostConstruct`, `@PreDestory` 이 두 애노테이션을 사용하면 가장 편리하게 초기화와 종료를 실행할 수 있다.

**@PostConstruct, @PreDestroy **애노테이션** **특징****
- 최신 스프링에서 가장 권장하는 방법이다.
- 애노테이션 하나만 붙이면 되므로 매우 편리하다.
- 패키지를 잘 보면 `javax.annotation.PostConstruct` 이다. 스프링에 종속적인 기술이 아니라 JSR-250라는 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
- 컴포넌트 스캔과 잘 어울린다.
- 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기화, 종료 해야 하면@Bean의 기능을 사용하자.

정리
- @PostConstruct, @PreDestroy 애노테이션을 사용하자
- 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean의 initMethod, destroyMethod를 사용하자.

# 빈 스코프

## 빈 스코프란?
스프링 빈이 기본적으로 싱글톤 스코프로 생성된다. 스코프는 빈이 존재할 수 있는 범위를 의미한다.

***스프링은 다음과 같은 스코프를 지원한다***
- 싱글톤 : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다. (default)
- 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프이다.
- 웹 관련 스코프
    - **request** : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프이다.
    - **session** : 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프이다.
    - **application** : 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프이다.
