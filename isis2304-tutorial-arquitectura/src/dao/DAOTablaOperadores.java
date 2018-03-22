package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Apartamento;
import vos.Operador;


public class DAOTablaOperadores {
	
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




	public boolean existeOperadorPorId(Long id) throws SQLException {

		String sqlOperadorPorId = "SELECT * FROM OPERADORES WHERE ID = " + id + " FETCH FIRST 1 ROWS ONLY"; 
		PreparedStatement stOperadorPorId = conn.prepareStatement(sqlOperadorPorId);
		recursos.add(stOperadorPorId);
		ResultSet rsOperadorPorId = stOperadorPorId.executeQuery();

		if (rsOperadorPorId.next()) {
			return true;

		}

		return false;
	}

}
