package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import vos.Cliente;
import vos.Propuesta;


public class DAOTablaPropuestas {

	public static final int RESERVA = 1;
	public static final int ID_IDOPERADOR = 2;
	public static final int PROPUESTAS_DISPONIBLES_DE_TIPO = 0;
	public static final int PROPUESTA_DISPONIBLE_POR_ID = 3;
	public static final int ID_OPERADOR_ID_PROPUESTA = 4;
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
		String sql = "SELECT * FROM PROPUESTAS LEFT OUTER JOIN RESERVAS ON PROPUESTAS.ID = RESERVAS.ID_PROPUESTA";

		String[] datos;
		switch(filtro) {

		case RESERVA:
			sql +=  " WHERE RESERVAS.ID = " + Integer.parseInt(parametro);
			break;

		case ID_IDOPERADOR:
			datos = parametro.split(",");  
			sql += " WHERE ID_OPERADOR = " + datos[1] + " AND PROPUESTAS.ID = " + datos[0];
			break;

		case PROPUESTA_DISPONIBLE_POR_ID:
			datos = parametro.split(",");
			sql += " WHERE PROPUESTAS.ID = " + datos[0]
					+ " AND (TIPO - " + Propuesta.INHABILITADA +") < 0"
					+ " AND PROPUESTAS.ID NOT IN (SELECT ID_PROPUESTA FROM RESERVAS WHERE (FECHA_INIC <= TO_DATE('" + datos[1]
							+ "', 'yyyy-mm-dd') AND FECHA_FINA >= TO_DATE('" + datos[1] 
									+ "', 'yyyy-mm-dd'))" + " OR (FECHA_FINA >= TO_DATE('" + datos[2]
											+"', 'yyyy-mm-dd') AND FECHA_INIC <= TO_DATE('" + datos[2] 
													+"', 'yyyy-mm-dd'))) FETCH FIRST 1 ROWS ONLY";
			break;
		case PROPUESTAS_DISPONIBLES_DE_TIPO:
			datos = parametro.split(",");
			sql += " WHERE TIPO = " + datos[0]
					+ " AND PROPUESTAS.ID NOT IN (SELECT ID_PROPUESTA FROM RESERVAS WHERE (FECHA_INIC <= TO_DATE('" + datos[1]
							+ "', 'yyyy-mm-dd') AND FECHA_FINA >= TO_DATE('" + datos[1] 
									+ "', 'yyyy-mm-dd'))" + " OR (FECHA_FINA >= TO_DATE('" + datos[2]
											+"', 'yyyy-mm-dd') AND FECHA_INIC <= TO_DATE('" + datos[2] 
													+"', 'yyyy-mm-dd'))) FETCH FIRST 1 ROWS ONLY";
			break;

		case ID_OPERADOR_ID_PROPUESTA: 
			datos = parametro.split(",");
			sql+= " WHERE ID_OPERADOR = " + datos[0]
					+ " AND PROPUESTAS.ID = " + datos[1];
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


	public Long[] saberDiasCancelacionYFechasMillis(Long idReserva)  throws SQLException, Exception {
		String sql = "SELECT DIAS_CANCELACION, FECHA_INIC, FECHA_FINA, FECHA_CREACION FROM PROPUESTAS PR, RESERVAS RE, FACTURAS FA WHERE RE.ID_PROPUESTA = PR.ID" + 
				" AND FA.ID = RE.ID_FACTURA AND RE.ID = " + idReserva;

		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();
		rs.next();
		Long[] ret = new Long[4];

		ret[1] = rs.getDate("FECHA_CREACION").getTime();
		ret[2] = rs.getDate("FECHA_INIC").getTime();
		ret[3] = rs.getDate("FECHA_FINA").getTime();
		ret[0] = ret[1] + TimeUnit.DAYS.toMillis(rs.getInt("DIAS_CANCELACION"));

		return ret;
	}


	public void retirarPropuesta(Long idPropuesta) throws SQLException, Exception  {
		String sql = "DELETE FROM PROPUESTAS WHERE ID = " + idPropuesta;
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();

	}


	public void bloquearOferta(Long idOferta) throws SQLException, Exception  {
		String sql = "UPDATE PROPUESTAS SET TIPO = TIPO + " + Propuesta.INHABILITADA
				+" WHERE ID = " + idOferta; 
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();
	}


	public void desbloquearOferta(Long idOferta) throws SQLException, Exception {
		String sql = "UPDATE PROPUESTAS SET TIPO = TIPO - " + Propuesta.INHABILITADA
				+" WHERE ID = " + idOferta; 
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		st.executeQuery();
		
	}
	
	public ResultSet reqCons2()  throws SQLException, Exception {
		String sql = "SELECT po.id, po.nombre, po.tipo, po.costo, po.DIAS_CANCELACION ,COUNT(*) as cont FROM " + 
				"RESERVAS Re, propuestas po " + 
				"where Re.ID_PROPUESTA=po.ID " + 
				"group by po.id, po.nombre, po.tipo, po.costo, po.DIAS_CANCELACION " + 
				"order BY cont desc fetch first 20 rows only";
		
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();	
		return rs;
	}


	public ResultSet reqCons3() throws SQLException, Exception{
		String sql = "SELECT AA1.ID1 ID_ALOJAMIENTO, (100/AA1.CONTFIN)*AA1.CONTFIN - (100/AA1.CONTFIN)*AA2.CONTFIN TASA FROM " + 
				"(SELECT PR.ID ID1, COALESCE(PR2.CONT, 0) CONTFIN FROM PROPUESTAS PR LEFT OUTER JOIN " + 
				"(SELECT ID_OPERADOR, TIPO, COUNT(PR.ID) CONT FROM PROPUESTAS PR GROUP BY ID_OPERADOR, TIPO) PR2 " + 
				"ON PR.TIPO = PR2.TIPO AND PR.ID_OPERADOR = PR2.ID_OPERADOR) AA1, " + 
				"(SELECT PR.ID ID1, COALESCE(PR2.CONT, 0) CONTFIN FROM PROPUESTAS PR LEFT OUTER JOIN " + 
				"(SELECT ID_OPERADOR, TIPO, COUNT(PR.ID) CONT FROM PROPUESTAS PR, RESERVAS "+ 
				"WHERE ID_PROPUESTA = PR.ID GROUP BY ID_OPERADOR, TIPO) PR2 " + 
				"ON PR.TIPO = PR2.TIPO AND PR.ID_OPERADOR = PR2.ID_OPERADOR) AA2 " + 
				"WHERE AA1.ID1 = AA2.ID1";
		
		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();	
		return rs;
	}

}
