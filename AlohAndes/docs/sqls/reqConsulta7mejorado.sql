select  PAGADO, 
To_Number(to_char(
    (SELECT FECHA_CREACION FROM FACTURAS ORDER BY FECHA_CREACION FETCH FIRST 1 ROWS ONLY),'YY')) 
- To_number(to_char(FECHA_CREACION,'YY')) + 1 HOLA from FACTURAS;
SELECT FECHA_CREACION 
FROM FACTURAS 
ORDER BY FECHA_CREACION 
FETCH FIRST 1 ROWS ONLY;