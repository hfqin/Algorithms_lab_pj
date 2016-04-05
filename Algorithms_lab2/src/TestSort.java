import java.util.Arrays;
public class TestSort {

	public static void main(String[] args) {
		MySort mySort = new MySort();
		int length;
		boolean isRight = true;
		long startTime, endTime, insertionSortTime, qStableSortTime, qFastStableSortTime;
		SortData a[], b[], c[];
		length = mySort.a.length;
		
		a = (SortData[]) Arrays.copyOf(mySort.a,length);
		b = (SortData[]) Arrays.copyOf(mySort.a,length);
		c = (SortData[]) Arrays.copyOf(mySort.a,length);
		
		//test insertionSort
		startTime = System.nanoTime();
		mySort.insertionSort(a, 0, mySort.n - 1);
		endTime = System.nanoTime();
		insertionSortTime = endTime-startTime;
		
		//test qStableSort
		startTime = System.nanoTime();
		mySort.qStableSort(b, 0, mySort.n - 1);
		endTime = System.nanoTime();
		qStableSortTime = endTime-startTime;
		
		//test qFastStableSort(combine)
		startTime = System.nanoTime();
		mySort.qFastStableSort(c, 0, mySort.n - 1);
		endTime = System.nanoTime();
		qFastStableSortTime = endTime-startTime;
		
		//check the sort answer
		for (int i = 0; i < mySort.n; i++) {
			//make sure the value is not different for three kinds of sort
			if (a[i].data != b[i].data || a[i].data != c[i].data || b[i].data != c[i].data) {
				isRight = false;
				break;
			}
			
			//make sure the order is not different for three kinds of sort
			if (a[i].index != b[i].index || a[i].index != c[i].index || b[i].index != c[i].index) {
				isRight = false;
				break;
			}
			
			if (i == 0) continue;
			
			//make sure the data is in order and stable
			if (a[i].data < a[i-1].data || (a[i].data == a[i-1].data && a[i].index <= a[i-1].index)) {
				isRight = false;
				break;
			}
		}
		
		if (isRight) {
			System.out.println(mySort.n + " numbers sort:");
			System.out.println("Congratulation! Your answer is right!");
			System.out.println(insertionSortTime + " ns for insertionSort");
			System.out.println(qStableSortTime + " ns for qStableSort");
			System.out.println(qFastStableSortTime + " ns for qFastStableSort(combine)");
		} else {
			System.out.println("Sorry! Your answer is wrong!");
		}
	}

}
