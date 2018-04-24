package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vos.Apartamento;
import vos.Reserva;


public class DAOTablaFacturas {
	
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaFacturas() {
		recursos = new ArrayList<Object>();
	}


	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}




	public void crearFactura(Apartamento apartamento) throws SQLException, Exception {
		
		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO, MIN_TIEMPO_DIAS, CAPACIDAD) VALUES (%1$s, '%2$s', %3$s, %4$s, %5$s)",
															apartamento.getId(),
															apartamento.getNombre(),
															apartamento.getTipo(),
															apartamento.getMinimoDeTiempo(),
															apartamento.getCapacidad());
		
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();
		
		sql = String.format("INSERT INTO APARTAMENTOS_COMPARTIDOS(ID, TIENE_MUEBLES) VALUES (%1$s, %2$s)",
														apartamento.getId(),
														(apartamento.getMuebles()==true?1:0));

		System.out.println(sql);
		st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();
	}


	public Long crearFactura(Long idCliente, String hoy)  throws SQLException, Exception{
		PreparedStatement st = conn.prepareStatement("select count(*) cont from facturas");
		recursos.add(st);
		ResultSet rs = st.executeQuery();
		rs.next();
		Long index = rs.getLong("CONT") + 1;

		
		String sql = "INSERT INTO FACTURAS(ID_CLIENTE, ID, FECHA_CREACION) VALUES (";
		sql += idCliente + ", " + index + ", TO_DATE('" + hoy + "', 'yyyy-mm-dd'))";
		st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();
		return index;
		
	}


	public Double abonarFactura(Long idReserva, Double cantPago) throws SQLException, Exception {		
		
		
		String sql = "SELECT ID_FACTURA FROM RESERVAS WHERE ID = " + idReserva;
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();
		rs.next();
		
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		Date hoy = new Date();
		
		sql = "UPDATE FACTURAS SET PAGADO = PAGADO + " + cantPago + ", FECHA_ULTIMO_PAGO = TO_DATE('"
				+ dtf.format(hoy) +  "', 'yyyy-mm-dd') WHERE ID =" + rs.getLong("ID_FACTURA");
		st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();
		
		sql = "SELECT FA.* FROM RESERVAS RE, FACTURAS FA WHERE RE.ID_FACTURA = FA.ID AND RE.ID = " + idReserva;
		st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		rs = st.executeQuery();
		rs.next();
		
		return rs.getDouble("COSTO_TOTAL") - rs.getDouble("PAGADO");
		
	}


	public void actualizarPago(Double precioActual, Long id) throws SQLException, Exception {
		
		
		String sql = "UPDATE FACTURAS SET COSTO_TOTAL = COSTO_TOTAL + " + precioActual;
		sql += " WHERE ID IN (SELECT FA.ID FROM FACTURAS FA, RESERVAS RE WHERE RE.ID_FACTURA = FA.ID AND RE.ID = " + id + ")";
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();	
	}

}
