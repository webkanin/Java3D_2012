
.model small,stdcall
.stack 100h
.386

.data
saveMode BYTE ?	; saved video mode
xc WORD ?		; center x
yc WORD ?		; center y
x SWORD ?		; x coordinate
y SWORD ?		; y coordinate
dE SWORD ?		; east delta
dSE SWORD ?		; southeast delta
w WORD 320		; screen width

.code
main PROC
	mov ax,@data
	mov ds,ax

	;Set Video Mode 320X200
	mov  ah,0Fh	; get current video mode
	int  10h
	mov  saveMode,al	; save mode

	mov ah,0	; set new video mode
	mov al,13h	; mode 13h
	int 10h

	push 0A000h	; video segment address
	pop es      ; ES = A000h (video segment).

	;Set Background
	mov dx,3c8h	; video palette port (3C8h)
	mov al,0	; set palette index
	out dx,al

	;Set screen background color to dark blue.
	mov dx,3c9h	; port address 3C9h
	mov al,0	; red
	out dx,al
	mov al,0	; green
	out dx,al
	mov al,32	; blue (32/63)
	out dx,al

	; Draw Circle
	; Change color at index 1 to yellow (63,63,0)
	mov dx,3c8h	; video palette port (3C8h)
	mov al,1	; set palette index 1
	out dx,al

	mov dx,3c9h	; port address 3C9h
	mov al,63	; red
	out dx,al
	mov al,63	; green
	out dx,al
	mov al,0	; blue
	out dx,al

	mov xc,160	; center of screen
	mov yc,100

	; Calculate coordinates
	mov x, 0
	mov y, 50	; radius 50
	mov bx, -49	; 1-radius
	mov dE, 3
	mov dSE, -95

DRAW:
	call Draw_Pixels	; Draw 8 pixels

	cmp bx, 0	; decide E or SE
	jns MVSE

	add bx, dE	; move east
	add dE, 2
	add dSE, 2
	inc x
	jmp NXT
MVSE:
	add bx, dSE	; move southeast
	add dE, 2
	add dSE, 4
	inc x
	dec y
NXT:
	mov cx, x	; continue if x < y
	cmp cx, y
	jb DRAW

	; Restore Video Mode
	mov ah,10h	; wait for keystroke
	int 16h
	mov ah,0   	; reset video mode
	mov al,saveMode   	; to saved mode
	int 10h

	.EXIT
main ENDP

; Draw 8 pixels symmetrical about the center
Draw_Pixels PROC
	; Calculate the video buffer offset of the pixel.
	mov ax, yc
	add ax, y
	mul w
	add ax, xc
	add ax, x
	mov di, ax
	mov BYTE PTR es:[di],1	; store color index
	; Horizontal symmetrical pixel
	sub di, x
	sub di, x
	mov BYTE PTR es:[di],1	; store color index
	; Vertical symmetrical pixel
	mov ax, yc
	sub ax, y
	mul w
	add ax, xc
	add ax, x
	mov di, ax
	mov BYTE PTR es:[di],1	; store color index
	; Horizontal pixel
	sub di, x
	sub di, x
	mov BYTE PTR es:[di],1	; store color index
	; Switch x, y to get other 4 pixels
	mov ax, yc
	add ax, x
	mul w
	add ax, xc
	add ax, y
	mov di, ax
	mov BYTE PTR es:[di],1	; store color index
	sub di, y
	sub di, y
	mov BYTE PTR es:[di],1	; store color index
	mov ax, yc
	sub ax, x
	mul w
	add ax, xc
	add ax, y
	mov di, ax
	mov BYTE PTR es:[di],1	; store color index
	sub di, y
	sub di, y
	mov BYTE PTR es:[di],1	; store color index

	ret
Draw_Pixels ENDP

END main