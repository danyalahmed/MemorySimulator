import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Dani on 09/03/2017.
 */
@SuppressWarnings("CanBeFinal")
class QueueHandler
        implements Runnable {
  @Override
  public void run() {
    setProcess();
  }

  private ThreadPoolExecutor threadPoolExecutor;
  private MemManager memManager;
  private String file;

  public QueueHandler(ThreadPoolExecutor threadPoolExecutor, MemManager memManager, String file) {
    this.threadPoolExecutor = threadPoolExecutor;
    this.memManager = memManager;
    this.file = file;
  }

  private void setProcess() {
    Path file_path = Paths.get(this.file);
    System.out.println(file_path);
    try (Scanner file = new Scanner(file_path)) {
      int delay, size, rt;
      char pid;

      while (file.hasNextLine()) {
        Scanner line = new Scanner(file.nextLine());
        line.useDelimiter(":");
        delay = line.nextInt();
        pid = line.next().charAt(0);
        size = line.nextInt();
        rt = line.nextInt();
        line.close();

//        System.out.printf("delay: %s, ID: %c, size: %s, runtime: %s\n",
//                delay, pid, size, rt);

        Thread.sleep(delay * 100);
        Process process = new Process(pid, size, rt, memManager);
        threadPoolExecutor.execute(process);

      }
      file.close();
    } catch (NoSuchFileException e) {
      System.err.println("File not found: " + this.file);
      System.exit(1);
    } catch (IOException e) {
      //noinspection ThrowablePrintedToSystemOut
      System.err.println(e);
      System.exit(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
