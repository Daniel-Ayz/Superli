package delivery.backend.businessLayer.destination;

public class Point {
    private int northCoordinate;
    private int eastCoordinate;

    public Point(int northCoordinate, int eastCoordinate) {
        this.northCoordinate = northCoordinate;
        this.eastCoordinate = eastCoordinate;
    }

    public int getNorthCoordinate(){ return this.northCoordinate; }

    public int getEastCoordinate() { return this.eastCoordinate; }

    public int euclideanDistance(Point other) {
        return (int) Math.sqrt(Math.pow(this.northCoordinate - other.northCoordinate, 2) + Math.pow(this.eastCoordinate - other.eastCoordinate, 2));
    }

}
