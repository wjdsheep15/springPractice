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


```java
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
private DiscountPolicy rateDiscountPolicy
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
	this.discountPolicy = discountPolicy:
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