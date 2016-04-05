/*
 * offer a class for sort data.
 */
class SortData {
	/*
	 * the value of the number
	 */
	int data;
	/*
	 * the location of the number
	 */
	int index;
}

/*
 * offer a class which has sort methods
 */
public class MySort {
	/*
	 * array of sort data
	 */
	SortData a[];
	/*
	 * n is total numbers of sort data
	 */
	int n;

	/*
	 * structure method, randomly produce some numbers, stored in array a[]
	 */
	MySort() {
		n = (int) (Math.random() * 1000);
		//n=3000;
		a = new SortData[n];
		for (int i = 0; i < n; i++) {
			a[i] = new SortData();
			a[i].data = (int) (Math.random() * n);
			a[i].index = i;
		}
	}

	/*
	 * quickSort which is not stable
	 */
	public void qSort(SortData[] a, int left, int right) {
		int x = left + (int) Math.floor(Math.random() * (right - left));
		SortData temp;
		int key = a[x].data;
		int i = left, j = right;
		while (i <= j) {
			while (a[i].data < key)
				i++;
			while (a[j].data > key)
				j--;
			if (i <= j) {
				temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}
		if (i < right)
			qSort(a, i, right);
		if (j > left)
			qSort(a, left, j);
	}

	/*
	 * stable quickSort
	 */
	public void qStableSort(SortData[] a, int left, int right) {
		int x = left + (int) Math.floor(Math.random() * (right - left));
		//int x = (left+right)/2;
		SortData temp;
		int key1 = a[x].data, key2 = a[x].index;
		int i = left, j = right;
		while (i <= j) {
			while ((a[i].data < key1)
					|| (a[i].data == key1 && a[i].index < key2))
				i++;
			while ((a[j].data > key1)
					|| (a[j].data == key1 && a[j].index > key2))
				j--;
			if (i <= j) {
				temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}
		if (i < right)
			qStableSort(a, i, right);
		if (j > left)
			qStableSort(a, left, j);
	}

	/*
	 * stable insertionSort
	 */
	public void insertionSort(SortData a[], int left, int right) {
		int i;
		for (i = left + 1; i <= right; i++) {
			SortData key = a[i];// get the insert number
			int j = i - 1;
			while (j >= left
					&& (a[j].data > key.data || (a[j].data == key.data && a[j].index > key.index))) { 
				a[j + 1] = a[j];
				j--;
			}
			a[j + 1] = key; // insert
		}
	}

	/*
	 * stable quickSort combined with insertionSort
	 */
	public void qFastStableSort(SortData[] a, int left, int right) {
		if (right - left <= 300) {
			insertionSort(a, left, right);
			return;
		}
		int x = left + (int) Math.floor(Math.random() * (right - left));
		SortData temp;
		int key1 = a[x].data, key2 = a[x].index;
		int i = left, j = right;
		while (i <= j) {
			while ((a[i].data < key1)
					|| (a[i].data == key1 && a[i].index < key2))
				i++;
			while ((a[j].data > key1)
					|| (a[j].data == key1 && a[j].index > key2))
				j--;
			if (i <= j) {
				temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				i++;
				j--;
			}
		}
		if (i < right)
			qFastStableSort(a, i, right);
		if (j > left)
			qFastStableSort(a, left, j);
	}
}
