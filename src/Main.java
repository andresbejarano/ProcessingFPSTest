import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet {
	
	private static final long serialVersionUID = 1L;

	private int world[][] = {
			{5,1,2,1,2,1,2,1,2,1,2,3,3,2,1,2,1,2,1,2,1,2,1,5},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,4,5,6,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,1},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {2,0,0,0,0,0,0,0,0,0,0,3,2,1,0,0,0,0,0,0,1,0,0,2},
            {3,0,0,0,0,0,0,0,0,0,0,3,2,1,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,3,2,1,0,0,0,0,0,0,0,0,0,3},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,1},
            {2,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {5,1,2,1,2,1,2,1,2,1,2,3,3,2,1,2,1,2,1,2,1,2,1,5}};
	
	private int unit = 30;				//Unidad basica
	private int halfUnit = unit / 2;	//Mitad de la unidad basica
	private int boxSize = unit - 5;		//Tamano de los cubos
	
	private PVector eye;		//Posicion (X, Y, Z) del punto donde se encuentra la camara
	private PVector center;		//Posicion (X, Y, Z) del punto que observa la camara
	private PVector up;			//Vector del eje vertical de la camara
	
	private float halfWidth;	//Mitad de la anchura de la ventana
	private float halfHeight;	//Mitad de la altura de la ventana
	
	private int cameraStep = 5;				//Cantidad de desplazamiento
	private float yawStep = HALF_PI / 30;	//Pasos de rotacion
	
	//Variables para la vision en perspectiva
	private float fovy = PI / 3.0f;
	private float aspect;
	private float cameraZ;
	private float zNear;
	private float zFar;
	
	/**
	 * 
	 */
	public void setup() {
		size(800, 600, P3D);
		eye = new PVector(50, 200 - halfUnit, 50);
		center = new PVector(200, 200 - halfUnit, 200);
		up = new PVector(0, 1, 0);
		halfWidth = width / 2.0f;
		halfHeight = height / 2.0f;
		aspect = width / height;
		cameraZ = halfHeight / tan(PI * 60.0f / 360.0f);
		zNear = cameraZ / 10.0f;
		zFar = cameraZ * 10.0f;
		textFont(createFont("Consolas", 30, true));
		textAlign(CENTER);
		noCursor();
	}
	
	/**
	 * 
	 */
	public void draw() {
		
		yawRotation(map(mouseX, 0, width, -yawStep, yawStep));
		
		//Posicionamiento de la camara en la posicion en la que se encuentra el jugador
		perspective(fovy, aspect, zNear, zFar);
		camera(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
		
		//Dibujo del escenario
		background(255);
		stroke(0);
		strokeWeight(1);
		for(int i = 0; i < world.length; i += 1) {
			for(int j = 0; j < world[i].length; j += 1) {
				for(int k = 0; k < world[i][j]; k += 1) {
					pushMatrix();
					translate(i * unit, 200 - k * unit, j * unit);
					fill(0xffff0000);
					box(boxSize);
					popMatrix();
				}
			}
		}
		
		//Posicionamiento de la camara en su posicion normal con respecto a la ventana para el dibujo del HUD
		ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, eye.z - 1, eye.z - 2);
		camera(halfWidth, halfHeight, cameraZ, halfWidth, halfHeight, 0, 0, 1, 0);
		
		//Dibujo del cursor
		noFill();
		stroke(0, 0, 255);
		strokeWeight(2);
		ellipse(mouseX, mouseY, 30, 30);
		ellipse(mouseX, mouseY, 60, 60);
		line(mouseX - 60, mouseY, mouseX + 60, mouseY);
		line(mouseX, mouseY - 60, mouseX, mouseY + 60);
		
		fill(0);
		text("(" + eye.x + ", " + eye.y + ", " + eye.z + ")", halfWidth - 1, 35);
		text("(" + eye.x + ", " + eye.y + ", " + eye.z + ")", halfWidth + 1, 35);
		text("(" + eye.x + ", " + eye.y + ", " + eye.z + ")", halfWidth, 34);
		text("(" + eye.x + ", " + eye.y + ", " + eye.z + ")", halfWidth, 36);
		fill(255);
		text("(" + eye.x + ", " + eye.y + ", " + eye.z + ")", halfWidth, 35);
		
	}
	
	/**
	 * 
	 */
	public void keyPressed() {
		if(key == CODED) {
			if(keyCode == UP) {
				PVector move = new PVector(center.x - eye.x, center.y - eye.y, center.z - eye.z);
				move.normalize();
				move.mult(cameraStep);
				eye.x += move.x;
				eye.y += move.y;
				eye.z += move.z;
				center.x += move.x;
				center.y += move.y;
				center.z += move.z;
			}
			else if(keyCode == DOWN) {
				PVector move = new PVector(center.x - eye.x, center.y - eye.y, center.z - eye.z);
				move.normalize();
				move.mult(-cameraStep);
				eye.x += move.x;
				eye.y += move.y;
				eye.z += move.z;
				center.x += move.x;
				center.y += move.y;
				center.z += move.z;
			}
			else if(keyCode == RIGHT) {
				PVector move = new PVector(center.x - eye.x, center.y - eye.y, center.z - eye.z);
				move = move.cross(up);
				move.normalize();
				move.mult(cameraStep);
				eye.x += move.x;
				eye.y += move.y;
				eye.z += move.z;
				center.x += move.x;
				center.y += move.y;
				center.z += move.z;
			}
			else if(keyCode == LEFT) {
				PVector move = new PVector(center.x - eye.x, center.y - eye.y, center.z - eye.z);
				move = move.cross(up);
				move.normalize();
				move.mult(-cameraStep);
				eye.x += move.x;
				eye.y += move.y;
				eye.z += move.z;
				center.x += move.x;
				center.y += move.y;
				center.z += move.z;
			}
		}
	}
	
	/**
	 * 
	 * @param theta
	 */
	public void yawRotation(float theta) {
		float nX = eye.x + (center.x - eye.x) * PApplet.cos(theta) - (center.z - eye.z) * PApplet.sin(theta);
		float nZ = eye.z + (center.x - eye.x) * PApplet.sin(theta) + (center.z - eye.z) * PApplet.cos(theta);
		center.x = nX;
		center.z = nZ;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		PApplet.main(new String[] {"Main"});
	}

}