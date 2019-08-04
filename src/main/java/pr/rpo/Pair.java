package pr.rpo;

public class Pair<F,S> {
    F first;
    S second;

    private Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }

    public static <T,K> Pair from(T f, K s) {
        return new Pair<T, K>(f,s);
    }

    public F first() {
        return first;
    }

    public S senond() {
        return second;
    }

}
