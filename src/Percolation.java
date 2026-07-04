import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private int opened;
    private boolean[][] matrix;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n needs to be at least 1");
        }

        opened = 0;
        size = n;

        matrix = new boolean[size][size];
        for (boolean[] row : matrix) {
            for (int i = 0; i < row.length; i++) {
                row[i] = false;
            }
        }

        weightedQuickUnionUF = new WeightedQuickUnionUF((size * size) + 2);
    }

    // Indexes are in base 1
    public void open(int idxRow, int idxCol) {
        idxRow -= 1;
        idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        // Check if the cell is already open
        boolean cellValue = matrix[idxRow][idxCol];
        if (cellValue) {
            return;
        }

        opened += 1;
        // Open the node and connect with open neighbors
        matrix[idxRow][idxCol] = true;
        int currentNode = getCellIndex(idxRow, idxCol);
        for (int node : getConnections(idxRow, idxCol)) {
            if (node == -1) {
                continue;
            }
            weightedQuickUnionUF.union(currentNode, node);
        }
    }

    // Indexes are in base 1
    public boolean isOpen(int idxRow, int idxCol) {
        idxRow -= 1;
        idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        return matrix[idxRow][idxCol];
    }

    // Indexes are in base 1
    public boolean isFull(int idxRow, int idxCol) {
        idxRow -= 1;
        idxCol -= 1;
        checkIndexInBounds(idxRow, idxCol);

        if (!isOpen(idxRow, idxCol)) {
            return false;
        }

        int cellIndex = getCellIndex(idxRow, idxCol);
        int cellConnectedSet = weightedQuickUnionUF.find(cellIndex);
        int topConnectedSet = weightedQuickUnionUF.find((size * size));
        return cellConnectedSet == topConnectedSet;
    }

    public int numberOfOpenSites() {
        return opened;
    }

    public boolean percolates() {
        int topComponentSet = weightedQuickUnionUF.find((size * size));
        int bottomComponentSet = weightedQuickUnionUF.find((size * size) + 1);
        return topComponentSet == bottomComponentSet;
    }

    private void checkIndexInBounds(int idxRow, int idxCol) {
        boolean rowOutOfBounds = idxRow < 0 || idxRow >= matrix.length;
        boolean colOutOfBounds = idxCol < 0 || idxCol >= matrix[0].length;
        if (rowOutOfBounds || colOutOfBounds) {
            throw new IllegalArgumentException("row or col out of bounds");
        }
    }

    private int[] getConnections(int idxRow, int idxCol) {
        int[] connections = new int[6];

        // Check the open neighbors to connect them
        int[][] neighbors = {
                {
                    idxRow + 1,
                    idxCol
                },
                {
                    idxRow - 1,
                    idxCol
                },
                {
                    idxRow,
                    idxCol + 1
                },
                {
                    idxRow,
                    idxCol - 1
                }
        };

        for (int i = 0; i < neighbors.length; i++) {
            int[] neighbor = neighbors[i];
            // Check that the neighbor is not out of bounds
            if (neighbor[0] < 0 || neighbor[0] >= matrix.length) {
                connections[i] = -1;
                continue;
            }

            if (neighbor[1] < 0 || neighbor[1] >= matrix[0].length) {
                connections[i] = -1;
                continue;
            }

            if (matrix[neighbor[0]][neighbor[1]]) {
                int nodeNumber = getCellIndex(neighbor[0], neighbor[1]);
                connections[i] = nodeNumber;
            } else {
                connections[i] = -1;
            }
        }

        // Check if it should be connected to the top node
        if (idxRow == 0) {
            connections[4] = size * size;
        } else {
            connections[4] = -1;
        }

        // Check if it should be connected to the bottom node
        if (idxRow == matrix.length - 1) {
            connections[5] = (size * size) + 1;
        } else {
            connections[5] = -1;
        }

        return connections;
    }

    private int getCellIndex(int idxRow, int idxCol) {
        return (idxRow * size) + idxCol;
    }
}
