package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand {
	private ArrayList<Point> points=new ArrayList<Point>();
	
	public void add(Point p){ 
		this.points.add(p); 
		this.setChanged();
		this.notifyObservers();
	}
	public ArrayList<Point> getPoints(){ return this.points; }
	
	
	@Override
	public void execute(GraphicsContext g) {
		ArrayList<Point> points = this.getPoints();
		g.setStroke(this.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		
	}
	@Override
	protected String output() {
		int Red = (int) (255*this.getColor().getRed());
		int Blue = (int) (255*this.getColor().getBlue());
		int Green = (int) (255*this.getColor().getGreen());
		
		String shape = "Squiggle\r\n";
		
		shape += "\tcolor:" + Red + "," + Green + "," + Blue + "\r\n";
		shape += "\tfilled:" + this.isFill() + "\r\n";
		shape += "\tpoints\r\n";
		for(int i=0; i<points.size();i++){
			Point point = points.get(i);
			shape += "\t\tpoint:(" + point.x + "," + point.y + ")\r\n";
		}
		shape += "\tend points\r\n";
		//shape += "\tcenter:(" + this.centre.x + "," + this.centre.y + ")\r\n";
		//shape += "\tradius:" + this.getRadius() + "\r\n";
		
		shape += "End Squiggle" + "\r\n";
		return shape;
	}
}
