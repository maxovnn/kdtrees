public class PointSET {
    
    private SET<Point2D> mPoints;
    /*
     * construct an empty set of points
     */
    public PointSET() {
        mPoints = new SET<Point2D>();
    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return mPoints.isEmpty();
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return mPoints.size();
    }

    /**
     * add the point p to the set (if it is not already in the set)
     * @param p
     */
    public void insert(Point2D p) {
        mPoints.add(p);
    }

    /**
     * @param p
     * @return does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        return mPoints.contains(p);
    }

    /**
     * draw all of the points to standard draw
     */
    public void draw() {
        for (Point2D point: mPoints) {
            point.draw();
        }
    }

    /**
     * @param rect
     * @return all points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Bag<Point2D> bag = new Bag<Point2D>();
        for (Point2D point: mPoints) {
            if (rect.contains(point)) {
                bag.add(point);
            }
        }
        return bag;
    }

    /**
     * 
     * @param p
     * @return a nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D p) {
        if (mPoints.isEmpty()) return null;
        Point2D minPoint = null;
        double min = Integer.MAX_VALUE;
        double current = Integer.MIN_VALUE;
        for (Point2D point: mPoints) {
            current = point.distanceTo(p);
            if (Double.compare(min, current) > 0) {
                minPoint = point;
                min = current;
            }
        }
        return minPoint;
    }
}