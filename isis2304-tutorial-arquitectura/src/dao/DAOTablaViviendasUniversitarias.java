package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import vos.ViviendaUniversitaria;

public class DAOTablaViviendasUniversitarias {
	///dao vivienda universittaria 





	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaViviendasUniversitarias() 
	{
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



	public void crearViviendaUni(ViviendaUniversitaria cliente) throws SQLException, Exception 
	{

		String sql = String.format("INSERT INTO OPERADORES(ID, NOMBRE, TIPO) VALUES (%1$s, '%2$s', %3$s)",
				cliente.getId(),
				cliente.getNombre(),
				cliente.getTipo());

		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		st.executeQuery();


		String sql2 = String.format("INSERT INTO VIVIENDA_UNI(ID, NOMBRE, APELLIDO, AFILIACION) VALUES ('%1$s', '%2$s', '%3$s', '%4$s','%5$s')",
				cliente.getId(),
				cliente.getServicioSalaDeEstudioPrecioAdicional(),
				cliente.getServicioGymPrecioAdicional(),
				cliente.getSalaDeEsparcimientoPrecioAdicional(),
				cliente.getRestaurantePrecioAdicional());
		System.out.println(sql2);
		PreparedStatement st2 = conn.prepareStatement(sql2);
		st2.executeQuery();
	}
}
