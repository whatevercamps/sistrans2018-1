
CREATE TABLE CLIENTES (
ID NUMBER GENERATED by default on null as IDENTITY,
NOMBRE VARCHAR2(40 BYTE) NOT NULL,
APELLIDO VARCHAR2(40 BYTE) NOT NULL,
AFILIACION NUMBER(1,0) NOT NULL, 
CONSTRAINT PK_CLIENTE PRIMARY KEY (ID),
CONSTRAINT CK_AFILIACIONCLIENTE CHECK ( AFILIACION = 1 OR AFILIACION = 2 OR AFILIACION = 3 OR AFILIACION = 4 OR AFILIACION = 5 OR AFILIACION = 6 )
);



