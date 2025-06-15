package problems;

import java.io.*;
import java.util.*;

public class Sudoku implements CSPProblem<Square, Integer> {

    private final Map<Square, List<Integer>> allVariables;

    // Map every square to the set of neighbors
    // "Neighbors" are those squares in the same row, column, or 3x3 box
    private final Map<Square, Set<Square>> neighbors = new HashMap<>();

    // Name of the file that contains the test case
    private final String filename;

    public Sudoku(String filename) {
        this.filename = filename;
        allVariables = getAllVariables();
        // For each row, get all the squares in that row
        for (int i = 0; i < 9; i++) {
            Set<Square> row = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                row.add(new Square(i, j));
            }
            addPairwiseNeighbors(row);
        }
        // For each column, get all squares in that column
        for (int j = 0; j < 9; j++) {
            Set<Square> column = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                column.add(new Square(i, j));
            }
            addPairwiseNeighbors(column);
        }
        // For each 3x3 box, get all squares in that box
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Set<Square> box = new HashSet<>();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        box.add(new Square(i * 3 + x, j * 3 + y));
                    }
                }
                addPairwiseNeighbors(box);
            }
        }
    }

    /**
     * Adds all pairwise neighbor relationships within a given set of squares.
     * <p>
     * For each square in the set, all other squares in the same set are added
     * to its list of neighbors. A square is not considered its own neighbor,
     * so self-pairs are excluded.
     * <p>
     * If a square does not already have an entry in the neighbor map, a new
     * set is created and added.
     *
     * @param group a set of squares that all share a constraint (for example,
     *               the same row, column, or box)
     */
    private void addPairwiseNeighbors(Set<Square> group) {
        for (Square s1 : group) {
            for (Square s2 : group) {
                if (!s1.equals(s2)) {
                    neighbors.computeIfAbsent(s1, _ -> new HashSet<>()).add(s2);
                }
            }
        }
    }

    public Map<Square, List<Integer>> getAllVariables() {
        Map<Square, List<Integer>> allVariables = new HashMap<>();
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
                String line;
                List<Integer> defaultDomain = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
                // i: row number, j: column number
                for (int i = 0; i < 9; i++) {
                    if ((line = in.readLine()) != null && !line.isEmpty()) {
                        String[] numbers = line.trim().split(" ");
                        for (int j = 0; j < 9; j++) {
                            Square vName = new Square(i, j);
                            int number = Integer.parseInt(numbers[j]);
                            List<Integer> domain;
                            if (number > 0 && number < 10) {
                                // This square is pre-assigned
                                domain = new ArrayList<>(List.of(number));
                            } else {
                                // This square is open
                                domain = new ArrayList<>(defaultDomain);
                            }
                            allVariables.put(vName, domain);
                        }
                    } else {
                        // If a line is empty, or if the remaining lines are missing,
                        // treat an empty or missing line as all squares in that
                        // line are open
                        for (int j = 0; j < 9; j++) {
                            Square vName = new Square(i, j);
                            List<Integer> domain = new ArrayList<>(defaultDomain);
                            allVariables.put(vName, domain);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            //noinspection CallToPrintStackTrace
            ioe.printStackTrace();
        }
        return allVariables;
    }

    /**
     * Prints the current state of the Sudoku puzzle to the console.
     *
     * @param allVariables a map representing the Sudoku grid.
     */
    public void printPuzzle(Map<Square, List<Integer>> allVariables) {
        @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
        StringBuilder solution = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (allVariables.get(new Square(i, j)).size() > 1) {
                    System.out.print("[ ]");
                    solution.append("[ ]");
                } else {
                    System.out.print(
                            "[" + allVariables.get(new Square(i, j)).getFirst() + "]");
                    solution.append("[").append(allVariables.get(new Square(i, j)).getFirst()).append("]");
                }
            }
            System.out.print("\r\n");
            solution.append("\r\n");
        }
    }

    /**
     * Given a square, return all its neighbors as a list.
     * <p>
     * "Neighbors" are those squares in the same row, column, or 3x3 box
     * 
     * @param sq the square
     * @return List of neighboring squares
     */
    public List<Square> getNeighborsOf(Square sq) {
        return (new ArrayList<>(neighbors.get(sq)));
    }

    /**
     * @return the list of pre-assigned squares
     */
    public List<Square> getPreAssignedVariables() {
        List<Square> assigned = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (allVariables.get(new Square(i, j)).size() == 1) {
                    assigned.add(new Square(i, j));
                }
            }
        }
        return assigned;
    }

}