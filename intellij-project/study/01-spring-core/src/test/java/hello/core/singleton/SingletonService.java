package hello.core.singleton;

public class SingletonService {

    // 자기 자신을 내부에 private static으로 가지고 있음. 이러면 class 레벨로 올라가기 때매 딱 하나만 가질 수 ㅇㅇ.
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
