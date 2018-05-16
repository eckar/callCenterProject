import javax.print.attribute.standard.MediaSize;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Dispatcher extends Thread{

    private static final String INIT_MSG = " you are in the queue of calls ";
    private static final String OPERATOR_ANSWER = " An operator have answered ";
    private static final String AVAILABLE_OPERATORS = " Available operators = ";
    private static final String CALL_ENDS = " The phone call ends.";
    private static final int MAX_CONCURRENT_CALLS = 10;
    public static final String ERROR_MESSAGE = "In this moments all our operators are busy, but soon we will be answering your call";

    private int minDurationCall = 5;
    private int maxDurationCall = 10;
    private long duration;

    private CyclicBarrier waitingCalls;
    private Semaphore employees;
    private Logger logger = Logger.getLogger("Logger");

    public Dispatcher(Semaphore employees, CyclicBarrier waitingCalls, String name){
        this.employees = employees;
        this.waitingCalls = waitingCalls;
        this.setName(name);
        this.start();
    }

    @Override
    public void run(){
        try {
            logger.info(getName() + INIT_MSG);
            waitingCalls.await();
            employees.acquire();

            logger.info(getName() + OPERATOR_ANSWER);
            Thread.sleep(TimeUnit.SECONDS.toMillis(durationOfCall()));

            logger.info(getName() + CALL_ENDS);
            employees.release();

            logger.info(AVAILABLE_OPERATORS + employees.availablePermits());
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println(e);
        }
    }

    public void dispatchCall(List<String> calls) {

        int operatorsQuantity = (int) (employees.availablePermits() * 6) / employees.availablePermits();
        int supervisorQuantity = (int) (employees.availablePermits() * 3)/ employees.availablePermits();
        int directorQuantity = (int) (employees.availablePermits() * 1)/ employees.availablePermits();

        if(validateMaximumConcurrentCalls(calls)) {
            for (int i = 0; i < operatorsQuantity; i++) {
                new Dispatcher(employees, waitingCalls, calls.get(i));
            }
            for (int i = 0; i < supervisorQuantity; i++) {
                new Dispatcher(employees, waitingCalls, calls.get(i));
            }
            for (int i = 0; i < directorQuantity; i++) {
                new Dispatcher(employees, waitingCalls, calls.get(i));
            }
        }
        else {
            logger.info(ERROR_MESSAGE);
        }
    }

    public boolean validateMaximumConcurrentCalls(List<String> calls){
        return calls.size()<= MAX_CONCURRENT_CALLS;
    }

    private long durationOfCall(){
        Random randomDuration = new Random();
        return duration = randomDuration.nextInt(maxDurationCall - minDurationCall ) + minDurationCall;
    }
}