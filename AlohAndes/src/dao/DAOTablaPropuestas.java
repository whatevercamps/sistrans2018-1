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

	public static final int RESERVA = 1;

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

	public List<Propuesta> darPropuestasPor(Integer filtro, String parametro) throws SQLException, Exception{

		ArrayList<Propuesta> propuestas = new ArrayList<Propuesta>();
		String sql = "SELECT * FROM PROPUESTAS, RESERVAS WHERE PROPUESTAS.ID = ID_PROPUESTA";

		switch(filtro) {

		case RESERVA:
			sql +=  " AND RESERVAS.ID = " + Integer.parseInt(parametro);
			break;


		default:
			break;
		}
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			Propuesta propuesta = new Propuesta();
			propuesta.setId(rs.getLong("ID_PROPUESTA"));
			propuesta.setNombre(rs.getString("NOMBRE"));
			propuesta.setCosto(rs.getDouble("COSTO"));
			propuesta.setDiasCancelacion(rs.getInt("DIAS_CANCELACION"));
			propuesta.setTipo(rs.getInt("TIPO"));
			propuestas.add(propuesta);
		}
		System.out.println("tam propuestas filtro=" + filtro + " parametro=" + parametro + ": " + propuestas.size());
		return propuestas;

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

		String sql = String.format("INSERT INTO PROPUESTAS(ID, ID_OPERADOR, TIPO, NOMBRE, COSTO, DIAS_CANCELACION) VALUES (%1$s, %2$s, %3$s, '%4$s', %5$s, %6$s)",
				propuesta.getId(),
				idOperador,
				propuesta.getTipo(),
				propuesta.getNombre(),
				propuesta.getCosto(),
				propuesta.getDiasCancelacion());
		System.out.println(sql);
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		st.executeQuery();
	}


	public Object[] saberDiasCancelacionEInicial(Long idReserva)  throws SQLException, Exception {
		String sql = "SELECT DIAS_CANCELACION, FECHA_INIC FROM PROPUESTA PR, RESERVA RE WHERE RE.ID_PROPUESTA = PR.ID" + 
				" AND RE.ID = " + idReserva;

		PreparedStatement st = conn.prepareStatement("select count(*) cont from facturas");
		recursos.add(st);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();
		rs.next();
		Object[] ret = new Object[2];
		ret[0] = rs.getInt("DIAS_CANCELACION");
		ret[1] = rs.getDate("FECHA_INIC");
		return ret;
	}

}
