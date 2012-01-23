#include <windows.h>
#include <string.h>

LRESULT CALLBACK
MainWndProc (HWND hwnd, UINT nMsg, WPARAM wParam, LPARAM lParam) {
	
	HDC hdc;             /* Device context used for drawing */
	PAINTSTRUCT ps;      /* Paint structure used during drawing */
	RECT rc;             /* Client area rectangle */
	int	cx;				 /* Center x-coordinate */
	int cy;              /* Center y-coordinate */
	int r;               /* Radius of circle */
	
	/* Message processing.*/
	switch (nMsg) {
		
	case WM_DESTROY:
		/* The window is being destroyed, close the application */
		PostQuitMessage (0);
		return 0;
		break;
		
	case WM_PAINT:
		/* The window needs to be redrawn. */
		hdc = BeginPaint (hwnd, &ps);
		GetClientRect (hwnd, &rc);
		
		/* Calculater center and radius */
		cx = (rc.left + rc.right)/2;
		cy = (rc.top + rc.bottom)/2;
		if (rc.bottom - rc.top < rc.right - rc.left)
			r = (rc.bottom - rc.top) / 2 - 20;
		else
			r = (rc.right - rc.left) / 2 - 20;
		
		Ellipse(hdc, cx-r, cy-r, cx+r, cy+r);
		
		EndPaint (hwnd, &ps);
		return 0;
		break;
		
	}
	
	return DefWindowProc (hwnd, nMsg, wParam, lParam);
}


int WINAPI
WinMain (HINSTANCE hInst, HINSTANCE hPrev, LPSTR lpCmd, int nShow) {
	HWND hwndMain;						/* Main window handle */
	MSG msg;							/* Win32 message structure */
	WNDCLASSEX wndclass;				/* Window class structure */
	char* szMainWndClass = "WinCircle";	/* The window class name */
	
	/* Create a window class */	
	/* Initialize the entire structure to zero */
	memset (&wndclass, 0, sizeof(WNDCLASSEX));
	
	/* The class Name */
	wndclass.lpszClassName = szMainWndClass;
	
	/* The size of the structure. */
	wndclass.cbSize = sizeof(WNDCLASSEX);
	
	/* All windows of this class redraw when resized. */
	wndclass.style = CS_HREDRAW | CS_VREDRAW;
	
	/* All windows of this class use the MainWndProc window function. */
	wndclass.lpfnWndProc = MainWndProc;
	
	/* This class is used with the current program instance. */
	wndclass.hInstance = hInst;
	
	/* Use standard application icon and arrow cursor */
	wndclass.hIcon = LoadIcon (NULL, IDI_APPLICATION);
	wndclass.hIconSm = LoadIcon (NULL, IDI_APPLICATION);
	wndclass.hCursor = LoadCursor (NULL, IDC_ARROW);
	
	/* Color the background white */
	wndclass.hbrBackground = (HBRUSH) GetStockObject (WHITE_BRUSH);
	
	/* Register the window class */
	RegisterClassEx (&wndclass);
	
	/* Create a window using the window class */
	hwndMain = CreateWindow (
		szMainWndClass,             /* Class name */
		"Circle",                   /* Caption */
		WS_OVERLAPPEDWINDOW,        /* Style */
		CW_USEDEFAULT,              /* Initial x (use default) */
		CW_USEDEFAULT,              /* Initial y (use default) */
		CW_USEDEFAULT,              /* Initial x size (use default) */
		CW_USEDEFAULT,              /* Initial y size (use default) */
		NULL,                       /* No parent window */
		NULL,                       /* No menu */
		hInst,                      /* This program instance */
		NULL                        /* Creation parameters */
		);
	
    /* Display the window */
	ShowWindow (hwndMain, nShow);
	UpdateWindow (hwndMain);
	
	/* The message loop */
	while (GetMessage (&msg, NULL, 0, 0)) {
		TranslateMessage (&msg);
		DispatchMessage (&msg);
	}
	return msg.wParam;
}
