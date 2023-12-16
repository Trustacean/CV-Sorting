package main.java.models;

import java.util.ArrayList;
import java.util.Comparator;

class AVLNode {
    CV data;
    AVLNode left, right;
    int height;
    ArrayList<CV> result;

    public AVLNode(CV cv) {
        data = cv;
        height = 1;
    }
}

public class AVLTree {
    private AVLNode root;

    public void insert(CV cv, Comparator<CV> comparator) {
        root = insert(root, cv, comparator);
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

    public ArrayList<CV> inOrder() {
        ArrayList<CV> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(AVLNode node, ArrayList<CV> result) {
        if (node != null) {
            inOrder(node.left, result);
            result.add(node.data);
            inOrder(node.right, result);
        }
    }

    public ArrayList<CV> preOrder() {
        ArrayList<CV> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(AVLNode node, ArrayList<CV> result) {
        if (node != null) {
            result.add(node.data);
            preOrder(node.left, result);
            preOrder(node.right, result);
        }
    }

    public ArrayList<CV> postOrder() {
        ArrayList<CV> result = new ArrayList<>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(AVLNode node, ArrayList<CV> result) {
        if (node != null) {
            postOrder(node.left, result);
            postOrder(node.right, result);
            result.add(node.data);
        }
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

    public ArrayList<CV> searchByName(String name) {
        ArrayList<CV> result = new ArrayList<>();
        searchByName(root, name, result);
        return result;
    }

    private void searchByName(AVLNode node, String name, ArrayList<CV> result) {
        if (node != null) {
            int comparison = name.compareTo(node.data.getName());

            if (comparison == 0) {
                // Found a matching CV
                result.add(node.data);
                // Continue searching in both subtrees in case there are multiple CVs with the same name
                searchByName(node.left, name, result);
                searchByName(node.right, name, result);
            } else if (comparison < 0) {
                // Search in the left subtree
                searchByName(node.left, name, result);
            } else {
                // Search in the right subtree
                searchByName(node.right, name, result);
            }
        }
    }
}
