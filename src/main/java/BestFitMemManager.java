/**
 * Created by Dani on 07/03/2017.
 */
public class BestFitMemManager extends MemManager {

  public BestFitMemManager(int s) {
    super(s);
  }

  @Override
  protected int findSpace(int s) {
    int indexOfMin = -1;
    int minDiff = _largestSpace - s;
    //memory is empty
    if (_largestSpace == _memory.length) {
      return 0;
    }
    //if the largest space is just as much as the required then this is best place to put our process.
    if (_largestSpace == s) {
      return indexOfLargest;
    }
    for (int i = 0; i < _memory.length; i++) {
      if (_memory[i] == '.') {
        //empty memory from position i
        int emptyCells = countFreeSpacesAt(i);

        //in case we have enough space from position i
        if (emptyCells == s) {
          return i;
        }
        if (emptyCells > s && (emptyCells - s) <= minDiff) {
          indexOfMin = i;
          minDiff = emptyCells - s;
        }
        //now that we know from i to (i + empty-cell -1) are all empty and we already count them
        // there is no need to count from each of them
        i = i + emptyCells - 1;
      }
    }
    return indexOfMin;
  }
}
