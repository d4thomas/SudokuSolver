package problems;

import java.util.List;
import java.util.Map;

/**
 *
 * @param <X> the data type of variables.
 *            * (e.g., for Sudoku, we use Square such as (4, 7) to name
 *            squares of the 9x9 board, where 4 is the row number and
 *            7 is the column number)
 * @param <V> the data type of variable values.
 *            * (e.g., for Sudoku, a value is an integer between 1 and 9)
 */
public interface CSPProblem<X, V> {

    Map<X, List<V>> getAllVariables();

    /**
     * Given a variable, return its neighbors.
     *
     * @param var variable
     * @return list of neighbors
     */
    List<X> getNeighborsOf(X var);

    /**
     * @return a list of variables that have been pre-assigned
     *         values initially.
     */
    List<X> getPreAssignedVariables();

    /**
     * Print the puzzle based on the passed map of all variables.
     */
    void printPuzzle(Map<X, List<V>> allVariables);

}