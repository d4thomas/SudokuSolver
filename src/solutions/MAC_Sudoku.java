package solutions;

import problems.*;

import java.util.List;
import java.util.Map;

import algorithms.MAC;

public class MAC_Sudoku extends MAC<Square, Integer> {

    public MAC_Sudoku(Sudoku problem) {
        super(problem);
    }

    /**
     * Implements the `revise()` operation for arc consistency checking in Sudoku.
     * <p>
     * This method enforces consistency between two variables (`tail` and `head`)
     * connected by a constraint. It removes values from `tail`'s domain that are
     * inconsistent with all values in `head`'s domain.
     * <p>
     * In the context of Sudoku, two squares (i.e., variables) must take different
     * values. Therefore, a value `v` in the domain of `tail` is inconsistent if
     * `head`'s domain contains **only** the same value `v`.
     *
     * @param tail The variable whose domain may be reduced (tail of the arc).
     * @param head The variable used to check for consistency (head of the arc).
     * @return true if the domain of `tail` was revised (i.e., any values were
     *         removed), false otherwise.
     */
    public boolean revise(Square tail, Square head) {
        boolean revised = false;

        Map<Square, List<Integer>> domains = getAllVariables();
        List<Integer> tailDomain = domains.get(tail);
        List<Integer> headDomain = domains.get(head);
        Integer[] originalValues = tailDomain.toArray(new Integer[0]);

        for (Integer v : originalValues) {
            boolean hasSupport = false;
            for (Integer w : headDomain) {
                if (!v.equals(w)) {
                    hasSupport = true;
                    break;
                }
            }
            if (!hasSupport) {
                tailDomain.remove(v);
                revised = true;
            }
        }

        return revised;
    }

    /**
     * The main method serves as the entry point for the Sudoku solver application.
     *
     * @param args Command-line arguments supplied to the application. In this method,
     *             the arguments are unused.
     */
    public static void main(String[] args) {
        String filename = "./SudokuPuzzles/Puzzle10.txt";
        Sudoku problem = new Sudoku(filename);
        MAC_Sudoku agent = new MAC_Sudoku(problem);
        System.out.println("Loading puzzle from " + filename + "...");
        problem.printPuzzle(problem.getAllVariables());
        if (agent.initAC3() && agent.search()) {
            System.out.println("Solution found:");
            problem.printPuzzle(agent.getAllVariables());
        } else {
            System.out.println("Unable to find a solution.");
        }
    }
}