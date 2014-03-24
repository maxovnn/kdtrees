public class KdTree {
    private Node root;
    private int size; 
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        public Node(Point2D p, RectHV rect, boolean h) {
            this.p = p;
            rect = new RectHV();
        }
    }

    /*
     * construct an empty set of points
     */
    public KdTree() {
        
    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * add the point p to the set (if it is not already in the set)
     * @param p
     */
    public void insert(Point2D p) {
        if (root == null) {
            root = new Node();
        }
    }

    private Node getTreeParent(Point2D p) {
        if (root == null) return null;
        Node node = root;
        Node parent = null;
        while (node != null) {
            if (node.rect.contains(p)) {
                if (node.lb.p == p) {
                    return node;
                }
                node = node.lb;
            } else {
                node = node.rt;
            }
        }
        return null;
    }

    /**
     * @param p
     * @return does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        Node node = root;
        while (node != null) {
            if (node.p == p) {
                return true;
            }
            if (node.rect.contains(p)) {
                node = node.lb;
            } else {
                node = node.rt;
            }
        }
        return false;
    }

    /**
     * draw all of the points to standard draw
     */
    public void draw() {
        
    }

    /**
     * @param rect
     * @return all points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        
    }

    /**
     * 
     * @param p
     * @return a nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D p) {
        
    }
    
    public static void main(String[] args){
    }

}