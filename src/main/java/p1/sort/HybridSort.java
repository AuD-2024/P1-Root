package p1.sort;

import p1.comparator.CountingComparator;

import java.util.Comparator;

/**
 * A hybrid sorting algorithm. It uses a combination of quickSort and insertionSort.
 * <p>
 * quickSort is used for sorting the lists of size greater than or equal to k.
 * <p>
 * insertionSort is used for sorting the lists of size less than k.
 *
 * @param <T> the type of the elements to be sorted.
 *
 * @see Sort
 */
public class HybridSort<T> implements Sort<T> {

    /**
     * The threshold for switching from quickSort to insertionSort.
     */
    private int k;

    /**
     * The comparator used for comparing the sorted elements.
     */
    private final CountingComparator<T> comparator;

    /**
     * Creates a new {@link HybridSort} instance.
     *
     * @param k          the threshold for switching from quickSort to insertionSort.
     * @param comparator the comparator used for comparing the sorted elements.
     */
    public HybridSort(int k, Comparator<T> comparator) {
        this.k = k;
        this.comparator = new CountingComparator<>(comparator);
    }

    @Override
    public void sort(SortList<T> sortList) {
        comparator.reset();
        mergeSort(sortList, 0, sortList.getSize() - 1);
    }

    @Override
    public int getComparisonsCount() {
        return comparator.getComparisonsCount();
    }

    /**
     * Returns the current threshold for switching from quickSort to insertionSort.
     * @return the current threshold for switching from quickSort to insertionSort.
     */
    public int getK() {
        return k;
    }

    /**
     * Sets the threshold for switching from quickSort to insertionSort.
     * @param k the new threshold.
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * Sorts the given {@link SortList} using the mergeSort algorithm.
     * It will only consider the elements between the given left and right indices (both inclusive).
     * Elements with indices less than left or greater than right will not be altered.
     * <p>
     * Once the amount of elements to sort is less than the threshold {@link #k}, the algorithm switches to bubbleSort.
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the list to be sorted. (inclusive)
     * @param right The rightmost index of the list to be sorted. (inclusive)
     */
    public void mergeSort(SortList<T> sortList, int left, int right) {

        if (right - left + 1 < k) {
            bubbleSort(sortList, left, right);
        } else if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(sortList, left, middle);
            mergeSort(sortList, middle + 1, right);
            merge(sortList, left, middle, right);
        }

    }

    /**
     * Merges the two sorted sublists between the indices left and right (both inclusive) of the given {@link SortList}.
     * The middle index separates the two sublists and is the last index of the left sublist.
     *
     * <p>The left sublist ranges from left to middle (both inclusive) and the right sublist ranges from
     * middle + 1 to right (both inclusive). Bot sublists are sorted.
     *
     * <p>The algorithm uses a temporary {@link SortList} to store the merged elements. The results are copied back to
     * the original {@link SortList} at the same location. Elements with indices less than left or greater than right
     * will not be altered.
     *
     * <p>After merging the elements between left and right (both inclusive) will be sorted.
     *
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the two sublists to be merged. (inclusive)
     * @param middle The index that separates the two sublists. It is the last index that belongs to the left sublist.
     * @param right The rightmost index of the two sublists to be merged. (inclusive)
     */
    public void merge(SortList<T> sortList, int left, int middle, int right) {

        int size = right - left + 1;
        SortList<T> tmp = new ArraySortList<>(size);

        int pl = left;
        int pr = middle + 1;

        for (int i = 0; i < size; i++) {

            if (pr > right || (pl <= middle && comparator.compare(sortList.get(pl), sortList.get(pr)) <= 0)) {
                tmp.set(i, sortList.get(pl));
                pl++;
            } else {
                tmp.set(i, sortList.get(pr));
                pr++;
            }
        }

        for (int i = 0; i < size; i++) {
            sortList.set(left + i, tmp.get(i));
        }
    }

    /**
     * Sorts the given {@link SortList} using the bubbleSort algorithm.
     * It will only consider the elements between the given left and right indices (both inclusive).
     * Elements with indices less than left or greater than right will not be altered.
     *
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the list to be sorted.
     * @param right The rightmost index of the list to be sorted.
     */
    public void bubbleSort(SortList<T> sortList, int left, int right) {

        for (int i = right; i > left; i--) {
            for (int j = left; j < i; j++) {
                if (comparator.compare(sortList.get(j), sortList.get(j + 1)) > 0) {
                    T temp = sortList.get(j + 1);
                    sortList.set(j + 1, sortList.get(j));
                    sortList.set(j, temp);
                }
            }
        }
    }

}
