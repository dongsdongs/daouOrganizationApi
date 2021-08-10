## 다우기술 기술과제 : 조직도 API 개발 / 이동건 
조직도 API for README.md
> 조직도 API는 JPA, JAVA, Spring Framework를 통해 개발되었으며 추가 된 Dependencies는
 * Spring MVC를 사용해 Build Web, RESTful, application을 위한 Spring Web
 * Java annotation library인 Lombok
 * Java Persistence API를 사용하기 위한 Spring Data JPA
 * fast in-memory database인 H2 Database
>다우기술 경력직 채용 기술과제를 제출하기 위한 목적으로 제작되었습니다.

**Requirements:**
 * GET 조직도 조회
 * POST 부서/부서원 추가
 * PUT 부서/부서원 수정
 * DELETE 부서/부서원 삭제

## Table of Contents
_Note: 목차에 대한 안내일 뿐이며, API 사양 문서에 대한 용어를 정의하거지 않습니다._
- [서버구동방법](#서버구동방법)
- [테이블구조](#테이블구조)
- [MemberApiController](#MemberApiController)
- [DeptApiController](#DeptApiController)
- [ErrorCode처리](#ErrorCode처리)
- [JUnit처리](#JUnit처리)
- [완료못한부분이유및보완할점](#완료못한부분이유및보완할점)

## 서버구동방법
* 최초 H2 Database를 https://h2database.com/html/main.html 경로를 통해 PC OS에 맞는 설치파일을 다운로드 후 해당 API 프로젝트 내부에 설치
* 설치 된 H2 Directory > bin > h2.bat을 명령어 창을 통해 실행
* H2콘솔이 실행되면 주소창의 ip값을 지우고 '최초' localhost로 변경 (*뒤의 키값은 유지)
* JDBC URL :  jdbc:h2:~/daouCodeApi (최초 한 번, 세션키 유지한 상태로 실행)
* ~/daouCodeApi.mv.db 파일 생성 확인 및 연결
* 이후 H2접속 시 JDBC URL 입력창에 jdbc:h2:tcp://localhost/~/daouCodeApi 연결
* daouCodeApi > src > main > java > daouCodeApiWork.daouCodeApi > api > MemberApiController.class 내부에서 Shift + F10 혹은 상위메뉴 Run 선택

> Note: 최초 API 실행 시 @component 어노테이션에 의해 initDB가 실행되어 API 실행을 위한 기초 데이터 INSERT

  ```markdown
@Component
@RequiredArgsConstructor
public class initDB {
...
  ```
  부서 및 부서원에 대한 기본정보를 담고 있으며 부서/부서원 Entity를 직접 사용하지 않고 Create DTO를 통해 데이터 이동 및 EntityManager를 통해 데이터 입력
```
@Data
    static class CreateMemberRequest {
    ...
@Data
    static class CreateDeptRequest {    
    ...
```
- Member, Dept의 내용이 변경되면 API 스펙이 변경되기 때문에 별도의 DTO를 사용
- resposne DTO를 Return Type으로 만들어 API 통신 이후 DTO에 저장해놓은 데이터 확인 가능

## 테이블구조
- 부서: Dept
- 부서원: Member

**DEPT:**
|내용|COLUMN|PK/FK|Type|
|:---|:---:|:---:|:---:|
|부서의 ID|dept_id|PK|String|
|상위 부서의 ID|pId||String|
|부서 LEVEL|level||int|
|부서명|name||String|
|부서타입|type||String|
|Member|children|FK|Member|


**MEMBER:**
|내용|COLUMN|PK/FK|Type|
|:---|:---:|:---:|:---:|
|부서원의 ID|member_id|PK|int|
|부서원 이름|name||String|
|부서원 타입|type||String|
|매니저 여부|manager||boolean|
|Dept|dept|FK|Dept|

## MemberApiController

1.**조회 <GET> http://{SERVER_URL}/api/organizations**
- MemberService의 findMembers를 통해 부서원 조회 후 List형태로 반환 후 Stream으로 분석 후 Collect를 통해 DTO에 Generic형태로 담아 결과 조회
  
### parameter
|param|type|필수여부|DESC|
|:---|:---:|:---:|:---:|
|deptCode|String|N|부서코드, 미입력시 최상위 노드를 기준으로 조회|
|deptOnly|boolean|N|부서 정보만 응답 여부|
|searchType|String|N|dept:부서, member: 부서원|
|searchKeyword|String|N|검색어|

### response
|속성|type|DESC|
|:---|:---:|:---:|
|id|Integer|데이터 고유 ID|
|type|String|Company:회사, Division: 본부, Department:부/팀, Memeber:부서원|
|code|String|부서코드|
|manager|boolean|type이 "MEMBER"인 경우 팀장여부 true/false|
 
 
2.**등록 <POST> http://{SERVER_URL}/org/member**
- 요청 값으로 Member 엔티티를 직접 받지않고 CreateMemberRequest DTO를 통해서 데이터를 이동
 Member 엔티티를 직접 받게되면 프리젠테이션 계층을 위한 로직이 추가가 되며
 엔티티에 @NotEmpty같은 직접적인 로직이 들어간다.
 엔티티가 변경되면 API 스펙이 변경되기 때문에 DTO를 통해서 데이터를 이동한다.

3.**수정 <PUT> http://{SERVER_URL}/org/member/{memberId}**
- update를 위한 response DTO, request DTO를 별도로 만들어서 사용
 등록과 같은 DTO를 사용해도 무방할것 같으나 등록과 수정의 API 스펙이 다르기 때문에 별도의 update DTO를 만들어 사용
- @PathVariable("memberId")를 사용
- MemberService의 findOne의 파라미터로 id값을 전달
- MemberRepository의 EntityManager로 데이터 처리

4.**삭제 <DELETE> http://{SERVER_URL}/org/member/{memberId}**
- @PathVariable("memberId")를 사용
- MemberService의 findOne의 파라미터로 id값을 전달
- MemberRepository의 EntityManager로 데이터 처리

## DeptApiController

1.**등록 <POST> http://{SERVER_URL}/org/dept**
 - Member 등록과 같은 방식으로 엔티티를 직접 받지않고 CreateDeptRequest DTO를 통해서 데이터를 이동

2.**수정 <PUT> http://{SERVER_URL}/org/dept/{deptId}**
- update를 위한 response DTO, request DTO를 별도로 만들어서 사용
- @PathVariable("deptId")를 사용
- DeptService의 findOne의 파라미터로 id값을 전달
- DeptRepository의 EntityManager로 데이터 처리

3.**삭제 <DELETE> http://{SERVER_URL}/org/dept/{deptId}**
- @PathVariable("memberId")를 사용
- DeptService의 findOne의 파라미터로 id값을 전달
- DeptRepository의 EntityManager로 데이터 처리
 
## ErrorCode처리

|속성|type|필수여부|DESC|
|:---|:---:|:---:|:---:|
|code|String|Y|오류 코드|
|message|String|Y|오류 메시지|
  
|HTTP 상태 코드|오류 코드|DESC|
|:---|:---:|:---:|
|400|BAD_REQUEST|요청값이 적절하지 않음|
|500|INTERNAL_SERVER_ERROR|내부 서버 오류|

**Description:**
- exception PACKAGE를 통해 ErrorException.class, ErrorCodes enum을 통해 애러코드를 관리

## JUnit처리
**MemeberApiTest
- JUnit4 사용
- Java에서 독립된 단위테스트를 지원해주는 프레임워크
- 소스코드의 특정 모듈, 함수에 대한 Testcase
- assesrt메서드를 통해 테스트의 수행결과를 도출  
1. 맴버가입
  - MemberService의 join함수를 통해 반환받은 Id값과 Member를 생성해 영속성을 유지하는 setId를 통해 전달한 ID값을  assertEqual테스트
2. 중복맴버예외처리
  - Member를 memeber1, memeber2를 통해 두 번 생성하여 setId를 통해 동일한(중복되는 부서원ID를 SET) MemeberService의 join을 통해 fail TEST

## 완료못한부분이유및보완할점
> 완료하지 못한 이유 
 - Controller > SERVICE > REPOSITORY 방식으로 진행되는 방식에 대한 부분은 기존의 개발해왔던 방식과 비슷했으나
 - JPA에 객체를 전달하여 반족적인 일을 처리해 주는 방식에 대하여 익숙하지 않았던 점
 - 테이블간 관계를 이어주는 1:N, N:N, 1:1등 방식의 이해가 부족하지 않았던 점
 - 객체와 데이터베이스를 맵핑하는 형태의 이해 부족
 - JPA의 다양한 어노테이션의 사용에 대한 경험 부족
> 보완할 점
 - 지속적인 JPA에 대한 학습이 필요하다고 생각하며 JPA의 수많은 장점(생산성, 유지보수 등)들을 보유하고 있기다고 생각
 - 1차 캐시와 동일성이 유지됨에 따른 조회 기능과 그에 따른 JPA의 핵심이라고 할 수 있는 영속성 컨텍스트에 대한 깊은 이해가 필요
 - 객체와 관계형 데이터베이스의 맵핑
 - 지연로딩과 즉시로딩의 관계에 대한 이해가 
 
  ** MemberApiController 의 Parameter를 통한 조회 시 부족했던 점
  - JPA Query의 표준인 Criteria에 대한 애러발생 MemberSearch를 통해 전달 된 parameter의 여부에 따른 검색조건을 사용하지 못한 부분
  
  
