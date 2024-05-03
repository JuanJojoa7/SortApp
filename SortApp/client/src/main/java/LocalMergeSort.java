public class LocalMergeSort {
    public void mergeSort(String[] arr) {
        if (arr.length <= 1) {
            return;
        }

        int mid = arr.length / 2;
        String[] leftHalf = new String[mid];
        String[] rightHalf = new String[arr.length - mid];
        System.arraycopy(arr, 0, leftHalf, 0, mid);
        System.arraycopy(arr, mid, rightHalf, 0, arr.length - mid);

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        merge(arr, leftHalf, rightHalf);
    }

    private void merge(String[] arr, String[] left, String[] right) {
        int leftIndex = 0, rightIndex = 0, arrIndex = 0;

        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex].compareTo(right[rightIndex]) < 0) {
                arr[arrIndex++] = left[leftIndex++];
            } else {
                arr[arrIndex++] = right[rightIndex++];
            }
        }

        while (leftIndex < left.length) {
            arr[arrIndex++] = left[leftIndex++];
        }

        while (rightIndex < right.length) {
            arr[arrIndex++] = right[rightIndex++];
        }
    }
}
