/**
 * Created by Dani on 07/03/2017.
 */
@SuppressWarnings("CanBeFinal")
abstract class MemManager {

  private boolean _changed;
  int _largestSpace;
  char[] _memory;
  private static final Object mutex = new Object();
  int indexOfLargest;

  MemManager(int s) {
    this._memory = new char[s];
    for (int i = 0; i < _memory.length; i++) {
        _memory[i] = '.';
    }
    //s is the number of empty cell that can be fill in the memory and when the
    // memory is empty its obvious that there are n number of cells as the largestSpace
    this._largestSpace = s;

    this._changed = true;
  }

  public boolean isChanged() {
    return _changed;
  }

  protected abstract int findSpace(int s);

  int countFreeSpacesAt(int pos) {
    int freeSpace = 0;
    for (int i = pos; i < _memory.length; i++) {
      if (_memory[i] == '.') {
        freeSpace++;
      } else {
        break;
      }
    }
    return freeSpace;
  }

  public void allocate(Process p) throws InterruptedException {
    synchronized (mutex) {
      while (p.getAddress() == -1) {
        if (_largestSpace >= p.getSize()) {
          int address = p.memManager.findSpace(p.getSize());
          for (int i = address; i < p.getSize() + address; i++) {
            _memory[i] = p.getID();
          }
          p.setAddress(address);
        } else {
          mutex.wait();
        }
      }
    }
    setLargestSpace();
    _changed = true;
  }

  private void setLargestSpace() {
    _largestSpace = 0;
    int spaceCounter = 0;
    for (int i = 0; i < _memory.length; i++) {
      if (_memory[i] == '.') {
        spaceCounter++;
        if (spaceCounter > _largestSpace) {
          _largestSpace = spaceCounter;
          indexOfLargest = i - spaceCounter + 1;
        }
      } else {
        spaceCounter = 0;
      }
    }
  }

  public void free(Process p) {
    synchronized (mutex) {
      for (int i = 0; i < _memory.length; i++) {
        if (_memory[i] == p.getID()) {
          _memory[i] = '.';
        }
      }
      p.setAddress(-1);
      setLargestSpace();
      _changed = true;
      mutex.notifyAll();
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < _memory.length; i++) {
      if (i == 0) {
        stringBuilder.append("  0|").append(_memory[i]);
      } else if (i % 20 == 0) {
        if (i < 100) {
          stringBuilder.append("|\n ").append(i).append("|").append(_memory[i]);
        } else {
          stringBuilder.append("|\n").append(i).append("|").append(_memory[i]);
        }
      } else {
        stringBuilder.append(_memory[i]);
        if (i + 1 == _memory.length) {
          stringBuilder.append("|");
        }
      }
    }
    if (_largestSpace < 100) {
      stringBuilder.append("\nls: ").append(_largestSpace);
    } else if (_largestSpace < 10) {
      stringBuilder.append("\nls:  ").append(_largestSpace);
    } else {
      stringBuilder.append("\nls:").append(_largestSpace);
    }
    _changed = false;
    return String.valueOf(stringBuilder);
  }
}