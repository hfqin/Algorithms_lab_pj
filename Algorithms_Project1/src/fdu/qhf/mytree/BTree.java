package fdu.qhf.mytree;

import java.util.ArrayList;

/*
 * class B-Tree
 */
public class BTree {
	/*
	 * member variable, the root of B-Tree.
	 */
	private TreeNode root = null;
	
	/*
	 * member variable, the branch t
	 */
	private int t;
	
	/*
	 * member variable, the branch fullNum
	 */
	private final int fullNum;

	/*
	 * structure method, get data and initial
	 */
	public BTree(int t, ArrayList<String> arrayList1,
			ArrayList<String> arrayList2) {
		this.t = t;
		this.fullNum = 2 * t - 1;
		createRoot();
		initial(arrayList1, arrayList2);
	}

	/*
	 * create root for B-tree
	 */
	private void createRoot() {
		root = new TreeNode();
		root.isLeaf = true;
		root.num = 0;
	}

	/*
	 * initial
	 */
	private void initial(ArrayList<String> arrayList1,
			ArrayList<String> arrayList2) {
		int length = arrayList1.size();
		for (int i = 0; i < length; i++) {
			insert(arrayList1.get(i), arrayList2.get(i));

		}
	}

	/*
	 * get node x's No.i element's precursor
	 */
	private TreeNode precursor(TreeNode x, int i) {
		TreeNode y = x.child[i];
		TreeNode ans = y;
		while (y != null) {
			ans = y;
			y = y.child[y.num];
		}
		return ans;
	}

	/*
	 * get node x's No.i element's successor
	 */
	private TreeNode successor(TreeNode x, int i) {
		TreeNode y = x.child[i + 1];
		TreeNode ans = y;
		while (y != null) {
			ans = y;
			y = y.child[0];
		}
		return ans;
	}

	/*
	 * split x's No.i Child
	 */
	private void splitChild(TreeNode x, int i) {
		TreeNode z = new TreeNode();
		TreeNode y = x.child[i];
		z.isLeaf = y.isLeaf;
		z.num = t - 1;
		for (int j = 0; j < t - 1; j++) {
			z.entry[j] = y.entry[j + t];
		}
		if (!y.isLeaf) {
			for (int j = 0; j < t; j++) {
				z.child[j] = y.child[j + t];
			}
		}

		y.num = t - 1;

		for (int j = x.num; j >= i + 1; j--) {
			x.child[j + 1] = x.child[j];
		}

		x.child[i + 1] = z;
		for (int j = x.num; j >= i; j--) {
			x.entry[j + 1] = x.entry[j];
		}

		x.entry[i] = y.entry[t - 1];
		x.num++;
	}

	/*
	 *  merge one element of x's child[i]'s sibling
	 */
	private void mergeSibling(TreeNode x, int i, boolean mergeLeft) {
		if (mergeLeft) {
			TreeNode me = x.child[i];
			TreeNode sibling = x.child[i - 1];
			me.num++;
			for (int j = me.num; j > 0; j--) {
				me.entry[j] = me.entry[j - 1];
				me.child[j] = me.child[j - 1];
			}

			me.child[0] = sibling.child[sibling.num];
			me.entry[0] = x.entry[i - 1];
			x.entry[i - 1] = sibling.entry[sibling.num - 1];
			sibling.child[sibling.num] = sibling.child[sibling.num - 1];
			deleteKey(sibling, sibling.num - 1);
		} else {
			TreeNode me = x.child[i];
			TreeNode sibling = x.child[i + 1];
			me.num++;
			me.entry[me.num - 1] = x.entry[i];
			me.child[me.num] = sibling.child[0];
			x.entry[i] = sibling.entry[0];
			deleteKey(sibling, 0);
		}
	}

	/*
	 *  merge x's child[i] to child[i+1], return child[i+1]
	 */
	private TreeNode mergeChild(TreeNode x, int i) {
		TreeNode y = x.child[i];
		TreeNode z = x.child[i + 1];
		int j = y.num;
		int k = 0;
		y.num = y.num + z.num + 1;
		y.entry[j] = x.entry[i];
		j++;
		while (j < y.num) {
			y.entry[j] = z.entry[k];
			y.child[j] = z.child[k];
			k++;
			j++;
		}
		y.child[y.num] = z.child[z.num];

		z = y;

		deleteKey(x, i);

		return z;
	}

	/*
	 * search entry key, start at x
	 */
	private TreeNode searchEntry(TreeNode x, Entry key) {
		if (x == null)
			return null;
		int i = 0;
		while (i < x.num && key.compare(x.entry[i]) == 1) {
			i++;
		}
		if (i < x.num && key.compare(x.entry[i]) == 0) {
			return x;
		} else {
			if (x.isLeaf) {
				return null;
			} else {
				return searchEntry(x.child[i], key);
			}
		}
	}

	/*
	 * method suit all insertion
	 */
	private void insertEntry(Entry key) {
		if (searchEntry(root, key) != null)
			return;
		TreeNode node = root;
		if (node.isFull()) {
			TreeNode s = new TreeNode();
			root = s;
			s.isLeaf = false;
			s.num = 0;
			s.child[0] = node;
			splitChild(s, 0);

			insertNonFull(s, key);
		} else {
			insertNonFull(root, key);
		}
	}

	/*
	 * method suit only not full insertion
	 */
	private void insertNonFull(TreeNode x, Entry key) {
		int i = x.num - 1;
		if (x.isLeaf) {
			while (i >= 0 && key.compare(x.entry[i]) == -1) {
				x.entry[i + 1] = x.entry[i];
				i--;
			}
			x.entry[i + 1] = key;
			x.num++;
		} else {
			while (i >= 0 && key.compare(x.entry[i]) == -1) {
				i--;
			}
			i++;
			if (x.child[i].isFull()) {
				splitChild(x, i);
				if (key.compare(x.entry[i]) == 1) {
					i++;
				}
			}
			insertNonFull(x.child[i], key);
		}
	}

	/*
	 *  delete the number i element in node x
	 */
	private void deleteKey(TreeNode x, int i) {
		while (i < x.num - 1) {
			x.entry[i] = x.entry[i + 1];
			x.child[i] = x.child[i + 1];
			i++;
		}
		x.child[i] = x.child[i + 1];
		x.num--;

	}

	/*
	 * delete entry key, finding start at node x
	 */
	private void deleteEntry(TreeNode x, Entry key) {
		TreeNode find = searchEntry(x, key);
		TreeNode instead;
		if (find == null)
			return;
		int i = 0;
		if (find == x) {
			i = find.findEntry(key);
			// case 1
			if (x.isLeaf) {
				deleteKey(x, i);

			} else {
				// case 2a
				if (x.child[i].num >= t) {
					instead = precursor(x, i);
					x.entry[i] = instead.entry[instead.num - 1];

					deleteKey(instead, instead.num - 1);
					return;
				}
				// case 2b
				if (x.child[i + 1].num >= t) {
					instead = successor(x, i);
					x.entry[i] = instead.entry[0];

					deleteKey(instead, 0);
					return;
				}
				// case 2c
				if (x == root && x.num == 1) {
					root = mergeChild(x, 0);
					deleteEntry(root, key);
				} else {
					x.child[i] = mergeChild(x, i);
					deleteEntry(x.child[i], key);
				}
			}
		} else {
			int l = 0;
			int r = 0;
			i = 0;

			while (i < x.num && key.compare(x.entry[i]) > 0) {

				i++;
			}
			l = i - 1;
			r = i + 1;
			// case 3
			if (x.child[i].num < t) {
				// case 3a
				if (l >= 0 && x.child[l].num >= t) {
					mergeSibling(x, i, true);
					deleteEntry(x.child[i], key);
					return;
				}
				if (r <= x.num && x.child[r].num >= t) {

					mergeSibling(x, i, false);
					deleteEntry(x.child[i], key);
					return;
				}
				// case 3b
				if (x == root && x.num == 1) {

					x = mergeChild(x, 0);
					root = x;
					deleteEntry(x, key);
				} else {
					if (i == x.num) {
						i--;
					}
					x.child[i] = mergeChild(x, i);
					deleteEntry(x.child[i], key);
				}
			} else {

				deleteEntry(x.child[i], key);
			}

		}
	}

	/*
	 * show all node start at node
	 */
	private String show(TreeNode node, int level, int childNum) {
		String ans = "";
		if (node != null) {
			ans += "level=" + level + " " + "child=" + childNum + " /";
			for (int i = 0; i < node.num; i++) {
				ans += node.entry[i].value + "/";
			}
			ans += System.getProperty("line.separator");
			for (int i = 0; i < node.num + 1; i++) {
				ans += show(node.child[i], level + 1, i);
			}
		}
		return ans;
	}

	/*
	 * show all node from x to y
	 */
	private String show(TreeNode node, Entry x, Entry y) {
		if (node == null)
			return "";
		String ans = "";
		int left = 0;
		int right = node.num - 1;
		while (left < node.num && node.entry[left].compare(x) < 0)
			left++;
		if (!"".equals(y.value.trim())) {
			while (right >= 0 && node.entry[right].compare(y) > 0)
				right--;
		}
		for (int i = left; i <= right; i++) {
			ans += show(node.child[i], x, y);
			if (node.entry[i].compare(x) >= 0
					&& (node.entry[i].compare(y) <= 0 || "".equals(y.value
							.trim()))) {
				ans += node.entry[i].value + " " + node.entry[i].chineseValue
						+ "\n";
			}
		}
		ans += show(node.child[right + 1], x, y);

		return ans;
	}

	/*
	 * insert word, English is x, Chinese is y
	 */
	public void insert(String x, String y) {
		insertEntry(new Entry(x, y));
	}

	/*
	 * delete word, English is x
	 */
	public void delete(String x) {
		deleteEntry(root, new Entry(x, ""));
	}

	/*
	 * search word, English is x, return Chinese
	 */
	public String search(String x) {
		Entry key = new Entry(x, "");
		TreeNode ans = searchEntry(root, key);
		if (ans != null) {
			int index = ans.findEntry(key);
			return ans.entry[index].chineseValue;
		} else {
			return null;
		}
	}

	/*
	 * show the whole tree
	 */
	public String show() {
		return show(root, 0, 0);
	}

	/*
	 * show the tree from x to y
	 */
	public String show(String x, String y) {
		return show(root, new Entry(x, ""), new Entry(y, ""));
	}

	/*
	 * inner class, offer data structure for every node
	 */
	private class TreeNode {
		private boolean isLeaf;
		private int num;
		private Entry entry[] = new Entry[2 * t + 1];
		private TreeNode child[] = new TreeNode[2 * t + 1];

		private boolean isFull() {
			if (num == fullNum) {
				return true;
			} else {
				return false;
			}
		}

		private int findEntry(Entry key) {
			for (int i = 0; i < this.num; i++) {
				if (this.entry[i].compare(key) == 0) {
					return i;
				}
			}
			return -1;
		}
	}

	/*
	 * inner class, it is the implement for the key data in every node 
	 */
	private class Entry {
		private String value;
		private String chineseValue;

		private Entry(String x, String y) {
			// TODO Auto-generated constructor stub
			this.value = x;
			this.chineseValue = y;
		}

		private int compare(Entry key) {
			String str1 = this.value;
			String str2 = key.value;
			if (str1.equals(str2)) {
				return 0;
			}
			if (str1.compareTo(str2) < 0)
				return -1;
			return 1;
		}
	}
}
