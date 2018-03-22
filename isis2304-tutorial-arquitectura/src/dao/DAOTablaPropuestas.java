package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;
import vos.Propuesta;


public class DAOTablaPropuestas {
	
	public final static String USUARIO = "PARRANDEROS";
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaPropuestas() {
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


	public List<Cliente> darClientes() throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sentencia = "SELECT * FROM CLIENTES FETCH FIRST 100 ROWS ONLY";
		PreparedStatement stamnt = conn.prepareStatement(sentencia);
		recursos.add(stamnt);
		ResultSet rs = stamnt.executeQuery();

		while(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String appelido = rs.getString("APELLIDO"); 
			Integer tipo = rs.getInt("TIPO");
			Cliente cliente = new Cliente(id,name,appelido,tipo,null);
			
	
			clientes.add(cliente);
		}
		return clientes;
	}




	public Cliente darCliente(Long id) throws SQLException {
		Cliente clientePorId = null;

		String sqlClientePorId = "SELECT * FROM CLIENTES WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		PreparedStatement stClientePorId = conn.prepareStatement(sqlClientePorId);
		recursos.add(stClientePorId);
		ResultSet rsClientePorId = stClientePorId.executeQuery();

		if (rsClientePorId.next()) {
			Long id2 = rsClientePorId.getLong("ID");
			String nombreClientePorId = rsClientePorId.getString("NOMBRE");
			String apellidoClientePorId = rsClientePorId.getString("APELLIDO");
			Integer tipo = rsClientePorId.getInt("AFILIACION");
			clientePorId = new Cliente(id2, nombreClientePorId, apellidoClientePorId, tipo, null);

		}

		return clientePorId;
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


	public boolean existePropuestaPorId(Long id) throws SQLException, Exception {
		String sqlPropuestaPorId = "SELECT * FROM PROPUESTAS WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		System.out.println(sqlPropuestaPorId);
		PreparedStatement stOperadorPorId = conn.prepareStatement(sqlPropuestaPorId);
		recursos.add(stOperadorPorId);
		ResultSet rsPropuestaPorId = stOperadorPorId.executeQuery();

		if (rsPropuestaPorId.next()) {
			return true;

		}

		return false;
	}




	public void crearPropuesta(Propuesta propuesta, Long idOperador) throws SQLException, Exception{
		
		String sql = String.format("INSERT INTO PROPUESTAS(ID, ID_OPERADOR, TIPO, NOMBRE) VALUES (%1$s, %2$s, %3$s, '%4$s')",
				propuesta.getId(),
				idOperador,
				propuesta.getTipo(),
				propuesta.getNombre());
System.out.println(sql);
PreparedStatement st = conn.prepareStatement(sql);
st.executeQuery();
	}

}
