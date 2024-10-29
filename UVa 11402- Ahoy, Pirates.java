/*


UVa 11402- Ahoy, Pirates

https://onlinejudge.org/index.php?option=onlinejudge&Itemid=8&page=show_problem&problem=2397

SegmentTree with lazy update 
Important tips :
- Keep note of updating lazyOp and sum at the same time
- Flip operation will flip other operation, 1 such example is set will become clear and vice-versa



*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Main {

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader()
        {
            br = new BufferedReader(
                    new InputStreamReader(System.in));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }

        long nextLong() { return Long.parseLong(next()); }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try {
                str = br.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    static boolean debug = false;


    public static void main(String... args) {

            FastReader in = new FastReader();

            int t = in.nextInt();
            int testCase = 1;
            while(t-->0) {

                int m = in.nextInt();
                int totalLength = 0;

                // First pass: Calculate total length required
                int[] TValues = new int[m];
                String[] sValues = new String[m];

                for (int i = 0; i < m; i++) {
                    int T = in.nextInt();
                    String s = in.next();
                    TValues[i] = T;
                    sValues[i] = s;
                    totalLength += T * s.length();
                }

                // Initialize StringBuilder with the calculated capacity
                StringBuilder st = new StringBuilder(totalLength);

                // Second pass: Append the repeated strings
                for (int i = 0; i < m; i++) {
                    StringBuilder temp = new StringBuilder();
                    for (int j = 0; j < TValues[i]; j++) {
                        temp.append(sValues[i]);
                    }
                    st.append(temp);
                }

                char[] arr = st.toString().toCharArray();
                if(debug) System.out.println(Arrays.toString(arr));
                new Main().solve(arr, in, testCase);
                testCase++;

            }

        }


        private void solve(char[] arr, FastReader in, int testCase) {

            int n = arr.length;
            SegmentTree segTree = new SegmentTree(arr, 0, n-1);
            int q = in.nextInt();

            System.out.println("Case " + testCase + ":");

            int query = 1;
            while(q-->0) {

                char op = in.next().charAt(0);
                int a = in.nextInt();
                int b = in.nextInt();

                if(op=='F') {
                    segTree.set(a, b);
                } else if(op=='I') {
                    segTree.flip(a, b);
                } else if(op=='E') {
                    segTree.clear(a, b);
                } else if(op=='S') {
                    int sum = segTree.getSum(a, b);
                    System.out.println("Q" + query + ": " + sum);
                    query++;
                }
            }

        }

        /*
        * 1: flip
        * 2: clear
        * 3: set
        * 0: nothing
        * */
        class SegmentTree {

            int sum;
            int lazyOp;
            int left;
            int right;

            SegmentTree leftChild;
            SegmentTree rightChild;

            public SegmentTree(char[] arr, int l, int r) {
                left = l;
                right = r;
                if(l==r) {
                    sum = Character.getNumericValue(arr[l]);
                    return;
                }

                int m = l + (r-l)/2;
                leftChild = new SegmentTree(arr, l, m);
                rightChild = new SegmentTree(arr, m+1, r);
                sum = leftChild.sum + rightChild.sum;
            }

            private void flipChild(SegmentTree child) {
                if(child==null) return;
                if(child.lazyOp == 0) {
                    child.lazyOp = 1;
                    child.sum = child.right-child.left+1 - child.sum;
                } else if(child.lazyOp == 1) {
                    child.lazyOp = 0;
                    child.sum = child.right-child.left+1 - child.sum;
                } else if(child.lazyOp == 2) {
                    child.lazyOp = 3;
                    child.sum = child.right-child.left+1;
                } else if(child.lazyOp == 3) {
                    child.lazyOp = 2;
                    child.sum = 0;
                }

            }

            private void propagateLazy() {

                if(lazyOp == 0) return;
                if(lazyOp == 1) {
                    flipChild(leftChild);
                    flipChild(rightChild);
                } else if(lazyOp == 2) {
                    if(leftChild!=null){
                        leftChild.lazyOp = 2;
                        leftChild.sum = 0;
                    }
                    if(rightChild!=null){
                        rightChild.lazyOp = 2;
                        rightChild.sum = 0;
                    }
                } else if(lazyOp == 3) {
                    if(leftChild!=null){
                        leftChild.lazyOp = 3;
                        leftChild.sum = leftChild.right - leftChild.left + 1;
                    }
                    if(rightChild!=null){
                        rightChild.lazyOp = 3;
                        rightChild.sum = rightChild.right - rightChild.left + 1;
                    }
                }
                lazyOp = 0;
            }

            public void set(int l, int r) {
                if(l>right || r<left) return;
                propagateLazy();
                if(left == right) {
                    sum = 1;
                    return;
                }
                if(left>=l && right<=r) {
                    sum = right-left+1;
                    lazyOp = 3;
                    return;
                }
                leftChild.set(l, r);
                rightChild.set(l, r);
                sum = leftChild.sum + rightChild.sum;
            }

            public void flip(int l, int r) {
                if(l>right || r<left) return;
                propagateLazy();
                if(left == right) {
                    sum = 1-sum;
                    return;
                }
                if(left>=l && right<=r) {
                    sum = right-left+1-sum;
                    lazyOp = 1;
                    return;
                }
                leftChild.flip(l, r);
                rightChild.flip(l, r);
                sum = leftChild.sum + rightChild.sum;
            }

            public void clear(int l, int r) {
                if(l>right || r<left) return;
                propagateLazy();
                if(left == right) {
                    sum = 0;
                    return;
                }
                if(left>=l && right<=r) {
                    sum = 0;
                    lazyOp = 2;
                    return;
                }
                leftChild.clear(l, r);
                rightChild.clear(l, r);
                sum = leftChild.sum + rightChild.sum;
            }

            public int getSum(int l, int r) {
                if(l>right || r<left) return 0;
                propagateLazy();
                if(left==right || (left>=l && right<=r)) return sum;
                return leftChild.getSum(l, r) + rightChild.getSum(l, r);
            }

        }



}

