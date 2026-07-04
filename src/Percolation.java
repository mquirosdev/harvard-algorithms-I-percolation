import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.Arrays;
import java.util.ArrayList;

public class Percolation {
    private int size;
    private int opened;
    private boolean[][] matrix;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int n) {
        if(n <= 0) { throw new IllegalArgumentException("n needs to be at least 1"); }

        opened = 0;
        size = n;

        // Initialize percolation matrix
        matrix = new boolean[size][size];
        for (boolean[] row : matrix) { 
            Arrays.fill(row, false);
        }

        // Initialize quick union
        weightedQuickUnionUF = new WeightedQuickUnionUF((size * size) + 2);
    }

    // Indexes are in base 1
    public void open(int idxRow, int idxCol) {
        idxRow -= 1; idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        // Check if the cell is already open
        boolean cellValue = matrix[idxRow][idxCol];
        if(cellValue) { return; }

        opened += 1;
        // Open the node and connect with open neighbors
        matrix[idxRow][idxCol] = true;
        int currentNode = getCellIndex(idxRow, idxCol);
        for(int node : getConnections(idxRow, idxCol)) {
            weightedQuickUnionUF.union(currentNode, node);
        }
    }

    // Indexes are in base 1
    public boolean isOpen(int idxRow, int idxCol) {
        idxRow -= 1; idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        return matrix[idxRow][idxCol];
    }

    // Indexes are in base 1
    public boolean isFull(int idxRow, int idxCol) {
        idxRow -= 1; idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        if(!isOpen(idxRow, idxCol)) { return false; }

        int cellIndex = getCellIndex(idxRow, idxCol);
        return weightedQuickUnionUF.find(cellIndex) == weightedQuickUnionUF.find((size * size));
    }

    public int numberOfOpenSites() {
        return opened;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.find((size * size)) == weightedQuickUnionUF.find((size * size) + 1);
    }

    private void checkIndexInBounds(int idxRow, int idxCol) {
        boolean rowOutOfBounds = idxRow < 0 || idxRow >= matrix.length;
        boolean colOutOfBounds = idxCol < 0 || idxCol >= matrix[0].length;
        if(rowOutOfBounds || colOutOfBounds) {
            throw new IllegalArgumentException("row or col out of bounds");
        }
    }

    private ArrayList<Integer> getConnections(int idxRow, int idxCol) {
        ArrayList<Integer> connections = new ArrayList<Integer>();

        // Check the open neighbors to connect them
        int[][] neighbors = {
                {idxRow + 1, idxCol},
                {idxRow - 1, idxCol},
                {idxRow, idxCol + 1},
                {idxRow, idxCol - 1}
        };

        for(int[] neighbor : neighbors) {
            // Check that the neighbor is not out of bounds
            if(neighbor[0] < 0 || neighbor[0] >= matrix.length) { continue; }
            if(neighbor[1] < 0 || neighbor[1] >= matrix[0].length) { continue; }

            if(matrix[neighbor[0]][neighbor[1]]) {
                int nodeNumber = getCellIndex(neighbor[0], neighbor[1]);
                connections.add(nodeNumber);
            }
        }

        // Check if it should be connected to the top node
        if(idxRow == 0) { connections.add((size * size)); }

        // Check if it should be connected to the bottom node
        if(idxRow == matrix.length - 1) { connections.add((size * size) + 1); }

        return connections;
    }

    private int getCellIndex(int idxRow, int idxCol) {
        return (idxRow * size) + idxCol;
    }
}
