package mutiny;

import io.smallrye.mutiny.Uni;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.time.Duration;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class predef {
    public static <T> T error(String msg){
        throw new RuntimeException(msg);
    }

    public static <T> Uni<T> pure(T x) {
        return Uni.createFrom().item(x);
    }

    public static <T> void eventually(Uni<T> x, Matcher<? super T> p){
        x.onItem().invoke(v -> MatcherAssert.assertThat(v, p))
                .await().atMost(Duration.ofSeconds(3));
    }

    public static <T> void eventually(Uni<T> x, Predicate<T> p){
        x.onItem().invoke(v -> assertTrue(p.test(v)))
                .await().atMost(Duration.ofSeconds(3));
    }
}
