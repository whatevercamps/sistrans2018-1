package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;
import vos.Propuesta;
import vos.Servicio;


public class DAOTablaServicios {

	public static final int PROPUESTA = 1;

	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaServicios() {
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


	public boolean existeServicioPorId(Long id) throws SQLException, Exception {
		String sqlServicioPorId = "SELECT * FROM SERVICIOS WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		System.out.println(sqlServicioPorId);
		PreparedStatement st = conn.prepareStatement(sqlServicioPorId);
		recursos.add(st);
		ResultSet rsServicioPorId = st.executeQuery();

		if (rsServicioPorId.next()) {
			return true;

		}

		return false;
	}


	public void conectarServicioAPropuesta(Long serId, Long propId) throws SQLException, Exception{
		String sql = String.format("INSERT INTO SERVICIOS_PROPUESTA(ID_SERVICIO, ID_PROPUESTA) VALUES (%1$s, %2$s)",
				serId,
				propId);
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();

	}


	public List<Servicio> darServiciosPor(int filtro, String parametro) throws SQLException, Exception{
		ArrayList<Servicio> servicios = new ArrayList<Servicio>();
		String sql = "SELECT * FROM SERVICIOS, SERVICIOS_PROPUESTA WHERE ID_SERVICIO = ID";
		
		switch(filtro) {

		case PROPUESTA:
			sql +=  " AND ID_PROPUESTA = " + Integer.parseInt(parametro);
			break;

		default:
			break;
		}

		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			Servicio servicio = new Servicio(rs.getString("NOMBRE"), rs.getDouble("COSTO"), rs.getLong("ID"));
			servicios.add(servicio);
		}
		
		return servicios;
	}

}
