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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import vos.Cliente;
import vos.Reserva;


public class DAOTablaReservas {
	public static final int BUSQUEDA_PROPUESTA = 0;
	public static final int BUSQUEDA_CLIENTE = 1;
	public static final int BUSQUEDA_ID = 2;
	public static final int BUSQUEDA_CLIENTE_ID_RESERVA_ID = 3;

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

	public boolean existeReservaPropuestaCliente(Long idCliente, Long idProp, Date fechaInic, Date fechaFinal) throws SQLException, Exception{
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		String sqlReserva = String.format("SELECT RE.* FROM RESERVAS RE, FACTURAS FA WHERE RE.ID_FACTURA = FA.ID AND "
				+ "ID_CLIENTE = " + idCliente + " AND ID_PROPUESTA = " + idProp 
				+ " AND FECHA_FINA >= TO_DATE('%1$s', 'yyyy-mm-dd') "
				+ " AND FECHA_INIC <= TO_DATE('%2$s', 'yyyy-mm-dd') FETCH FIRST 1 ROWS ONLY", dtf.format(fechaInic), dtf.format(fechaFinal)); 
		System.out.println(sqlReserva);
		PreparedStatement st = conn.prepareStatement(sqlReserva);
		recursos.add(st);
		ResultSet rsOperadorPorId = st.executeQuery();

		if (rsOperadorPorId.next()) {
			return true;

		}

		return false;
	}


	public void crearReserva(Long idFactura, Reserva reserva) throws SQLException, Exception {

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = String.format("INSERT INTO RESERVAS(ID, ID_FACTURA, ID_PROPUESTA, FECHA_INIC, FECHA_FINA) VALUES (%1$s, %2$s, %3$s, TO_DATE('%4$s', 'yyyy-mm-dd'), TO_DATE( '%4$s', 'yyyy-mm-dd'))",
				reserva.getId(),
				idFactura, 
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
		String sql = "SELECT RE.* FROM RESERVAS RE, FACTURAS FA WHERE RE.ID_FACTURA = FA.ID";

		switch(filtro) {

		case BUSQUEDA_CLIENTE:
			sql += String.format(" AND ID_CLIENTE = %1$s", parametro);
			break;
		
		case BUSQUEDA_ID:
			sql += String.format(" AND RE.ID = %1$s", parametro);
			break;

		case BUSQUEDA_CLIENTE_ID_RESERVA_ID:
			String[] datos = parametro.split(",");
			sql += String.format(" AND ID_CLIENTE = %1$s", datos[0]);
			sql += String.format(" AND RE.ID = %1$s", datos[1]);
			
		case BUSQUEDA_PROPUESTA:
			sql += " AND ID_PROPUESTA = " + parametro;
		default:
			break;
		}


		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println("Filtro: " + filtro + ", paramatro: " + parametro);
		System.out.println(sql);
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


	public void eliminarReserva(Long id)  throws SQLException, Exception{
		String sql = "DELETE FROM RESERVAS WHERE ID = " + id;
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();
	}

}
