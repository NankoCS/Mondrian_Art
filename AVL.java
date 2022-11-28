public class AVL {
	public AVL_Node root;

	// return the height of a node
	public int height(AVL_Node N) {
		if (N == null) {
			return -1;
		} else {
			return N.height;
		}
	}

	public void updateHeight(AVL_Node n) {
		n.height = Math.max(height(n.left), height(n.right));
	}

	public int getBalance(AVL_Node n) {
		if (n == null) {
			return 0;

		} else {
			return height(n.right) - height(n.left);
		}

	}

	public AVL_Node ROTD(AVL_Node a) {
		AVL_Node b = a.left;
		AVL_Node c = b.right;

		b.right = a;
		a.left = c;

		updateHeight(b);
		updateHeight(a);

		return a;
	}

	public AVL_Node ROTG(AVL_Node a) {
		AVL_Node b = a.right;
		AVL_Node c = b.left;

		b.left = a;
		a.right = c;

		updateHeight(b);
		updateHeight(a);

		return a;
	}

	public AVL_Node DROTG(AVL_Node a) {
		a.right = ROTD(a.right);

		return ROTG(a);
	}

	public AVL_Node DROTD(AVL_Node a) {
		a.left = ROTG(a.left);

		return ROTD(a);
	}

	// Balance the height
	public AVL_Node Balance(AVL_Node d) {
		updateHeight(d);
		int balance = getBalance(d);

		if (balance > 1) {
			if (height(d.right.right) > height(d.right.left)) {
				d = ROTG(d);
			} else {
				d = DROTG(d);
			}
		} else if (balance < -1) {
			if (height(d.left.left) > height(d.left.right)) {
				d = ROTD(d);
			} else {
				d.left = DROTD(d);
			}
		}
		return d;

	}

	// Basic one if you wanna search a weight
	public AVL_Node search(double weight) {
		AVL_Node curr = this.root;
		while (curr != null) {
			if (curr.weight == weight) {
				break;
			}
			if (curr.weight < weight) {
				curr = curr.left;
			} else {
				curr = curr.right;
			}
		}
		return curr;
	}

	// while loop to add, add 0.1 to weight if already in
	// Recursive function that insert your Kd_leaf
	public AVL_Node insert(AVL_Node n, double weight, Kd_node kd_node) // travail sur la hauteur
	{
		if (n == null) {
			return new AVL_Node(weight, kd_node, null, null);
		} else if (weight < n.weight) {
			n.left = insert(n.left, weight, kd_node);
		} else if (weight > n.weight) {
			n.right = insert(n.right, weight, kd_node);
		} else if (weight == n.weight) {
			n.right = insert(n.right, weight + 0.1, kd_node);
		}
		return Balance(n);
	}

	// Function that return node with the biggest Weight
	public AVL_Node getMaxWeight(AVL_Node n) {
		if (n != null) {
			while (n.right != null) {
				n = n.right;
			}
		}
		return n;
	}

	// Recursive function
	public AVL_Node remove(AVL_Node root, double weight) {
		if (root == null) {
			return root;
		}

		if (weight > root.weight) {
			root.right = remove(root.right, weight);
		} else if (weight < root.weight) {
			root.left = remove(root.left, weight);
		} else {
			if (root.left == null || root.right == null) {
				AVL_Node tmp = null;
				if (tmp == root.left) {
					tmp = root.right;
				} else {
					tmp = root.left;
				}

				// No child case
				if (tmp == null) {
					tmp = root;
					root = null;
				} else {
					root = tmp;
				}
			}
			// 2 non-null children
			// We get the maximum from the left subtree
			else {
				AVL_Node maxWeightChild = getMaxWeight(root.left);
				root.weight = maxWeightChild.weight;
				root.left = remove(root.left, root.weight);
			}

		}

		if (root != null) {
			root = Balance(root);
		}
		return root;

	}

	void parcours_sym(AVL_Node node) {
		if (node != null) {
			parcours_sym(node.left);
			System.out.println(node.weight + " " + node.kd_node.getHeight() + " - " + node.kd_node.getWidth());
			parcours_sym(node.right);
		}
	}

	public static void main(String[] args) {

		AVL tree = new AVL();
		tree.root = tree.insert(tree.root, 10, null);
		tree.root = tree.insert(tree.root, 20, null);
		tree.root = tree.insert(tree.root, 30, null);
		tree.root = tree.insert(tree.root, 40, null);
		tree.root = tree.insert(tree.root, 50, null);
		tree.root = tree.insert(tree.root, 25, null);
		tree.parcours_sym(tree.root);
		tree.remove(tree.root, 25);
		System.out.println();
		tree.parcours_sym(tree.root);

	}

}
