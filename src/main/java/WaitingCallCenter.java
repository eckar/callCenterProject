public class WaitingCallCenter implements Runnable {

    @Override
    public void run() {
        System.out.println("Please wait, in a few seconds one of our operator will attend your call...");
    }
}