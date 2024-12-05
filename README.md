# Hilt를 사용한 의존성 관리

이 프로젝트는 Hilt를 학습하기 위해 클린 아키텍처 + MVVM 으로 구현된 프로젝트 입니다.

1. Domain 계층 (Model, Repository Interface, UseCases)
2. Data 계층 (Room Database, Entity, DAO, Repository Implementation)
3. Presentation 계층 (Screens, ViewModels)
4. DI 설정 (Application, AppModule)

##### 사용된 라이브러리 버전

- hilt = "2.48"
- hiltNavigationCompose = "1.1.0"
- room = "2.6.1"
- ksp = "1.9.0-1.0.13"

## 1. 기본 설정

```
// 프로젝트 레벨의 Hilt 설정
// MemoApplication.kt
@HiltAndroidApp
class MemoApplication : Application()

// Activity에서의 Hilt 설정
// MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

- `@HiltAndroidApp` : Hilt를 사용하는 앱의 시작점을 지정
- `@AndroidEntryPoint` : Hilt가 의존성을 주입할 Android 컴포넌트를 표시

## 2. 의존성 제공

```
//AppModule.kt

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMemoDatabase(app: Application): MemoDatabase {
        return Room.databaseBuilder(/*...*/).build()
    }

    @Provides
    @Singleton
    fun provideMemoRepository(db: MemoDatabase): MemoRepository {
        return MemoRepositoryImpl(db.memoDao())
    }
}
```

- `@Module` : Hilt에게 의존성을 제공하는 방법을 알려주는 클래스
- `@InstallIn` : 해당 모듈을 어떤 컴포넌트에 설치할지 지정
- `@Provides` : 의존성을 제공하는 메서드임을 표시
- `@Singleton` : 앱 전체에서 단일 인스턴스로 관리

#### Hilt 컴포넌트와 스코프

Hilt는 Android 클래스의 수명 주기에 맞춘 다양한 컴포넌트를 제공한다.

```
@InstallIn(SingletonComponent::class) // 앱 수명주기
@InstallIn(ActivityComponent::class)  // Activity 수명주기
@InstallIn(ViewModelComponent::class) // ViewModel 수명주기
@InstallIn(FragmentComponent::class)  // Fragment 수명주기
@InstallIn(ServiceComponent::class)   // Service 수명주기
@InstallIn(ViewComponent::class)      // View 수명주기
```

▼ 사용 예시

```
@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    @ActivityScoped  // Activity 수명주기 동안 단일 인스턴스 유지
    fun provideActivityHelper(): ActivityHelper {
        return ActivityHelperImpl()
    }
}
```

## 3. 생성자 주입

```
// MemoRepositoryImpl.kt
class MemoRepositoryImpl @Inject constructor(
    private val dao: MemoDao
)

// AddMemoUseCase.kt
class AddMemoUseCase @Inject constructor(
    private val repository: MemoRepository
)

// DeleteMemoUseCase.kt
class GetMemosUseCase @Inject constructor(
    private val repository: MemoRepository
)

// GetMemoUseCase.kt
class DeleteMemoUseCase @Inject constructor(
    private val repository: MemoRepository
)

```

- `@Inject constructor` : Hilt에게 이 생성자를 사용하여 의존성을 주입하라고 지시

## 4. ViewModel에서의 Hilt 사용

```
// MemoListViewModel.kt
@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val getMemosUseCase: GetMemosUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
) : ViewModel()


// AddMemoViewModel.kt
@HiltViewModel
class AddMemoViewModel @Inject constructor(
    private val addMemoUseCase: AddMemoUseCase
) : ViewModel()
```

생성자 주입을 통해 UseCase들을 주입받는다. Hilt가 이 생성자를 사용해서 ViewModel 인스턴스를 생성한다.

- `@HiltViewModel` : Hilt가 관리하는 ViewModel임을 표시
- compose에서는 `hiltViewModel()`을 통해서 ViewModel 인스턴스를 받아온다.

#### Hilt의 이점

1. 보일러플레이트 코드 감소
   - DI 설정을 위한 반복적인 코드 작성 불필요
   - 어노테이션 기반으로 간단하게 의존성 주입 구현

▼ 예시

```
// Hilt 사용 전
class Repository(
    private val database: Database,
    private val api: Api,
    private val cache: Cache
)
// 직접 의존성 생성 및 주입 필요

// Hilt 사용 후
class Repository @Inject constructor(
    private val database: Database,
    private val api: Api,
    private val cache: Cache
)
// 자동으로 의존성 주입
```

2. 컴파일 타임 의존성 검증

   - 의존성 문제를 앱 실행전에 발견 가능
   - 순환 참조나 누락된 의존성을 컴파일 시점에 확인
   - 명확한 에러 메시지 제공

3. Android 생명주기 통합

   - Android 컴포넌트의 생명주기와 자동 동기화
   - 메모리 누수 방지
   - 적절한 시점에 의존성 생성 및 제거

4. 테스트 용이성
   - 의존성을 쉽게 교체할 수 있는 구조
   - 테스트용 모듈을 별도로 제공 가능

▼ 예시

```
@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return FakeRepository()  // 테스트용 구현체
    }
}
```

5. 표준화된 DI 패턴
   - 프로젝트 전반에 걸쳐 일관된 DI 패턴 적용
   - 팀 협업 시 코드 이해도 향상

## 클린아키텍처와 의존성 체인

```
ViewModel -> UseCase -> Repository(interface) <- RepositoryImpl -> DAO -> Database
```

Hilt의 주요 목적은 Android 앱에서의 의존성 주입(Dependency Injection)을 더 쉽고 체계적으로 관리하는 것이다. Hilt를 사용하면 수동으로 의존성 체인을 구성하는 복잡한 작업 없이, 어노테이션 기반으로 쉽게 의존성 주입을 구현할 수 있다.

이렇게 구성하는 이유 :

1. 의존성 역전 원칙(DIP) 적용

   - Repository 인터페이스는 도메인 계층에 존재
   - Repositoryimpl은 데이터 계층에 존재
   - 도메인 계층은 구체적은 구현에 의존하지 않음

2. 테스트 용이성

   - Repository 인터페이스를 통해 Mock 객체 생성 가능
   - UseCase 테스트 시 실제 DB나 네트워크 없이 테스트 가능

3. 유연성 증가
   - 구현체(RepositoryImpl)를 쉽게 교체 가능
   - 다양한 데이터 소스 처리 가능 (예: API, 캐시, DB 등)

공식 문서
https://developer.android.com/training/dependency-injection/hilt-android?hl=ko
