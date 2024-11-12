/*
Balanced Tree
When using a tree data structure, it's common for the tree to become unbalanced over time due to the insertion order of nodes, which can in turn affect the performance of our programs. Let's define a balanced tree as one where the difference in height of the left and right subtrees is at most one, for all nodes in the given tree. Write a function is_balanced(node) that determines whether a binary tree is balanced or not.

Input: The root node of a binary tree

Output: True if the tree is balanced, False otherwise.

Assume you are given the root node of a tree that conforms to the following interface:
Iterative approach
*/

import java.util.*;
public class Solution {

    // Definition for a binary tree node
    static class Node {
        String value;
        Node left;
        Node right;

        Node(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        Node(String value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    static class StackElement {

        Node node;
        Integer leftChildHeight;
        Integer rightChildHeight;

        public StackElement(Node node) {
            this.node = node;
        }

    }

    public static boolean isBalanced(Node node) {
        // your code goes here
        if(node==null) return 0;
        Stack<StackElement> st = new Stack<>();
        st.push(new StackElement(node));

        while(!st.empty()) {

            StackElement currElement = st.pop();
            if(currElement.leftChildHeight != null && currElement.rightChildHeight != null) { //below subTree is visited
                int diff = Math.abs(currElement.leftChildHeight - currElement.rightChildHeight);
                if(diff>1) return false;
                if(st.empty()) break;
                if(st.peek().leftChildHeight == null) {
                    st.peek().leftChildHeight = Math.max(currElement.leftChildHeight, currElement.rightChildHeight) + 1;
                } else if(st.peek().rightChildHeight == null){
                    st.peek().rightChildHeight = Math.max(currElement.leftChildHeight, currElement.rightChildHeight) + 1;
                }
            } else if(currElement.leftChildHeight != null) {  // left is completed , try visiting right

                if(currElement.node.right!=null) {
                    st.push(currElement);
                    st.push(new StackElement(currElement.node.right));
                } else {
                    currElement.rightChildHeight = 0;
                    st.push(currElement);
                }

            } else {                                          // left is unvisited

                if(currElement.node.left != null) {
                    st.push(currElement);
                    st.push(new StackElement(currElement.node.left));
                } else {
                    currElement.leftChildHeight = 0;
                    st.push(currElement);
                }

            }

        }



        return true;


    }

    public static void main(String[] args) {
        // debug your code below
        Node a = new Node("a",
                new Node("b",
                        new Node("d"),
                        new Node("e")),
                new Node("c",
                        null,
                        new Node("f")));

        System.out.println(isBalanced(a)); // Should return true for a balanced tree
    }
}
