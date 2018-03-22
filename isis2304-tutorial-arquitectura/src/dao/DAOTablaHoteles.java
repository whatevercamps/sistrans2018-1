package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Hotel;


public class DAOTablaHoteles {
	
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaHoteles() {
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




	public void crearHotel(Hotel hotel) throws SQLException, Exception {
		
		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO) VALUES (%1$s, '%2$s', %3$s)",
															hotel.getId(),
															hotel.getNombre(),
															hotel.getTipo());
		
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeQuery();
		
		sql = String.format("INSERT INTO HOTELES(ID, COD_INTEND, TAMANIO) VALUES (%1$s, %2$s, %3$s)",
														hotel.getId(),
														hotel.getCodigoSuperIntendecnia(),
														hotel.getTamanio());

		System.out.println(sql);
		st = conn.prepareStatement(sql);
		st.executeQuery();
	}

}
