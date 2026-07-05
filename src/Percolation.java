import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private boolean[][] matrix;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int openCells;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        matrix = new boolean[size][size];
        weightedQuickUnionUF = new WeightedQuickUnionUF((size * size) + 2);

        openCells = 0;
        virtualTop = (size * size);
        virtualBottom = virtualTop + 1;
    }

    public void open(int row, int col) {
        int idxRow = row - 1;
        int idxCol = col - 1;
        isIndexInBounds(idxRow, idxCol, true);


        if (matrix[idxRow][idxCol]) {
            return;
        }

        matrix[idxRow][idxCol] = true;
        openCells += 1;

        if (idxRow == 0) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                virtualTop
            );
        }

        if (idxRow == size - 1) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                virtualBottom
            );
        }

        boolean openTop = isIndexInBounds(idxRow - 1, idxCol, false);
        if (openTop && matrix[idxRow - 1][idxCol]) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                getFlatIndex(idxRow - 1, idxCol)
            );
        }

        boolean openBottom = isIndexInBounds(idxRow + 1, idxCol, false);
        if (openBottom && matrix[idxRow + 1][idxCol]) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                getFlatIndex(idxRow + 1, idxCol)
            );
        }

        boolean openLeft = isIndexInBounds(idxRow, idxCol - 1, false);
        if (openLeft && matrix[idxRow][idxCol - 1]) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                getFlatIndex(idxRow, idxCol - 1)
            );
        }

        boolean openRight = isIndexInBounds(idxRow, idxCol + 1, false);
        if (openRight && matrix[idxRow][idxCol + 1]) {
            weightedQuickUnionUF.union(
                getFlatIndex(idxRow, idxCol),
                getFlatIndex(idxRow, idxCol + 1)
            );
        }
    }

    public boolean isOpen(int row, int col) {
        int idxRow = row - 1;
        int idxCol = col - 1;
        isIndexInBounds(idxRow, idxCol, true);

        return matrix[idxRow][idxCol];
    }

    public boolean isFull(int row, int col) {
        int idxRow = row - 1;
        int idxCol = col - 1;
        isIndexInBounds(idxRow, idxCol, true);

        int topComponent = weightedQuickUnionUF.find(virtualTop);
        int component = weightedQuickUnionUF.find(
            getFlatIndex(idxRow, idxCol)
        );

        return topComponent == component;
    }

    public int numberOfOpenSites() {
        return openCells;
    }

    public boolean percolates() {
        int topComponent = weightedQuickUnionUF.find(virtualTop);
        int bottomComponent = weightedQuickUnionUF.find(virtualBottom);
        return topComponent == bottomComponent;
    }

    // public static void main() {
    //     Percolation percolation = new Percolation(10);

    //     percolation.open(-1, 5);
    // }

    private boolean isIndexInBounds(
        int idxRow,
        int idxCol,
        boolean raiseException
    ) {
        boolean inBounds = idxRow >= 0
            && idxRow < size
            && idxCol >= 0
            && idxCol < size;

        if (raiseException && !inBounds) {
            throw new IllegalArgumentException("Index is out of bounds");
        }

        return inBounds;
    }

    private int getFlatIndex(int idxRow, int idxCol) {
        return (idxRow * size) + idxCol;
    }
}
