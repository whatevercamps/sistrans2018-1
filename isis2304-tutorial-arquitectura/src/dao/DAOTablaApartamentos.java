package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Apartamento;


public class DAOTablaApartamentos {
	
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaApartamentos() {
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




	public void crearApartamento(Apartamento apartamento) throws SQLException, Exception {
		
		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO) VALUES (%1$s, '%2$s', %3$s)",
															apartamento.getId(),
															apartamento.getNombre(),
															apartamento.getTipo());
		
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeQuery();
		
		sql = String.format("INSERT INTO APARTAMENTOS_COMPARTIDOS(ID, TIENE_MUEBLES) VALUES (%1$s, %2$s)",
														apartamento.getId(),
														(apartamento.getMuebles()==true?1:0));

		System.out.println(sql);
		st = conn.prepareStatement(sql);
		st.executeQuery();
	}

}
