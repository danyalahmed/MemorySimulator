/**
 * Created by Dani on 09/03/2017.
 */
public class WorstFitMemManager extends MemManager {

  public WorstFitMemManager(int s) {
    super(s);
  }

  @Override
  protected int findSpace(int s) {
    //memory is empty
    if (_largestSpace == _memory.length) {
      return 0;
    }
    //as we save the index of largest memory available we even don't have to loop through the memory here WHY?
    //because we already know the address of a worst space where our progress can fit.
    return indexOfLargest;
  }
}
