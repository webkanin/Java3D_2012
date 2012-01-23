#include <GL/glut.h>

GLUquadricObj* sphere;

void display(void) {
	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glRotatef(0.2, 0.0, 0.0, 1.0);

	gluSphere(sphere, 1.8, 24, 24);
	glutSwapBuffers();
}


void idle(void) {
	glutPostRedisplay();
}

int main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutCreateWindow("Spinning Sphere");
	glutDisplayFunc(display);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-2.0, 2.0, -2.0, 2.0, -2.0, 2.0);
	glClearColor(1.0, 1.0, 1.0, 0.0);
	glColor3f(1.0, 0.5, 0.5);
	sphere = gluNewQuadric();
	gluQuadricDrawStyle(sphere, GLU_LINE);
	glutIdleFunc(idle);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	glutMainLoop();
}