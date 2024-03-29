public class KdTree {
    private Node root;
    private int size;
    
    private static class Node {
        private final Point2D p;      // the point
        private final boolean v;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        public Node(Point2D p, boolean v) {
            this.p = p;
            this.v = v;
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

    private Node insert(Node x, Point2D p, boolean v) {
        if (x == null) {
            return createNode(p, v);
        }
        int cmp;
        if (v) {
            cmp = Point2D.X_ORDER.compare(x.p, p);
        } else {
            cmp = Point2D.Y_ORDER.compare(x.p, p);
        }
        if (cmp > 0) x.lb = insert(x.lb, p, !x.v);
        else if (cmp < 0) x.rt = insert(x.rt, p, !x.v);
        else {
            if (v) {
                cmp = Point2D.Y_ORDER.compare(x.p, p);
            } else {
                cmp = Point2D.X_ORDER.compare(x.p, p);
            }
            if (cmp != 0) x.rt = insert(x.rt, p, !x.v);
        }
        return x;
    }

    private Node createNode(Point2D p, boolean v) {
        size++;
        return new Node(p, v);
    }

    /**
     * add the point p to the set (if it is not already in the set)
     * @param p
     */
    public void insert(Point2D p) {
        Point2D test = new Point2D(0.192618, 0.9539);
        if (test.equals(p)) {
            int ii = 0;
            ii++;
        }
        root = insert(root, p, true);
    }

    /**
     * @param p
     * @return does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        Point2D test = nearest(p);
        if (test == null) return false;
        return test.equals(p);
//        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean v) {
        Queue<Node> bfs = new Queue<Node>();
        Node node = null;
        bfs.enqueue(root);
        while (!bfs.isEmpty()) {
            node = bfs.dequeue();
            if (node.p.compareTo(p) == 0) {
                return true;
            }
            if (node.lb != null) bfs.enqueue(node.lb);
            if (node.rt != null) bfs.enqueue(node.rt);
        }
        return false;
            /*        if (x == null || p == null) {
            return false;
        }
        int cmp;
        if (v) {
            cmp = Point2D.X_ORDER.compare(x.p, p);
        } else {
            cmp = Point2D.Y_ORDER.compare(x.p, p);
        }
        if (cmp > 0) return contains(x.lb, p, !v);
        else if (cmp < 0) return contains(x.rt, p, !v);
        return true;*/
    }

    /**
     * draw all of the points to standard draw
     */
    public void draw() {
        if (root == null) return;
        class NodeWithLimitations {
            final Node node;
            final double x0;
            final double y0;
            final double x1;
            final double y1;
            NodeWithLimitations(Node node, double x0, double y0, double x1, double y1) {
                this.node = node;
                this.x0 = x0;
                this.y0 = y0;
                this.x1 = x1;
                this.y1 = y1;
            }
            public String toString() {
                return node.p.toString() + "[" + x0 + ", " + y0 + ", " + x1 + ", " + y1 + "]";
            }
        };
        Queue<NodeWithLimitations> bfs = new Queue<NodeWithLimitations>();
        NodeWithLimitations node = null;
        bfs.enqueue(new NodeWithLimitations(root, 0, 0, 1, 1));
        while (!bfs.isEmpty()) {
            node = bfs.dequeue();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.node.p.draw();
            
            StdDraw.setPenRadius(.005);
            if (node.node.v) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.node.p.x(), node.y0, node.node.p.x(), node.y1);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.x0, node.node.p.y(), node.x1, node.node.p.y());
            }
            if (node.node.lb != null) {
                NodeWithLimitations tmp = null;
                if (node.node.v) {
                    tmp = new NodeWithLimitations(node.node.lb, node.x0, node.y0, node.node.p.x(), node.y1);
                } else {
                    tmp = new NodeWithLimitations(node.node.lb, node.x0, node.y0, node.x1, node.node.p.y());
                }
                bfs.enqueue(tmp);
            }
            if (node.node.rt != null) {
                NodeWithLimitations tmp = null;
                if (node.node.v) {
                    tmp = new NodeWithLimitations(node.node.rt, node.node.p.x(), node.y0, node.x1, node.y1);
                } else {
                    tmp = new NodeWithLimitations(node.node.rt, node.x0, node.node.p.y(), node.x1, node.y1);
                }
                bfs.enqueue(tmp);
            }
        }
    }

    /**
     * @param rect
     * @return all points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Bag<Point2D> points = new Bag<Point2D>();
        Queue<Node> bfs = new Queue<Node>();
        Node node = null;
        bfs.enqueue(root);
        while (!bfs.isEmpty()) {
            node = bfs.dequeue();
            if (rect.contains(node.p)) {
                points.add(node.p);
            }
            if (node.v) {
                if (Double.compare(node.p.x(), rect.xmin()) >= 0) {
                    if (node.lb != null) bfs.enqueue(node.lb);
                }
                if (Double.compare(node.p.x(), rect.xmax()) <= 0) {
                    if (node.rt != null) bfs.enqueue(node.rt);
                }
            } else {
                if (Double.compare(node.p.y(), rect.ymin()) >= 0) {
                    if (node.lb != null) bfs.enqueue(node.lb);
                }
                if (Double.compare(node.p.y(), rect.ymax()) <= 0) {
                    if (node.rt != null) bfs.enqueue(node.rt);
                }
            }
        }
        return points;
    }

    /**
     * 
     * @param p
     * @return a nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D p) {
        Point2D nearest = (root == null)?null:root.p;
        Queue<Node> bfs = new Queue<Node>();
        Node node = null;
        bfs.enqueue(root);
        double dist;
        while (!bfs.isEmpty()) {
            node = bfs.dequeue();
            if (nearest.distanceTo(p) > node.p.distanceTo(p)) {
                nearest = node.p;
            }
            if (node.v) {
                dist = node.p.x() - p.x();
            } else {
                dist = node.p.y() - p.y();
            }
            if (Double.compare(node.p.distanceTo(p), Math.abs(dist)) > 0) {
                if (node.lb != null) {
                    bfs.enqueue(node.lb);
                }
                if (node.rt != null) {
                    bfs.enqueue(node.rt);
                }
            } else {
                if (Double.compare(dist, 0) > 0) {
                    if (node.rt != null) {
                        bfs.enqueue(node.rt);
                    }
                } else {
                    if (node.lb != null) {
                        bfs.enqueue(node.lb);
                    }
                }
            } 
        }
        return nearest;
    }
    public static void main(String[] args) {
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        String filename = "kdtree/input200K.txt";
        {
            In in = new In(filename);

            StdDraw.show(0);

            // initialize the two data structures with point from standard input
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
                brute.insert(p);
            }
        }
        {
            In in = new In(filename);

            StdDraw.show(0);

            // initialize the two data structures with point from standard input
            boolean bC = true;
            boolean kC = true;
            double bP;
            double kP;
            Point2D p,d, bPoint, kPoint = null;
            double testTime;
            do {
                p = new Point2D(0.190597, 0.9550);
                d = new Point2D(0.192618, 0.9539);
                bC = brute.contains(d);
                kC = kdtree.contains(d);
                Stopwatch watch  = new Stopwatch();
                bPoint = brute.nearest(d);
                testTime = watch.elapsedTime();
                bP = bPoint.distanceTo(p);
                watch  = new Stopwatch();
                kPoint = kdtree.nearest(d); 
                testTime = watch.elapsedTime();
                kP = kPoint.distanceTo(p);
            } while (Double.compare(bP, kP) == 0);
            int ii = 0;
            ii++;
        }
    }
}