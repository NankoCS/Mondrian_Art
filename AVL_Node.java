import java.awt.*;
import java.lang.Math;

public class AVL_Node {

	double weight;
	int height;
	AVL_Node left, right;
	Kd_node kd_node;

	public AVL_Node(double weight, Kd_node kd_node, AVL_Node left, AVL_Node right) {
		this.weight = weight;
		this.kd_node = kd_node;
		this.left = left;
		this.right = right;
	}
}
