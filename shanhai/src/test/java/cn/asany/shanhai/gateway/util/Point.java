package cn.asany.shanhai.gateway.util;

/** @author shenchao 数据点 */
public class Point {

  private double X;
  private double Y;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getX() {
    return X;
  }

  public void setX(double x) {
    X = x;
  }

  public double getY() {
    return Y;
  }

  public void setY(double y) {
    Y = y;
  }

  @Override
  public boolean equals(Object obj) {
    Point point = (Point) obj;
    if (this.getX() == point.getX() && this.getY() == point.getY()) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + X + "," + Y + ")";
  }

  @Override
  public int hashCode() {
    return (int) (X + Y);
  }
}
