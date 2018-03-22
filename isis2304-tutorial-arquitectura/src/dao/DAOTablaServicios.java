package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;


public class DAOTablaServicios {
	
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


	



	

	public void crearCliente(Cliente cliente) throws SQLException, Exception {
		
		String sql = String.format("INSERT INTO CLIENTES(ID, NOMBRE, APELLIDO, AFILIACION) VALUES ('%1$s', '%2$s', '%3$s', '%4$s')",
														cliente.getCodigo(),
														cliente.getNombre(),
														cliente.getApellido(),
														cliente.getTipo());
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeQuery();
	}


	public boolean existeServicioPorId(Long id) throws SQLException, Exception {
		String sqlServicioPorId = "SELECT * FROM SERVICIOS WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		System.out.println(sqlServicioPorId);
		PreparedStatement stOperadorPorId = conn.prepareStatement(sqlServicioPorId);
		recursos.add(stOperadorPorId);
		ResultSet rsServicioPorId = stOperadorPorId.executeQuery();

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
st.executeQuery();
		
	}

}
