import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        Semaphore employees = new Semaphore(10);
        CyclicBarrier waitingCalls = new CyclicBarrier(10, new WaitingCallCenter());
        List<String> calls = Arrays.asList("Marlon", "Javier", "Ana", "Andrea", "Fernando", "Homero", "Bernardo", "Lucia", "Julia", "Craig");
        Dispatcher dispatcher = new Dispatcher(employees,waitingCalls,"");
        dispatcher.dispatchCall(calls);
        System.out.println("Available operators initially = " + employees.availablePermits() );
    }
}