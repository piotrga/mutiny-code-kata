package mutiny;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import static mutiny.predef.*;
import static org.hamcrest.Matchers.is;

public class UniKata {

    public static final String SUCCESFULL_RESULT = "...";

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

    @Test public void uni_value_can_be_mapped(){ }

    @Test public void uni_can_be_chained_with_uni(){ }

    @Test public void uni_can_be_chained_with_uni_ignoring_the_output(){ }


}

