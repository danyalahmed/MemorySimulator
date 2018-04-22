/**
 * Created by Dani on 09/03/2017.
 */
public class FirstFitMemManager extends MemManager {

  public FirstFitMemManager(int s) {
    super(s);
  }

  @Override
  protected int findSpace(int s) {
    int indexOfMin = -1;
    //memory is empty
    if (_largestSpace == _memory.length) {
      return 0;
    }
    for (int i = 0; i < _memory.length; i++) {
      if (_memory[i] == '.') {
        //empty memory from position i
        int emptyCells = countFreeSpacesAt(i);
        //in case we have enough space from position i
        if (emptyCells >= s) {
          return i;
        }
        //now that we know from i to (i + empty-cell -1) are all empty and we already count them
        // there is no need to count from each of them
        i = i + emptyCells - 1;
      }
    }
    return indexOfMin;
  }
}
