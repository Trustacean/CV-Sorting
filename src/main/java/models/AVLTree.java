package main.java.models;

import java.util.Comparator;

class AVLNode {
    CV data;
    AVLNode left, right;
    int height;
    CV[] resultArray; // Replace ArrayList with an array

    public AVLNode(CV cv) {
        data = cv;
        height = 1;
    }
}

public class AVLTree {
    private AVLNode root;
    private int size; // Variable to store the size of the AVL tree

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(CV cv, Comparator<CV> comparator) {
        root = insert(root, cv, comparator);
        size++;
    }

    private int height(AVLNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private int getBalance(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private AVLNode insert(AVLNode node, CV cv, Comparator<CV> comparator) {
        if (node == null)
            return new AVLNode(cv);

        if (comparator.compare(cv, node.data) < 0)
            node.left = insert(node.left, cv, comparator);
        else if (comparator.compare(cv, node.data) > 0)
            node.right = insert(node.right, cv, comparator);
        else // Duplicate CVs are not allowed
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && comparator.compare(cv, node.left.data) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && comparator.compare(cv, node.right.data) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && comparator.compare(cv, node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && comparator.compare(cv, node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public CV[] inOrder() {
        CV[] result = new CV[size];
        inOrder(root, result, 0);
        return result;
    }

    private int inOrder(AVLNode node, CV[] result, int index) {
        if (node != null) {
            index = inOrder(node.left, result, index);
            result[index++] = node.data;
            index = inOrder(node.right, result, index);
        }
        return index;
    }

    public CV[] preOrder() {
        CV[] result = new CV[size];
        preOrder(root, result, 0);
        return result;
    }

    private int preOrder(AVLNode node, CV[] result, int index) {
        if (node != null) {
            result[index++] = node.data;
            index = preOrder(node.left, result, index);
            index = preOrder(node.right, result, index);
        }
        return index;
    }

    public CV[] postOrder() {
        CV[] result = new CV[size];
        postOrder(root, result, 0);
        return result;
    }

    private int postOrder(AVLNode node, CV[] result, int index) {
        if (node != null) {
            index = postOrder(node.left, result, index);
            index = postOrder(node.right, result, index);
            result[index++] = node.data;
        }
        return index;
    }

    public CV searchById(String id) {
        return searchById(root, id);
    }

    private CV searchById(AVLNode node, String id) {
        if (node == null) {
            return null; // CV not found
        }

        int comparison = id.compareTo(node.data.getId());

        if (comparison == 0) {
            return node.data; // Found the CV
        } else if (comparison < 0) {
            return searchById(node.left, id); // Search in the left subtree
        } else {
            return searchById(node.right, id); // Search in the right subtree
        }
    }

    public CV searchByName(String name) {
        return searchByName(root, name);
    }
    
    private CV searchByName(AVLNode node, String name) {
        if (node == null) {
            return null; // CV not found
        }
    
        int comparison = name.compareTo(node.data.getName()); // Compare with the name, not ID
    
        if (comparison == 0) {
            return node.data; // Found the CV
        } else if (comparison < 0) {
            return searchByName(node.left, name); // Search in the left subtree
        } else {
            return searchByName(node.right, name); // Search in the right subtree
        }
    }
    
}
