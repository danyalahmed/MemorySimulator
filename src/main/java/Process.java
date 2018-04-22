/**
 * Created by Dani on 07/03/2017.
 */
@SuppressWarnings("CanBeFinal")
class Process
        implements Runnable {

  private char ID;
  private int size;
  private int runTime;
  private int address;
  MemManager memManager;

  @Override
  public void run() {
    System.out.println(this + " waiting to run.");
    try {
      memManager.allocate(this);
      System.out.println(this + " running.");
      Thread.sleep(100 * this.runTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    memManager.free(this);
    System.out.println(this + " has finished.");
  }

  Process(char ID, int size, int runTime, MemManager memManager) {
    this.ID = ID;
    this.size = size;
    this.runTime = runTime;
    this.memManager = memManager;
    this.address = -1;
  }

  char getID() {
    return ID;
  }

  int getSize() {
    return size;
  }

  int getAddress() {
    return address;
  }

  void setAddress(int address) {
    this.address = address;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.getID()).append(":");
    //address three padded
    if (this.address < 0) {
      stringBuilder.append("  U");
    } else if (this.address < 10) {
      stringBuilder.append("  ").append(this.address);
    } else if (this.address < 100) {
      stringBuilder.append(" ").append(this.address);
    } else {
      stringBuilder.append(this.address);
    }
    stringBuilder.append("+");
    //size two padded
    if (this.size < 10) {
      stringBuilder.append(" ").append(this.size);
    } else if (this.size < 100) {
      stringBuilder.append(this.size);
    }
    return String.valueOf(stringBuilder);
  }

}
