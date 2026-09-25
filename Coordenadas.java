package package_00;

public class Coordenadas {
	
	private double x;
	private double y;
	
	public Coordenadas(double x, double y) {
		setX(x);
		setY(y);
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
