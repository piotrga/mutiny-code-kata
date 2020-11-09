package mutiny;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static mutiny.predef.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UniKata {

    // Read THIS before you start this Kata: https://smallrye.io/smallrye-mutiny/

    public static final String SUCCESFULL_RESULT = "...";

    @Test
    public void supplier(){
        Supplier<Integer> f = null;

        assertThat(f.get(), is(42));
    }

    @Test
    public void function(){
        Function<Integer, Integer> square = null;

        assertThat(square.apply(4), is(16));
    }

    @Test
    public void biFunction(){
        BiFunction<Integer, Integer, Integer> add = null;

        assertThat(add.apply(2, 3), is(5));
    }

    @Test
    public void consumer(){
        Integer received = null;
        Consumer<Integer> callback = null;

        callback.accept(2);

        assertThat(received, is(2));
    }

    @Test
    public void constant_can_be_lifted_to_uni(){
        Uni<Integer> res = null;

        eventually(res, is(3));
    }

    @Test
    public void unis_can_be_combined_to_tuple(){
        Uni<Integer> x = pure(3);
        Uni<String> y = pure("4");

        Uni<Tuple2<Integer, String>> xy = null;

        eventually(xy, is(Tuple2.of(3, "4")));
    }

    @Test
    public void can_run_one_uni_multiple_times_and_get_different_results(){
        Uni<Integer> random = null;

        Uni<Tuple2<Integer, Integer>> xy = Uni.combine().all().unis(random, random).asTuple();

        eventually(xy, x -> x.getItem1() != x.getItem2() );
    }

    @Test
    public void uni_can_recover_on_failure(){
        Uni<Integer> random = Uni.createFrom().item(() -> error("Boom!"));
        final int FALLBACK = -1;

        eventually( random, is(-1) );
    }

    @Test
    public void uni_can_retry(){
        AtomicInteger attempts = new AtomicInteger(0);
        Uni<String> http_get = Uni.createFrom().item(() -> (attempts.incrementAndGet() < 3) ? error("Boom!") : SUCCESFULL_RESULT);

        eventually( http_get, is(SUCCESFULL_RESULT));
    }

    @Test public void uni_value_can_be_mapped(){
        Double RADIUS = 10d;
        Uni<Double> pi = computePiOnGPU();
        Uni<Double> area = null;

        eventually(area, is(3141.5));
    }

    @Test public void uni_can_be_chained_with_uni(){
        Uni<Double> RADIUS = parse("10");
        Uni<Double> pi = computePiOnGPU();
        Uni<Double> area = null;

        eventually(area, is(3141.5));
    }

    private Uni<Double> parse(String value) {
        return Uni.createFrom().item( () -> Double.parseDouble(value));
    }

    private Uni<Double> computePiOnGPU() {
        return pure(3.1415);
    }



    @Test public void uni_can_be_chained_with_uni_ignoring_the_output(){}

}

