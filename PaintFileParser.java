package ca.utoronto.utm.paint;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 * rashid78
 *
 */

/*
instead of string tracker use command, update it as it goes along
final state-if there are words after end-file, incorrect
if color>255
*/


public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel; 
	private String tracker;
	
	/**
	 * Below are Patterns used in parsing 
	 */
	Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");
	
	Pattern pColor=Pattern.compile("^color:(\\d{1,3}),(\\d{1,3}),(\\d{1,3})$");
	Pattern pFilled=Pattern.compile("^filled:(true|false)$");
	
	Pattern pCircleStart=Pattern.compile("^Circle$");
	Pattern pCircleCenter=Pattern.compile("^center:[(]\\d{1,3},\\d{1,3}[)]$");
	Pattern pCircleRadius=Pattern.compile("^radius:\\d{1,3}$");
	Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	
	Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	Pattern pRectangleP1=Pattern.compile("^p1:[(]\\d{1,3},\\d{1,3}[)]$");
	Pattern pRectangleP2=Pattern.compile("^p2:[(]\\d{1,3},\\d{1,3}[)]$");
	Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	
	
	Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	Pattern pSquigglePoints= Pattern.compile("^points$");
	Pattern pSquigglePoint=Pattern.compile("^point:[(]\\d{1,3},\\d{1,3}[)]$");
	Pattern pSquiggleEndPoints=Pattern.compile("^endpoints$");
	Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");
	
	
	// ADD MORE!!
	//private Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	//private Pattern pRectangleEnd=Pattern.compile("^EndCircle$");
	
	//private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	//private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle");
	
	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}
	
	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";
		
		// During the parse, we will be building one of the 
		// following commands. As we parse the file, we modify 
		// the appropriate command.
		
		CircleCommand circleCommand = null; 
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;
	
		try {	
			int state=0; Matcher m; Matcher m2; Matcher n; Matcher o; Matcher e; String l;
			
			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {
				this.lineNumber++;
				//l=l.replaceAll("\\r\\n", "");
				/*if(l=="") {
					this.lineNumber++;
				}*/
				l=l.replaceAll("\\s", "");
				//l=l.replaceAll("\\n", "");
				System.out.println(lineNumber+" "+l+" "+state);
				if(l.length()>0) {
					switch(state){
						case 0:
							m=pFileStart.matcher(l);
							if(m.matches()){
								state=1;
								break;
							}
							error("Expected Start of Paint Save File");
							return false;
						case 1: // Looking for the start of a new object or end of the save file
							m=pCircleStart.matcher(l);
							n=pRectangleStart.matcher(l);
							o=pSquiggleStart.matcher(l);
							e=pFileEnd.matcher(l);
							if(m.matches() || n.matches() || o.matches() || e.matches()){
								// ADD CODE!!!
								// create shape object, use to check later what shape it is
								if(m.matches()) {
									//Point p = new Point(0,0);
									//circleCommand = new CircleCommand(p, 0);
									//Circle t = new Circle();
									tracker = "circle";
								} else if (n.matches()) {
									//Point p1 = new Point(0,0);
									//Point p2 = new Point(0,0);
									//rectangleCommand = new RectangleCommand(p1, p2);
									//Rectangle t = new Rectangle();
									tracker= "rectangle";
								} else if (o.matches()) {
									//squiggleCommand = new SquiggleCommand();
									//Squiggle t = new Squiggle();
									tracker="squiggle";
								} else if (e.matches()) {
									state=11;
									break;
								}
								state=2; 
								break;
							}
							error("Expected start of a new object");
							return false;
						case 2: // case for color
							// ADD CODE
							m=pColor.matcher(l);
							if(m.matches()) {
								state=3;
								break;
							}
							error("Expected a color");
							return false;
							//break;
						case 3: //case for filled
							m=pFilled.matcher(l);
							if(m.matches()) {
								state=4;
								break;
							}
							error("Expected filled status");
							return false;
							//break;
						// ...
						case 4: // check which shape you're in, check if its center or p1 or points
							//if (s.) // if in circle, go to center, go to state 5 (radius)
							// if in rectangle, go to p1 and go to state 7 (p2)
							// if in squiggle, go to points and go to state 6 (point)
							if(tracker == "circle") {
								m=pCircleCenter.matcher(l);
								if(m.matches()) {
									state=5;
									break;
								}
								error("Expected center of Circle");
								return false;
							} else if (tracker == "rectangle") {
								m=pRectangleP1.matcher(l);
								if(m.matches()) {
									state=6;
									break;
								}
								error("Expected p1 of Rectangle");
								return false;
							} else if (tracker == "squiggle") {
								m=pSquigglePoints.matcher(l);
								if(m.matches()) {
									state=7;
									break;
								}
								error("Expected start points of Squiggle");
								return false;
							}
							//break;
						case 5: // radius for circle 
							m=pCircleRadius.matcher(l);
							if(m.matches()) {
								state=8;
								break;
							}
							error("Expected circle radius");
							return false;
							//break;
						case 6: // p2
							m=pRectangleP2.matcher(l);
							if(m.matches()) {
								state=9;
								break;
							}
							error("Expected p2 of Rectangle");
							return false;
							//break;
						case 7: // points (stay in state 6 if more points)
							m=pSquigglePoint.matcher(l);
							m2=pSquiggleEndPoints.matcher(l);
							if(m.matches()) {
								state=7;
								break;
							}
							else if(m2.matches()) {
								state=10;
								break;
							}
							error("Expected a point or end of points for squiggle");
							return false;
							//break;
						case 8:
							m=pCircleEnd.matcher(l);
							if(m.matches()) {
								state=1;
								break;
							}
							error("Expected end of circle");
							return false;
						case 9:
							m=pRectangleEnd.matcher(l);
							if(m.matches()) {
								state=1;
								break;
							}
							error("Expected end of rectangle");
							return false;
						case 10:
							m=pSquiggleEnd.matcher(l);
							if(m.matches()) {
								state=1;
								break;
							}
							error("Expected end of squiggle");
							return false;
						case 11:
							/*m=pFileEnd.matcher(l);
							if(m.matches()) {
								break;
							}
							error("Expected end of file");
							return false;*/
							break;
					}
				} else {
					this.lineNumber++;
				}
			}
		}  catch (Exception e){
			
		}
		return true;
	}
}
