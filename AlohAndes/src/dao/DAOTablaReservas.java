package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	
	public static final int BUSQUEDA_CLIENTE = 1;
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
		PreparedStatement st = conn.prepareStatement(sqlReserva);
		recursos.add(st);
		ResultSet rsOperadorPorId = st.executeQuery();

		if (rsOperadorPorId.next()) {
			return true;

		}

		return false;
	}


	public void crearReserva(Long idCliente, Reserva reserva) throws SQLException, Exception {
		
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = String.format("INSERT INTO RESERVAS(ID, ID_CLIENTE, ID_PROPUESTA, FECHA_INIC, FECHA_FINA) VALUES (%1$s, %2$s, %3$s, TO_DATE('%4$s', 'yyyy-mm-dd'), TO_DATE( '%4$s', 'yyyy-mm-dd'))",
				reserva.getId(),
				idCliente, 
				reserva.getPropuesta().getId(),
				dtf.format(reserva.getFechaInicial()),
				dtf.format(reserva.getFechaFinal()));
System.out.println(sql);
PreparedStatement st = conn.prepareStatement(sql);
recursos.add(st);
st.executeQuery();
		
	}


	public List<Reserva> darReservasPor(int filtro, String parametro) throws SQLException, Exception{
		List<Reserva> reservas = new ArrayList<Reserva>();
		String sql = "SELECT * FROM RESERVAS";
		
		switch(filtro) {

		case BUSQUEDA_CLIENTE:
			sql += String.format(" WHERE ID_CLIENTE = %1$s", parametro);
			break;


		default:
			break;
		}

		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			System.out.println("si hay " + rs.getLong("ID"));
			Reserva act = new Reserva();
			act.setId(rs.getLong("ID"));
			act.setFechaInicial(rs.getDate("FECHA_INIC"));
			act.setFechaFinal(rs.getDate("FECHA_FINA"));
			reservas.add(act);
			
		}
		return reservas;
	}

}
