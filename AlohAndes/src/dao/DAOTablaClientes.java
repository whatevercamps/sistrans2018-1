package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cliente;
import vos.Factura;
import vos.Reserva;


public class DAOTablaClientes {
	
	public final static String USUARIO = "PARRANDEROS";
	public static final int BUSQUEDA_POR_RESERVA = 0;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaClientes() {
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

	public void setConn(Connection conn) throws SQLException {
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


	public List<Cliente> darClientesPor(int filtro, String parametro) throws SQLException, Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String sql = "SELECT RESERVAS.*, CLIENTES.* FROM CLIENTES, RESERVAS, FACTURAS WHERE CLIENTES.ID = ID_CLIENTE AND ID_FACTURA = FACTURAS.ID";

		switch(filtro) {

		case BUSQUEDA_POR_RESERVA:
			sql += " AND CLIENTES.ID = " + parametro + " FETCH FIRST 1 ROWS ONLY";
			break;
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
			Cliente act = new Cliente();
			act.setCodigo(rs.getLong("ID"));
			act.setNombre(rs.getString("NOMBRE"));
			act.setApellido(rs.getString("APELLIDO"));
			act.setTipo(rs.getInt("AFILIACION"));
			clientes.add(act);

		}
		return clientes;
	}

	public List<Factura> darFacturasCliente(Long idCliente){
		List<Factura> facturas = new ArrayList<Factura>();
		return facturas;
	}

	public Cliente darCliente(Long id) throws SQLException, Exception {
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
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");
	}

}
