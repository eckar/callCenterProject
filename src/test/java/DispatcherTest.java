import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import static org.junit.Assert.assertTrue;

public class DispatcherTest {

    Dispatcher dispatcher;
    Semaphore employees;
    CyclicBarrier waitingCalls;

    @Before
    public void setup(){
        employees = new Semaphore(10);
        waitingCalls = new CyclicBarrier(10, new WaitingCallCenter());
        dispatcher = new Dispatcher(employees,waitingCalls,"");
    }

    @Test
    public void shouldReceiveUntil10Calls() {
        List<String> callsList = Arrays.asList("Marlon", "Javier", "Ana", "Andrea", "Fernando", "Homero", "Bernardo", "Lucia", "Julia", "Craig");
        assertTrue(dispatcher.validateMaximumConcurrentCalls(callsList));
    }
}