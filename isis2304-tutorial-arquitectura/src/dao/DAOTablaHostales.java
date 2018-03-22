package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import vos.Hostal;
import vos.Hostal;


public class DAOTablaHostales {
	
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaHostales() {
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




	public void crearHostal(Hostal hostal) throws SQLException, Exception {
		
		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO, MIN_TIEMPO_DIAS, CAPACIDAD) VALUES (%1$s, '%2$s', %3$s, %4$s, %5$s)",
				hostal.getId(),
				hostal.getNombre(),
				hostal.getTipo(),
				hostal.getMinDeTiempo(),
				hostal.getCapacidad());
		
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeQuery();
			
		
		sql = String.format("INSERT INTO HOSTALES(ID,COD_INTEND, APERTURA, CLAUSURA) VALUES (%1$s, %2$s, '%3$s', '%4$s')",
														hostal.getId(),
														hostal.getCodIntendencia(),
														hostal.getHoraApertura(),
														hostal.getHoraClausura());

		System.out.println(sql);
		st = conn.prepareStatement(sql);
		st.executeQuery();
	}

}
