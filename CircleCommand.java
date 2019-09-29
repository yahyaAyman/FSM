package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;

public class CircleCommand extends PaintCommand {
	private Point centre;
	private int radius;
	
	public CircleCommand(Point centre, int radius){
		this.centre = centre;
		this.radius = radius;
	}
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) { 
		this.centre = centre; 
		this.setChanged();
		this.notifyObservers();
	}
	public int getRadius() { return radius; }
	public void setRadius(int radius) { 
		this.radius = radius; 
		this.setChanged();
		this.notifyObservers();
	}
	public void execute(GraphicsContext g){
		int x = this.getCentre().x;
		int y = this.getCentre().y;
		int radius = this.getRadius();
		if(this.isFill()){
			g.setFill(this.getColor());
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
			g.setStroke(this.getColor());
			g.strokeOval(x-radius, y-radius, 2*radius, 2*radius);
		}
	}
	// change name
	@Override
	public String output() {
		int Red = (int) (255*this.getColor().getRed());
		int Blue = (int) (255*this.getColor().getBlue());
		int Green = (int) (255*this.getColor().getGreen());
		
		String shape = "Circle\r\n";
		
		shape += "\tcolor:" + Red + "," + Green + "," + Blue + "\r\n";
		shape += "\tfilled:" + this.isFill() + "\r\n";
		shape += "\tcenter:(" + this.centre.x + "," + this.centre.y + ")\r\n";
		shape += "\tradius:" + this.getRadius() + "\r\n";
		shape += "End Circle" + "\r\n";
		return shape;
	}
}
