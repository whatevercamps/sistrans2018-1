package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Vecino;
import vos.Vecino;


public class DAOTablaVecinos {

	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaVecinos() {
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




	public void crearVecino(Vecino vecino) throws SQLException, Exception {

		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO, MIN_TIEMPO_DIAS, CAPACIDAD) VALUES (%1$s, '%2$s', %3$s, %4$s, %5$s)",
				vecino.getId(),
				vecino.getNombre(),
				vecino.getTipo(),
				vecino.getMinimoDeTiempo(),
				vecino.getCapacidad());

		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();

		sql = String.format("INSERT INTO VECINOS(ID,NUM_HABITACIONES,UBICACION,CARACT_SEGURO) VALUES (%1$s, %2$s,'%$3s','%$4s')",
				vecino.getId(),
				vecino.getNumeroDeHabitaciones(),
				vecino.getUbicacion(),vecino.getCaracteristicasDelSeguro());

		System.out.println(sql);
		st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();
	}

}
