package kmucs.capstone.furnidiy;

public class Vertex {
	
	private float x,y,z;
	private float R,G,B,alpha;
	
	public Vertex() {
		x=0;
		y=0;
		z=0;
		R=0;
		G=0;
		B=0;
		alpha=0;
		
	}
	public void setX(float x) {
		this.x += x;
	}
	public void setY(float y) {
		this.y += y;
	}
	public void setZ(float z) {
		this.z += z;
	}
	public void setX2(float x) {
		this.x = x;
	}
	public void setY2(float y) {
		this.y = y;
	}
	public void setZ2(float z) {
		this.z = z;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public void setR(float r) {
		R = r;
	}
	public void setG(float g) {
		G = g;
	}
	public void setB(float b) {
		B = b;
	}
	public float getR() {
		return R;
	}
	public float getG() {
		return G;
	}
	public float getB() {
		return B;
	}
	public float getAlpha() {
		return alpha;
	}
}
