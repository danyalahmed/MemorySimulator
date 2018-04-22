import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dani on 07/03/2017.
 */
public class SimController
        implements Runnable {

  private MemManager memManager;
  private ThreadPoolExecutor executorService;

  private SimController() {

  }

  @Override
  public void run() {
    while (!executorService.isShutdown()) {
      try {
        Thread.sleep(1000);
        if (memManager.isChanged()) {
          System.out.println(memManager.toString());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String args[]) {
    if (args.length != 3) {
      System.err.println("Usage: you must supply three argument\n char | int | String" +
              "\neg.java SimController f 500 pdata.txt" +
              "\nwhere f is Policy (First Fit, Best Fit or Worst Fit), 500 number of empty spaces in memory and " +
              "finally the file name containing commands, or operation memory requirements" +
              "\n(from commandline and the same foler as classess)");
      System.exit(1);
    }
    new SimController().setupAndStart(args);
  }

  private void setupAndStart(String args[]) {
    String opMode = args[0].toLowerCase();
    int memory = Integer.parseInt(args[1]);
    String fileName = args[2];

    switch (opMode.charAt(0)) {
      case 'f': {
        memManager = new FirstFitMemManager(memory);
        System.out.println("Policy: FIRST fit");
        break;
      }
      case 'b': {
        memManager = new BestFitMemManager(memory);
        System.out.println("Policy: BEST fit");
        break;
      }
      case 'w': {
        memManager = new WorstFitMemManager(memory);
        System.out.println("Policy: WORST fit");
        break;
      }
    }
    executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    executorService.execute(this);
    executorService.execute(new QueueHandler(executorService, memManager, fileName));
    // we check the active count > 1 because apart from our processes there is a
    // watcher thread running so when all our processes finish, we are left with only
    // one thread.
    while (executorService.getActiveCount() > 1) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      executorService.shutdown();
      executorService.awaitTermination(5L, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      //noinspection ThrowablePrintedToSystemOut
      System.err.println(e);
    }
    System.out.println("All threads have terminated");
  }

}
