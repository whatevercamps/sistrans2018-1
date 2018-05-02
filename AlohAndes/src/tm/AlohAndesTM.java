/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: RotondAndes 
 * Autor: David Bauista - dj.bautista10@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Properties;


import dao.DAOTablaApartamentos;
import dao.DAOTablaClientes;
import dao.DAOTablaFacturas;
import dao.DAOTablaHostales;
import dao.DAOTablaHoteles;
import dao.DAOTablaOperadores;
import dao.DAOTablaPropuestas;
import dao.DAOTablaReservas;
import dao.DAOTablaServicios;
import dao.DAOTablaVecinos;
import dao.DAOTablaViviendasUniversitarias;
import rest.ConsultasRest;
import rest.ConsultasRest.Respuesta;
import rest.ConsultasRest.RespuestaConsulta2;
import vos.Apartamento;
import vos.Cliente;
import vos.Hostal;
import vos.Hotel;
import vos.Operador;
import vos.Propuesta;
import vos.Reserva;
import vos.Servicio;
import vos.Vecino;
import vos.ViviendaUniversitaria;
import java.text.SimpleDateFormat;


/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author David Bautista
 */
public class AlohAndesTM {


	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Atributo que dará el último estado correcto de la base de datos.
	 */
	private Savepoint savepoint;

	/**
	 * Metodo constructor de la clase RotondAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto RotondAndesMaster, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public AlohAndesTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}



	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////






	public Cliente buscarClientePorId(Long id) throws SQLException, Exception{
		DAOTablaClientes dao = new DAOTablaClientes();
		Cliente ret = null;
		boolean conexionPropia = false; 
		try {

			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); conexionPropia = true; 
				conexionPropia = true;
				this.conn.setAutoCommit(false);
			}

			dao.setConn(conn);
			ret = dao.darCliente(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public Cliente crearCliente(Cliente cliente) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		Cliente ret = null;
		try {
			//verificar reglas de negocio
			if (buscarClientePorId(cliente.getCodigo()) != null) {
				throw new Exception("Ya hay un cliente con ese c�digo");
			}
			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			dao.crearCliente(cliente);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 

			ret = buscarClientePorId(cliente.getCodigo());

			if(ret == null) {
				throw new Exception("No se guard� correctamente el cliente, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}


	public boolean existeOperadorPorId(Long id) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaOperadores dao = new DAOTablaOperadores();
		boolean ret = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			ret = dao.existeOperadorPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}


	public void crearOperadorApartamento(Apartamento apartamento)  throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaApartamentos dao = new DAOTablaApartamentos();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(apartamento.getId())) {
				throw new Exception("Ya hay un operador con ese c�digo");
			}


			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);
			dao.crearApartamento(apartamento);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");



			//verificar 
			if(!existeOperadorPorId(apartamento.getId())) {
				throw new Exception("No se guard� correctamente el apartamento, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit(); 

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void crearOperadorHotel(Hotel hotel) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaHoteles dao = new DAOTablaHoteles();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(hotel.getId())) {
				throw new Exception("Ya hay un cliente con ese c�digo");
			}

			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			dao.crearHotel(hotel);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");

			//verificar 
			if(!existeOperadorPorId(hotel.getId())) {
				throw new Exception("No se guard� correctamente el hotel, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	public void crearOperadorHostal(Hostal hostal) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaHostales dao = new DAOTablaHostales();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(hostal.getId())) {
				throw new Exception("Ya hay un cliente con ese c�digo");
			}

			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			dao.crearHostal(hostal);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(hostal.getId())) {
				throw new Exception("No se guard� correctamente el hotel, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void crearOperadorVecino(Vecino vecino) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaVecinos dao = new DAOTablaVecinos();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(vecino.getId())) {
				throw new Exception("Ya hay un cliente con ese c�digo");
			}

			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			dao.crearVecino(vecino);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(vecino.getId())) {
				throw new Exception("No se guard� correctamente el hotel, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void crearOperadorViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaViviendasUniversitarias dao = new DAOTablaViviendasUniversitarias();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(viviendaUniversitaria.getId())) {
				throw new Exception("Ya hay un cliente con ese c�digo");
			}

			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true;
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			dao.crearViviendaUni(viviendaUniversitaria);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(viviendaUniversitaria.getId())) {
				throw new Exception("No se guard� correctamente el hotel, revisar xd...");
			}


			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public boolean existePropuestaPorId(Long id) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		boolean ret = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
			dao.setConn(conn);
			ret = dao.existePropuestaPorId(id);

			if(conexionPropia)
				this.conn.commit();

		} catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public void crearPropuesta(Long idOperador, Propuesta propuesta) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		try {

			//verificar reglas de negocio
			if (existePropuestaPorId(propuesta.getId())) {
				throw new Exception("Ya hay una propuesta con ese c�digo");
			}

			if(!existeOperadorPorId(idOperador)) {
				throw new Exception("No existe el operador");
			}

			//proc
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);
			dao.crearPropuesta(propuesta, idOperador);

			System.out.println("lo creo");

			this.conn.setSavepoint();

			//asignar servicios a propuestas

			if(!propuesta.getServicios().isEmpty() && propuesta.getServicios() != null) {
				agregarServiciosAPropuesta(propuesta, propuesta.getServicios());
			}
			//verificar 



			if(!existePropuestaPorId(propuesta.getId())) {
				throw new Exception("No se guard� correctamente la propuesta, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}


	public boolean existeServicioPorId(Long id) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaServicios dao = new DAOTablaServicios();
		boolean ret = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			ret = dao.existeServicioPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public void reservarPropuestaIndividual(Long idCliente, Reserva reserva)throws SQLException, Exception{
		boolean conexionPropia = false;
		DAOTablaReservas dao = new DAOTablaReservas();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true;
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			List<Operador> operadores = darOperadoresPor(DAOTablaOperadores.PROPUESTA, reserva.getPropuesta().getId().toString());

			if(operadores.isEmpty())
				throw new Exception("No se encontró el operador. Error fatal.");

			Long idFactura =crearFactura(idCliente, operadores.get(0).getId(), reserva.getPropuesta().getNombre());

			crearReserva(idFactura, idCliente, reserva);

			if(conexionPropia)
				this.conn.commit();
			else
				this.savepoint = this.conn.setSavepoint();

		} catch (SQLException e) {
			this.conn.rollback(savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}


	public void crearReserva(Long idFactura, Long idCliente, Reserva reserva)  throws SQLException, Exception{


		boolean conexionPropia = false; 

		DAOTablaReservas dao = new DAOTablaReservas();

		//reglas de negocio

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true;
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			}



			if(existeReservaEnConflicto(idCliente, reserva.getPropuesta().getId(), reserva.getFechaInicial(), reserva.getFechaFinal())) {
				throw new Exception("Ya existe una reserva de esta propuesta para este cliente");
			}


			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			String param = reserva.getPropuesta().getId().toString() + "," + dtf.format(reserva.getFechaInicial()) + "," + dtf.format(reserva.getFechaFinal());
			if(darPropuestasPor(DAOTablaPropuestas.PROPUESTA_DISPONIBLE_POR_ID, param).isEmpty())
				throw new Exception("Esta propuesta ya está reservada para los días en los que la reserva piensa ser efectuada");

			dao.setConn(conn);
			dao.crearReserva(idFactura, reserva);

			if(conexionPropia)
				this.conn.commit();
			else
				this.savepoint = this.conn.setSavepoint();

		} catch (SQLException e) {
			this.conn.rollback(savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public Long crearFactura(Long idCliente, Long idOperador, String concepto) throws SQLException, Exception{
		boolean conexionPropia = false; 
		Long index = null;
		DAOTablaFacturas dao = new DAOTablaFacturas();
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}



			dao.setConn(conn);

			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			Date hoy = new Date();

			index = dao.crearFactura(idCliente, dtf.format(hoy), idOperador, concepto);

		} catch (SQLException e) {
			this.conn.rollback(savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia){
					this.conn.commit();
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return index;

	}

	public boolean existeReservaEnConflicto(Long idCliente, Long idProp, Date fechaIn, Date fechaFi) throws SQLException, Exception {
		boolean conexionPropia = false; 
		DAOTablaReservas dao = new DAOTablaReservas();
		boolean ret = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			ret = dao.existeReservaPropuestaCliente(idCliente, idProp, fechaIn, fechaFi);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public List<Reserva> darReservasPor(int filtro, String parametro) throws SQLException, Exception{
		DAOTablaReservas dao = new DAOTablaReservas();
		boolean conexionPropia = false; 
		List<Reserva> reservas = new ArrayList<Reserva>();

		if(filtro == DAOTablaReservas.BUSQUEDA_CLIENTE && buscarClientePorId(Long.parseLong(parametro)) == null) {
			throw new Exception("El cliente no existe");
		}

		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			reservas = dao.darReservasPor(filtro, parametro);
			System.out.println("reservas...");
			for(Reserva a : reservas) {
				System.out.println("--" + a.getId() + "--");
				a.setPropuesta(darPropuestasPor(DAOTablaPropuestas.RESERVA, a.getId().toString()).get(0));
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return reservas;
	}

	public List<Propuesta> darPropuestasPor(int filtro, String parametro)  throws SQLException, Exception {
		boolean conexionPropia = false; 
		List<Propuesta> propuestas; 
		DAOTablaPropuestas dao = new DAOTablaPropuestas();

		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			propuestas = dao.darPropuestasPor(filtro, parametro);

			for(Propuesta p : propuestas) {
				p.setServicios(darServiciosPor(DAOTablaServicios.PROPUESTA, p.getId().toString()));
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return propuestas; 
	}

	public List<Servicio> darServiciosPor(int filtro, String parametro) throws SQLException, Exception {
		boolean conexionPropia = false; 
		List<Servicio> servicios; 
		DAOTablaServicios dao = new DAOTablaServicios();

		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			servicios = dao.darServiciosPor(filtro, parametro);

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return servicios; 

	}

	public void terminarReserva(Long idCliente, Long idReserva, Double cantPago) throws SQLException, Exception{
		boolean conexionPropia = false;
		try{
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			List<Reserva> reservas = darReservasPor(DAOTablaReservas.BUSQUEDA_CLIENTE_ID_RESERVA_ID,  idCliente + "," + idReserva);

			if( reservas.isEmpty()) 
				throw new Exception("El cliente no tiene una reserva con dicho id");

			actualizarCostoTotal(reservas.get(0));

			//this.savepoint = this.conn.setSavepoint(); 
			//se comenta para evitar que el precio se aumente cada vez que el dinero no es suficiente

			boolean fin = cancelarFactura(idReserva, cantPago);

			if (fin) {
				eliminarReserva(reservas.get(0));
			}
			else 
				throw new Exception("El dinero no es suficiente");

			if(conexionPropia)
				this.conn.commit();

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void retirarPropuesta(Long idOperador, Long idPropuesta) throws SQLException, Exception {
		boolean conexionPropia = false; 

		DAOTablaPropuestas dao = new DAOTablaPropuestas();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			if(darPropuestasPor(DAOTablaPropuestas.ID_IDOPERADOR, idPropuesta + "," + idOperador).isEmpty()) {
				throw new Exception("El operador con el id " + idOperador + " no cuenta con una propuesta con id " + idPropuesta);
			}

			if(!darAprobacionRetiroPropuesta(idPropuesta)) {
				throw new Exception("No se puede retirar la propuesta porque a�n no hay terminado la �ltima reserva vigente");
			}


			dao.setConn(conn);

			dao.retirarPropuesta(idPropuesta);

			if(conexionPropia)
				this.conn.commit();

		} catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void crearReservaColectiva(Long idCliente, List<Reserva> reservas) throws SQLException, Exception {

		boolean conexionPropia = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			}

			this.savepoint = this.conn.setSavepoint();

			Long idFactura = crearFactura(idCliente, new Long(0), "Reserva Colectiva");

			if(reservas.isEmpty())
				throw new Exception("La lista de reservas está vacía");

			for(Reserva r : reservas){
				crearReserva(idFactura, idCliente, r);
				this.savepoint = this.conn.setSavepoint();
			}

			if(conexionPropia)
				this.conn.commit();

		}catch (SQLException e) {
			this.conn.rollback(savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void terminarReservaColectiva(Long idCliente, Long[] idReservas, Double cantPago) throws SQLException, Exception{
		boolean conexionPropia = false;

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			}

			this.savepoint = this.conn.setSavepoint();



			for(Long idReserva : idReservas){

				List<Reserva> reservas = darReservasPor(DAOTablaReservas.BUSQUEDA_CLIENTE_ID_RESERVA_ID,  idCliente + "," + idReserva);
				if(reservas.isEmpty())
					throw new Exception("No existe la reserva con el id " + idReserva);

				Double precReserva = calcularPrecioActual(saberFechasYDiasCancelacionMillis(idReserva), reservas.get(0));
				System.out.println("CantPago: " + cantPago);
				System.out.println("PrecReserva: " + precReserva);
				cantPago = cantPago - precReserva;
				System.out.println("Restante: " + cantPago);
				if(cantPago < 0 && idReservas[idReservas.length - 1] != idReserva)
					throw new Exception("El dinero no es suficiente para terminar de pagar las reservas. Sin embargo, se pagaron hasta donde alcanzó.");

				if(idReservas[idReservas.length - 1] == idReserva)
					precReserva += cantPago;

				terminarReserva(idCliente, idReserva, precReserva);
				this.savepoint = this.conn.setSavepoint();
			}


			if(conexionPropia)
				this.conn.commit();

		}catch (SQLException e) {
			this.conn.rollback(savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void inhabilitaroferta(Long idOferta) throws SQLException, Exception {
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		boolean conexionPropia = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			List<Reserva> reservasEnPeligro = darReservasPor(DAOTablaReservas.BUSQUEDA_PROPUESTA, idOferta.toString());
			List<Reserva> reservasCanceladas = new ArrayList<Reserva>();
			for(Reserva res : reservasEnPeligro) {
				if(!reubicarCancelarReserva(res))
					reservasCanceladas.add(res);
				this.savepoint = this.conn.setSavepoint();
			}

			this.savepoint = this.conn.setSavepoint();

			//No permitir la creación de reservas para una propuesta.

			dao.bloquearOferta(idOferta);

			if(conexionPropia)
				this.conn.commit();

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	public List<Cliente> darClientesPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Cliente> clientes = new ArrayList<>(); 
		DAOTablaClientes dao = new DAOTablaClientes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			clientes = dao.darClientesPor(filtro, parametro);

			for(Cliente c : clientes) {
				//agregar facturas ( luego lo hago, igual no es necesario por ahora)
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes; 
	}

	public List<Operador> darOperadoresPor(int filtro, String parametro) throws SQLException, Exception {
		boolean conexionPropia = false;
		List<Operador> operadores = new ArrayList<Operador>();
		DAOTablaOperadores dao = new DAOTablaOperadores();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			operadores = dao.darOperadoresPor(filtro, parametro);

			for(Operador p : operadores) {
				//agregar propuestas ( luego lo hago, igual no es necesario por ahora)
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return operadores;
	}
	public void rehabilitarOferta(Long idOferta, Long idOperador)throws SQLException, Exception{
		boolean conexionPropia = false;
		DAOTablaPropuestas dao = new DAOTablaPropuestas(); 
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			List<Propuesta> p = darPropuestasPor(DAOTablaPropuestas.ID_OPERADOR_ID_PROPUESTA, idOperador + "," + idOferta);

			if(p.isEmpty())
				throw new Exception("El operador con el id " + idOperador + "no cuenta con una propuesta con id " + idOferta);

			if(p.get(0).getTipo() < 1000)
				throw new Exception("La oferta con id de propuesta = " + idOferta + " no se encuentra inhabilitada");

			dao.desbloquearOferta(idOferta);

			if(conexionPropia)
				this.conn.commit();
		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Respuesta> reqConsUno() throws SQLException, Exception {
		boolean conexionPropia = false;
		DAOTablaFacturas dao = new DAOTablaFacturas();
		ConsultasRest cs = new ConsultasRest();
		List<Respuesta> respuestas = new ArrayList<Respuesta>();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			ResultSet rs = dao.reqCons1();

			while(rs.next()) {
				String nombre = "Operador " + rs.getInt("ID1");
				Respuesta act = cs.new Respuesta(nombre);
				Double s1 = rs.getDouble("SUM1");
				Double s2 = rs.getDouble("SUM2");
				act.agregarDato("Año actual", "" + (s1 == null ? 0 : s1));
				act.agregarDato("Año pasado", "" + (s2 == null ? 0 : s2));

				respuestas.add(act);
			}

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuestas;

	}

	public List<RespuestaConsulta2> reqConsDos(ConsultasRest cs) throws SQLException, Exception {
		boolean conexionPropia = false;
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		List<RespuestaConsulta2> respuestas = new ArrayList<RespuestaConsulta2>();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			ResultSet rs = dao.reqCons2();

			while(rs.next()) {



				Propuesta pr = new Propuesta();

				pr.setCosto(rs.getDouble("COSTO"));
				pr.setDiasCancelacion(rs.getInt("DIAS_CANCELACION"));
				pr.setId(rs.getLong("ID"));
				pr.setNombre(rs.getString("NOMBRE"));
				pr.setTipo(rs.getInt("TIPO"));


				respuestas.add(cs.new RespuestaConsulta2(pr, rs.getInt("CONT")));
			}

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuestas;
	}	


	public Respuesta reqConsTres(ConsultasRest consultasRest) throws SQLException, Exception{
		boolean conexionPropia = false;
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		ConsultasRest cs = new ConsultasRest();
		Respuesta respuesta = consultasRest.new Respuesta("Tasas");

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			ResultSet rs = dao.reqCons3();

			while(rs.next()) {

				respuesta.agregarDato("Tasa de alojamiento de propuesta con id " + rs.getLong("ID_ALOJAMIENTO"), 
						rs.getDouble("TASA") + " por ciento");

			}

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuesta;
	}
	
	public List<Respuesta> reqConsCinco(ConsultasRest cs, int tipo) throws SQLException, Exception{

		boolean conexionPropia = false;
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		List<Respuesta> respuestas = new ArrayList<Respuesta>();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			ResultSet rs = dao.reqCons5(tipo);

			while(rs.next()) {
				Respuesta rta;
				Long idReserva = rs.getLong("ID_RES");
				Date FechaUltimoPago = rs.getDate("FECHA_ULTIMO_PAGO");
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println("idReserva: " + idReserva);
				
				if(idReserva == 0) {
					rta = cs.new Respuesta("Factura del " + dtf.format(rs.getDate("FECHA_CREACION")));
					rta.agregarDato("idCliente", "" + rs.getLong("ID_CLIENTE"));
					rta.agregarDato("Fecha de pago", dtf.format(FechaUltimoPago));
					rta.agregarDato("Pagado", "" + rs.getDouble("PAGADO"));
					rta.agregarDato("Concepto", rs.getString("CONCEPTO"));
					rta.agregarDato("Operador que facturó", "" + rs.getLong("ID_OPERADOR"));
					respuestas.add(rta);
				}

				else{
					Date inicio = rs.getDate("FECHA_INIC");
					Date fin = rs.getDate("FECHA_FINA");
					rta = cs.new Respuesta("Reserva para el día " + dtf.format(inicio));
					rta.agregarDato("idCliente", "" + rs.getLong("ID_CLIENTE"));
					long diff = Math.abs(fin.getTime() - inicio.getTime());
					Long diffDays = diff / (24 * 60 * 60 * 1000);
					rta.agregarDato("Fecha de terminación de reserva", dtf.format(fin));
					rta.agregarDato("numero días reserva", diffDays.toString());

					int tipoN = rs.getInt("TIPO");
					String sTipo = "";
					switch (tipoN) {
					case Propuesta.HABITACION_SENCILLA:
						sTipo = "habitación sencilla";
						break;
					case Propuesta.HABITACION_COMPARTIDA:
						sTipo = "Habitación compartida";
						break;
					case Propuesta.HABITACION_SUITE_SENCILLA:
						sTipo = "Habitación suite sencilla";
						break;
					case Propuesta.HABITACION_SUITE_COMPARTIDA:
						sTipo = "Habitación suite compartida";
						break;
					case Propuesta.HABITACION_SEMI_SUITE_SENCILLA:
						sTipo = "Habitación semi suite sencilla";
						break;
					case Propuesta.HABITACION_SEMI_SUITE_COMPARTIDA:
						sTipo = "Habitación semi suite compartida";
						break;
					default:
						sTipo = "Otro";
						break;
					}
					rta.agregarDato("Nombre propuesta", rs.getString("NOMBRE"));
					rta.agregarDato("Tipo Alojamiento", sTipo);
					rta.agregarDato("Costo", ""+rs.getDouble("COSTO"));
					rta.agregarDato("Días permitidos de cancelación", ""+rs.getInt("DIAS_CANCELACION"));
					respuestas.add(rta);

				}

			}

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuestas;
	}


	public List<Respuesta> reqConsSeis(ConsultasRest cs, Long idCliente) throws SQLException, Exception{

		boolean conexionPropia = false;
		DAOTablaClientes dao = new DAOTablaClientes();
		List<Respuesta> respuestas = new ArrayList<Respuesta>();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);

			ResultSet rs = dao.reqCons6(idCliente);

			while(rs.next()) {
				Respuesta rta;
				Long idReserva = rs.getLong("ID_RES");
				Date FechaUltimoPago = rs.getDate("FECHA_ULTIMO_PAGO");
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println("idReserva: " + idReserva);
				
				if(idReserva == 0) {
					rta = cs.new Respuesta("Factura del " + dtf.format(rs.getDate("FECHA_CREACION")));
					rta.agregarDato("Fecha de pago", dtf.format(FechaUltimoPago));
					rta.agregarDato("Pagado", "" + rs.getDouble("PAGADO"));
					rta.agregarDato("Concepto", rs.getString("CONCEPTO"));
					rta.agregarDato("Operador que facturó", "" + rs.getLong("ID_OPERADOR"));
					respuestas.add(rta);
				}

				else{
					Date inicio = rs.getDate("FECHA_INIC");
					Date fin = rs.getDate("FECHA_FINA");
					rta = cs.new Respuesta("Reserva para el día " + dtf.format(inicio));

					long diff = Math.abs(fin.getTime() - inicio.getTime());
					Long diffDays = diff / (24 * 60 * 60 * 1000);
					rta.agregarDato("Fecha de terminación de reserva", dtf.format(fin));
					rta.agregarDato("numero días reserva", diffDays.toString());

					int tipo = rs.getInt("TIPO");
					String sTipo = "";
					switch (tipo) {
					case Propuesta.HABITACION_SENCILLA:
						sTipo = "habitación sencilla";
						break;
					case Propuesta.HABITACION_COMPARTIDA:
						sTipo = "Habitación compartida";
						break;
					case Propuesta.HABITACION_SUITE_SENCILLA:
						sTipo = "Habitación suite sencilla";
						break;
					case Propuesta.HABITACION_SUITE_COMPARTIDA:
						sTipo = "Habitación suite compartida";
						break;
					case Propuesta.HABITACION_SEMI_SUITE_SENCILLA:
						sTipo = "Habitación semi suite sencilla";
						break;
					case Propuesta.HABITACION_SEMI_SUITE_COMPARTIDA:
						sTipo = "Habitación semi suite compartida";
						break;
					default:
						sTipo = "Otro";
						break;
					}
					rta.agregarDato("Nombre propuesta", rs.getString("NOMBRE"));
					rta.agregarDato("Tipo Alojamiento", sTipo);
					rta.agregarDato("Costo", ""+rs.getDouble("COSTO"));
					rta.agregarDato("Días permitidos de cancelación", ""+rs.getInt("DIAS_CANCELACION"));
					respuestas.add(rta);

				}

			}

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuestas;
	}

	public String[] reqConsSiete(String tiempo, Integer unidad) throws SQLException, Exception{
		boolean conexionPropia = false;
		DAOTablaReservas dao = new DAOTablaReservas();
		String[] ret =  new String[2];
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);
			int tem = tiempo.equalsIgnoreCase("day")? 1 : tiempo.equalsIgnoreCase("week") ? 2 : tiempo.equalsIgnoreCase("month") ? 3 : tiempo.equalsIgnoreCase("year") ? 4: 0;

			if(tem == 0)
				throw new Exception("El tiempo, " + tiempo +  ", no es correcto");

			if(unidad <= 0)
				throw new Exception("la unidad de tiempo, " + unidad +  ", no es un valor correcto, pendejo");

			int ite = 1;
			switch (tem) {
			case 1:
				ite = unidad;
				break;

			case 2:
				ite = 7 * unidad;
				break;

			case 3: 
				ite = 7 * 4 * unidad;
				break;

			case 4: 
				ite = 7 * 4 * 12 * unidad;
				break;
			default:
				break;
			}

			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");


			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			Date diaMayorDemanda = new Date();
			int cantMaximaDemanda = 0;

			Date diaMayorIngreso = new Date();
			Double  cantMAyorIngreso = 0.0;
			while(ite >= 0) {

				//Mayor Demanda
				c.add(Calendar.DATE, ite);
				Date act = c.getTime();
				int cantAct = dao.darCantidadReservasDia(dtf.format(act));
				if(cantAct >= cantMaximaDemanda ) {
					diaMayorDemanda = act;
					cantMaximaDemanda = cantAct;
				}

				//Mayor Ingreso
				c.add(Calendar.DATE, ite*(-1));
				act = c.getTime();
				Double ingAct = dao.darIngresosTotalDia(dtf.format(act));
				if(ingAct >= cantMAyorIngreso) {
					diaMayorIngreso = act;
					cantMAyorIngreso = ingAct;
				}

				ite--;
			}

			if(conexionPropia)
				this.conn.commit();

			ret[0] = dtf.format(diaMayorDemanda);
			ret[1] = dtf.format(diaMayorIngreso);

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return ret;
	}

	

	public Respuesta reqConsSieteMejorado(ConsultasRest cs, String tiempo) throws SQLException, Exception{
		boolean conexionPropia = false;
		DAOTablaReservas dao = new DAOTablaReservas();
		Respuesta rta;
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}

			dao.setConn(conn);
			int tem = tiempo.equalsIgnoreCase("day")? 1 : tiempo.equalsIgnoreCase("week") ? 2 : tiempo.equalsIgnoreCase("month") ? 3 : tiempo.equalsIgnoreCase("year") ? 4: 0;

			if(tem == 0)
				throw new Exception("El tiempo, " + tiempo +  ", no es correcto");

		
			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

		
			
			ResultSet rs = dao.reqConsSiete(tem);
			
			rta = cs.new Respuesta("Mejor fecha");
			rta.agregarDato(tiempo, tiempo + rs.getInt("HOLA"));
			
			

		}catch (SQLException e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if(conexionPropia)
				this.conn.rollback();
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return rta;
	}
	


	private boolean reubicarCancelarReserva(Reserva res) throws SQLException, Exception{

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		String param = res.getPropuesta().getTipo().toString() + "," + dtf.format(res.getFechaInicial()) + "," + dtf.format(res.getFechaFinal());
		List<Propuesta> propuestasSimilares = darPropuestasPor(DAOTablaPropuestas.PROPUESTAS_DISPONIBLES_DE_TIPO, param);
		if(propuestasSimilares.isEmpty()){
			terminarReservaEmergencia(res);
			return false;
		}

		Cliente cli = darClientesPor(DAOTablaClientes.BUSQUEDA_POR_RESERVA, res.getId().toString()).get(0);
		res.setPropuesta(propuestasSimilares.get(0));
		terminarReservaEmergencia(res);
		reservarPropuestaIndividual(cli.getCodigo(), res);
		return true;

	}



	private void terminarReservaEmergencia(Reserva res) throws SQLException, Exception{

		Double cantPago = actualizarCostoTotal(res);
		cancelarFactura(res.getId(), cantPago + 0.69);
		eliminarReserva(res);		
	}

	private void agregarServiciosAPropuesta(Propuesta propuesta, List<Servicio> servicios) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaServicios dao = new DAOTablaServicios();

		try {
			for(Servicio ser : servicios) {

				if(!existeServicioPorId(ser.getId())) {
					throw new Exception("No existe el servicio con el id");
				}

				dao.conectarServicioAPropuesta(ser.getId(), propuesta.getId());
				this.savepoint = this.conn.setSavepoint();
			}

		}  catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}



	private void eliminarReserva(Reserva reserva) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaReservas dao = new DAOTablaReservas();
		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);

			dao.eliminarReserva(reserva.getId());

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	private Double actualizarCostoTotal(Reserva reserva) throws SQLException, Exception{
		boolean conexionPropia = false; 
		Long[] dias = saberFechasYDiasCancelacionMillis(reserva.getId());
		Double precioActual = calcularPrecioActual(dias, reserva);

		DAOTablaFacturas dao = new DAOTablaFacturas();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				this.savepoint = this.conn.setSavepoint();
			}


			dao.setConn(conn);

			dao.actualizarPago(precioActual, reserva.getId());

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return precioActual;
	}


	private Double calcularPrecioActual(Long[] dias, Reserva reserva) throws SQLException, Exception {

		Double precioSinDescuento = reserva.getPropuesta().getCosto();
		for(Servicio s : reserva.getPropuesta().getServicios()) {
			precioSinDescuento += s.getCosto();
		}

		Date hoy = new Date();
		Long millisHoy = hoy.getTime();

		Double descuento = millisHoy >= dias[3] ? 0 : millisHoy >= dias[2] ? 0.5 : millisHoy >= dias[0] ? 0.7 : 0.9;

		return precioSinDescuento - precioSinDescuento*descuento;

	}

	private Long[] saberFechasYDiasCancelacionMillis(Long idReserva) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		Long[] dias = new Long[4];
		try {
			if (this.conn == null || this.conn.isClosed()) {this.conn = darConexion(); conexionPropia = true; this.conn.setAutoCommit(false);}
			dao.setConn(conn);
			dias = dao.saberDiasCancelacionYFechasMillis(idReserva);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return dias;
	}

	private boolean cancelarFactura(Long idReserva, Double cantPago) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaFacturas dao = new DAOTablaFacturas();
		boolean ret = false;
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);

			}

			dao.setConn(conn);
			Double restante = dao.abonarFactura(idReserva, cantPago);
			if(restante <= 0)
				ret = true;

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}


	private boolean darAprobacionRetiroPropuesta(Long idPropuesta) throws SQLException, Exception{
		List<Reserva> reservas = darReservasPor(DAOTablaReservas.BUSQUEDA_PROPUESTA, idPropuesta.toString());

		if(reservas.isEmpty())
			return true;

		return false;
	}









}
