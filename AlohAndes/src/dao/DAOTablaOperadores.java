package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Apartamento;
import vos.Operador;
import vos.Reserva;


public class DAOTablaOperadores {
	
	public static final int PROPUESTA = 1;

	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaOperadores() {
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
	
	public List<Operador> darOperadoresPor(int filtro, String parametro) throws SQLException, Exception {
		List<Operador> operadores = new ArrayList<Operador>();
		String sql = "SELECT OP.* FROM OPERADORES OP, PROPUESTAS PR WHERE PR.ID_OPERADOR = OP.ID";

		switch(filtro) {

		case PROPUESTA:
			sql += String.format(" AND PR.ID = %1$s", parametro);
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
			Operador act = new Operador();
			act.setId(rs.getLong("ID"));
			act.setNombre(rs.getString("NOMBRE"));
			act.setCapacidad(rs.getDouble("CAPACIDAD"));
			act.setTipo(rs.getInt("TIPO"));
			act.setMinimoDeTiempo(rs.getInt("MIN_TIEMPO_DIAS"));
			operadores.add(act);

		}
		return operadores;
		
	}




	public boolean existeOperadorPorId(Long id) throws SQLException {

		String sqlOperadorPorId = "SELECT * FROM OPERADORES WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		System.out.println(sqlOperadorPorId);
		PreparedStatement st = conn.prepareStatement(sqlOperadorPorId);
		recursos.add(st);
		ResultSet rsOperadorPorId = st.executeQuery();

		if (rsOperadorPorId.next()) {
			return true;

		}

		return false;
	}

}
