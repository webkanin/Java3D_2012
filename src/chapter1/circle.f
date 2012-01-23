      PROGRAM CIRCLE
C
C  Define error file, Fortran unit number, and workstation type,
C  and workstation ID.
C
      PARAMETER (IERRF=6, LUNIT=2, IWTYPE=1, IWKID=1)
      PARAMETER (ID=121)
      DIMENSION XP(ID),YP(ID)
C
C  Open GKS, open and activate a workstation.
C
      CALL GOPKS (IERRF,IDUM)
      CALL GOPWK (IWKID,LUNIT,IWTYPE)
      CALL GACWK (IWKID)
C
C  Define colors.
C
      CALL GSCR(IWKID,0, 1.0, 1.0, 1.0)
      CALL GSCR(IWKID,1, 1.0, 0.0, 0.0)
C
C  Draw a circle.
C
      X0 = .5
      Y0 = .5
      R  = .3
      JL = 120
      RADINC = 2.*3.1415926/REAL(JL)
      DO 10 J=1,JL+1
      X = X0+R*COS(REAL(J)*RADINC)
      Y = Y0+R*SIN(REAL(J)*RADINC)
      XP(J) = X
      YP(J) = Y
   10 CONTINUE
      CALL GSPLI(1)
      CALL GSPLCI(1)
      CALL GPL(JL+1,XP,YP)
C
C  Deactivate and close the workstation, close GKS.
C
      CALL GDAWK (IWKID)
      CALL GCLWK (IWKID)
      CALL GCLKS
C
      STOP
      END
