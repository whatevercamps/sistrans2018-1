package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DateFormatter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import vos.Cliente;
import vos.Reserva;


public class DAOTablaReservas {
	
	public final static String USUARIO = "PARRANDEROS";
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaReservas() {
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

	public boolean existeReservaPropuestaCliente(Long idCliente, Long idProp) throws SQLException, Exception{
		String sqlReserva = "SELECT * FROM RESERVAS WHERE ID_CLIENTE = " + idCliente + " AND ID_PROPUESTA = " + idProp + " FETCH FIRST 1 ROWS ONLY"; 
		System.out.println(sqlReserva);
		PreparedStatement stOperadorPorId = conn.prepareStatement(sqlReserva);
		recursos.add(stOperadorPorId);
		ResultSet rsOperadorPorId = stOperadorPorId.executeQuery();

		if (rsOperadorPorId.next()) {
			return true;

		}

		return false;
	}


	public void crearReserva(Long idCliente, Reserva reserva) throws SQLException, Exception {
		
	
		
		String sql = String.format("INSERT INTO RESERVAS(ID, ID_CLIENTE, ID_PROPUESTA, FECHA_INIC, FECHA_FINI) VALUES (%1$s, %2$s, %3$s, TIMESTAMP '%4$s', TIMESTAMP '%4$s')",
				reserva.getId(),
				idCliente, 
				reserva.getPropuesta().getId(),
				reserva.getFechaInicial(),
				reserva.getFechaFinal());
System.out.println(sql);
PreparedStatement st = conn.prepareStatement(sql);
st.executeQuery();
		
	}

}
