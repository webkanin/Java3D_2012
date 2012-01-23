#include <GL/glut.h>
#include <math.h>

void display(void) {
	int i;
	int n = 80;
	float a = 2*3.1415926535/n;
	float x;
	float y;

	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(1.0,0,0);

	glBegin(GL_LINE_LOOP);
	for (i = 0; i < n; i++) {
		x = cos(i*a);
		y = sin(i*a);
		glVertex2f(x, y);
	}
	glEnd();
	glFlush();
}


int main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutCreateWindow("Circle");
	glutDisplayFunc(display);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(-1.2, 1.2, -1.2, 1.2);
	glClearColor(1.0, 1.0, 1.0, 0.0);
	glutMainLoop();
}