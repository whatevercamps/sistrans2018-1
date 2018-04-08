/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (BogotÃ¡ - Colombia)
 * Departamento de IngenierÃ­a de Sistemas y ComputaciÃ³n
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.model.core.ID;

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
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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

		DAOTablaClientes dao = new DAOTablaClientes();
		Cliente ret = null;
		try {

			//verificar reglas de negocio
			if (buscarClientePorId(cliente.getCodigo()) != null) {
				throw new Exception("Ya hay un cliente con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearCliente(cliente);

			System.out.println("lo creo");
			//verificar 

			ret = buscarClientePorId(cliente.getCodigo());

			if(ret == null) {
				throw new Exception("No se guardó correctamente el cliente, revisar xd...");
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
				if(this.conn!=null)
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
		DAOTablaOperadores dao = new DAOTablaOperadores();
		boolean ret = false;
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
		DAOTablaApartamentos dao = new DAOTablaApartamentos();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(apartamento.getId())) {
				throw new Exception("Ya hay un operador con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearApartamento(apartamento);

			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(apartamento.getId())) {
				throw new Exception("No se guardó correctamente el apartamento, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void crearOperadorHotel(Hotel hotel) throws SQLException, Exception{
		DAOTablaHoteles dao = new DAOTablaHoteles();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(hotel.getId())) {
				throw new Exception("Ya hay un cliente con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearHotel(hotel);

			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(hotel.getId())) {
				throw new Exception("No se guardó correctamente el hotel, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}

	public void crearOperadorHostal(Hostal hostal) throws SQLException, Exception{
		DAOTablaHostales dao = new DAOTablaHostales();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(hostal.getId())) {
				throw new Exception("Ya hay un cliente con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearHostal(hostal);

			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(hostal.getId())) {
				throw new Exception("No se guardó correctamente el hotel, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void crearOperadorVecino(Vecino vecino) throws SQLException, Exception{
		DAOTablaVecinos dao = new DAOTablaVecinos();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(vecino.getId())) {
				throw new Exception("Ya hay un cliente con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearVecino(vecino);

			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(vecino.getId())) {
				throw new Exception("No se guardó correctamente el hotel, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void crearOperadorViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception{
		DAOTablaViviendasUniversitarias dao = new DAOTablaViviendasUniversitarias();
		try {

			//verificar reglas de negocio
			if (existeOperadorPorId(viviendaUniversitaria.getId())) {
				throw new Exception("Ya hay un cliente con ese código");
			}

			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearViviendaUni(viviendaUniversitaria);

			System.out.println("lo creo");
			//verificar 


			if(!existeOperadorPorId(viviendaUniversitaria.getId())) {
				throw new Exception("No se guardó correctamente el hotel, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public boolean existePropuestaPorId(Long id) throws SQLException, Exception{
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		boolean ret = false;
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			ret = dao.existePropuestaPorId(id);

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
				if(this.conn!=null)
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
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		try {

			//verificar reglas de negocio
			if (existePropuestaPorId(propuesta.getId())) {
				throw new Exception("Ya hay una propuesta con ese código");
			}

			if(!existeOperadorPorId(idOperador)) {
				throw new Exception("No existe el operador");
			}

			//proc
			this.conn = darConexion();
			dao.setConn(conn);

			dao.crearPropuesta(propuesta, idOperador);

			System.out.println("lo creo");


			//asignar servicios a propuestas

			if(!propuesta.getServicios().isEmpty() && propuesta.getServicios() != null) {
				agregarServiciosAPropuesta(propuesta, propuesta.getServicios());
			}
			//verificar 



			if(!existePropuestaPorId(propuesta.getId())) {
				throw new Exception("No se guardó correctamente la propuesta, revisar xd...");
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void agregarServiciosAPropuesta(Propuesta propuesta, List<Servicio> servicios) throws SQLException, Exception{
		DAOTablaServicios dao = new DAOTablaServicios();

		try {
			for(Servicio ser : servicios) {
				if(!existeServicioPorId(ser.getId())) {
					throw new Exception("No existe el servicio con el id");
				}

				dao.conectarServicioAPropuesta(ser.getId(), propuesta.getId());
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public boolean existeServicioPorId(Long id) throws SQLException, Exception{
		DAOTablaServicios dao = new DAOTablaServicios();
		boolean ret = false;
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public void crearReserva(Long idCliente, Reserva reserva)  throws SQLException, Exception{


		if(existeReservaEnConflicto(idCliente, reserva.getPropuesta().getId(), reserva.getFechaInicial(), reserva.getFechaFinal())) {
			throw new Exception("Ya existe una reserva de esta propuesta para este cliente");
		}

		Long idFactura = crearFactura(idCliente);

		DAOTablaReservas dao = new DAOTablaReservas();

		//reglas de negocio

		try {
			this.conn = darConexion();
			dao.setConn(conn);
			dao.crearReserva(idFactura, reserva);


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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public Long crearFactura(Long idCliente) throws SQLException, Exception{
		Long index = null;
		DAOTablaFacturas dao = new DAOTablaFacturas();
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			Date hoy = new Date();
			
			index = dao.crearFactura(idCliente, dtf.format(hoy));

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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return index;

	}

	public boolean existeReservaEnConflicto(Long idCliente, Long idProp, Date fechaIn, Date fechaFi) throws SQLException, Exception {
		DAOTablaReservas dao = new DAOTablaReservas();
		boolean ret = false;
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
		List<Reserva> reservas = new ArrayList<Reserva>();

		if(filtro == DAOTablaReservas.BUSQUEDA_CLIENTE && buscarClientePorId(Long.parseLong(parametro)) == null) {
			throw new Exception("El cliente no existe");
		}

		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
		List<Propuesta> propuestas; 
		DAOTablaPropuestas dao = new DAOTablaPropuestas();

		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
		List<Servicio> servicios; 
		DAOTablaServicios dao = new DAOTablaServicios();

		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
		List<Reserva> reservas = darReservasPor(DAOTablaReservas.BUSQUEDA_CLIENTE_ID_RESERVA_ID,  idCliente + "," + idReserva);
		if( reservas.isEmpty()) {
			throw new Exception("El cliente no tiene una reserva con dicho id");
		}
		actualizarCostoTotal(reservas.get(0));
		boolean fin = cancelarFactura(idReserva, cantPago);
		if (fin) 
			eliminarReserva(reservas.get(0));
		else {
			throw new Exception("El dinero no es suficiente, se abonó pero no se terminó la reserva.");
		}
	}
	

	public void retirarPropuesta(Long idOperador, Long idPropuesta) throws SQLException, Exception {
		
		if(darPropuestasPor(DAOTablaPropuestas.ID_IDOPERADOR, idPropuesta + "," + idOperador).isEmpty()) {
			throw new Exception("El operador con el id " + idOperador + " no cuenta con una propuesta con id " + idPropuesta);
		}
		
		if(!darAprobacionRetiroPropuesta(idPropuesta)) {
			throw new Exception("No se puede retirar la propuesta porque aún no hay terminado la última reserva vigente");
		}
		
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			
			dao.retirarPropuesta(idPropuesta);

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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	private void eliminarReserva(Reserva reserva) throws SQLException, Exception{
		DAOTablaReservas dao = new DAOTablaReservas();
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	private void actualizarCostoTotal(Reserva reserva) throws SQLException, Exception{
		
		Long[] dias = saberFechasYDiasCancelacionMillis(reserva);
		Double precioActual = calcularPrecioActual(dias, reserva);

		DAOTablaFacturas dao = new DAOTablaFacturas();
		
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

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

	private Long[] saberFechasYDiasCancelacionMillis(Reserva reserva) throws SQLException, Exception{
		DAOTablaPropuestas dao = new DAOTablaPropuestas();
		Long[] dias = new Long[4];
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			dias = dao.saberDiasCancelacionYFechasMillis(reserva.getId());
			

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
				if(this.conn!=null)
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
		DAOTablaFacturas dao = new DAOTablaFacturas();
		boolean ret = false;
		try {
			this.conn = darConexion();
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
				if(this.conn!=null)
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
