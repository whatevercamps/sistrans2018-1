/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogot√° - Colombia)
 * Departamento de Ingenier√≠a de Sistemas y Computaci√≥n
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
import java.util.List;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.model.core.ID;

import dao.DAOTablaClientes;
import dao.DAOTablaClientesFrecuentes;
import dao.DAOTablaIngredientes;
import dao.DAOTablaPedidos;
import dao.DAOTablaProductos;
import dao.DAOTablaRestaurantes;
import dao.DAOTablaTipos;
import dao.DAOTablaZonas;
import dtm.RotondAndesDistributed;
import jms.NonReplyException;
import dao.DAOTablaOrdenes;
import vos.Cliente;
import vos.ClienteFrecuente;
import vos.Informe;
import vos.Ingrediente;
import vos.IngredienteBase;
import vos.ListaRentabilidad;
import vos.Orden;
import vos.Pedido;
import vos.Producto;
import vos.ProductoBase;
import vos.RentabilidadRestaurante;
import vos.Restaurante;
import vos.TipoComida;
import vos.Zona;

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
	 * Atributo que contiene el DTM.
	 */
	private RotondAndesDistributed dtm;

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

	public List<Cliente> darClientes() throws Exception {
		List<Cliente> clientes; 
		DAOTablaClientes daoCliente = new DAOTablaClientes();
		try {

			this.conn = darConexion();

			daoCliente.setConn(conn);
			clientes = daoCliente.darClientes();

			for (Cliente cli : clientes) {
				cli.setOrdenes(darOrdenesCliente(cli.getId()));
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
				daoCliente.cerrarRecursos();

				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes; 
	}

	public List<Orden> darOrdenesCliente(Long id) throws SQLException, Exception {

		List<Orden> ordenes; 
		DAOTablaOrdenes daoOrdenes = new DAOTablaOrdenes();

		try {

			this.conn = darConexion();

			daoOrdenes.setConn(conn);
			ordenes =daoOrdenes.darOrdenesCliente(id);
			for (Orden or : ordenes) {
				or.setProductosOrdenados(darProductosPor(DAOTablaProductos.ORDEN, or.getId().toString()));
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
				daoOrdenes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ordenes; 
	}





	public Cliente darCliente(Long id) throws SQLException, Exception {
		Cliente res;
		DAOTablaClientes dao = new DAOTablaClientes(); 
		DAOTablaOrdenes daoOrdenes = new DAOTablaOrdenes();
		try {

			this.conn = darConexion();

			dao.setConn(conn);
			res = dao.darCliente(id);

			daoOrdenes.setConn(conn);
			res.setOrdenes(darOrdenesCliente(id));

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
				daoOrdenes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;
	}

	public Producto darProducto(Long id, Long idRest) throws SQLException, Exception {
		Producto res;
		DAOTablaProductos daoProd = new DAOTablaProductos(); 
		try {
			this.conn = darConexion();
			daoProd.setConn(conn);
			res = daoProd.darProducto(id, idRest);
			res.setIngredientes(darIngredientesProducto(res.getId()));
			res.setTiposComida(darTiposProducto(res.getId()));



		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		finally {
			try {
				daoProd.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return res;
	}



	public void borrarPreferenciaClienteFrecuente(Long id, String password, Long idProd) throws SQLException, Exception {
		DAOTablaClientesFrecuentes dao = new DAOTablaClientesFrecuentes();
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			//Verificar Cliente
			if(!dao.verficarCliente(id, password)) 
				throw new Exception("Clave invalida");
			//fin Verificacion		

			dao.borrarPreferencia(id, idProd);
		}catch (SQLException e) {
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

	public ClienteFrecuente agregarPreferenciaClienteFrecuente(Long id, String password, Long idProd) throws SQLException, Exception {
		ClienteFrecuente cliente = null;
		DAOTablaClientesFrecuentes dao = new DAOTablaClientesFrecuentes();
		DAOTablaProductos daoPref = new DAOTablaProductos();
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			//Verificar Cliente
			if(!dao.verficarCliente(id, password)) 
				throw new Exception("Clave invalida");
			//fin Verificacion		

			dao.registrarPreferencia(id, idProd);

			//INICIO AGREGAR PREFERENCIAS A ENTIDAD CLIENTEFRECUENTE
			cliente = dao.darClienteFrecuente(id);
			daoPref.setConn(conn);
			cliente.setPreferencias(daoPref.darPreferencias(id));

		}catch (SQLException e) {
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
		return cliente;
	}

	public Pedido agregarPedido(Cliente cliente, Producto producto, Restaurante rest) throws SQLException, Exception {
		Pedido res = null;
		DAOTablaPedidos dao = new DAOTablaPedidos();
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			res = dao.registrarPedido(cliente, producto, rest);
		}catch (SQLException e) {
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
		return res;
	}
	public List<Pedido> agregarPedidosMesa(Long id, Long[][] pedidos) throws SQLException, Exception {
		List<Pedido> res = new ArrayList<Pedido>();
		try {
			this.conn = darConexion();
			for(Long[] pedido : pedidos) {
				res.add(agregarPedido(darCliente(id), darProducto(pedido[0], pedido[1]), darRestaurante(pedido[1])));
			}


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return res;

	}



	public void despacharPedido(Long idPed) throws SQLException, Exception {
		DAOTablaPedidos dao = new DAOTablaPedidos();
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			//TODO Verificar Cliente
			dao.despacharPedido(idPed);
		}catch (SQLException e) {
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

	public void despacharPedidosMesa(Long idMesa, Long idRest) throws SQLException, Exception {
		DAOTablaPedidos dao = new DAOTablaPedidos();
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			Restaurante rest = darRestaurante(idRest);
			List<Pedido> pedidosMesa = darPedidosMesa(idMesa, rest);
			for(Pedido ped : pedidosMesa) {
				despacharPedido(ped.getId());
			}

		}catch (SQLException e) {
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

	private List<Pedido> darPedidosMesa(Long idMesa, Restaurante rest) throws SQLException, Exception {
		List<Pedido> pedidos; 
		DAOTablaPedidos daoPedidos = new DAOTablaPedidos();
		try {

			this.conn = darConexion();

			daoPedidos.setConn(conn);
			pedidos = daoPedidos.darPedidos(DAOTablaPedidos.SIN_DESPACHAR_DE_MESA, idMesa.toString());

			for (Pedido pedido : pedidos) {
				pedido.setRestaurante(rest.getId(), rest.getName());
				String clienteName = darCliente(pedido.getIdCliente()).getNombre();
				pedido.setNameCliente(clienteName);
				Producto prod = darProducto(pedido.getProducto().getId(), pedido.getIdRest());
				pedido.setProducto(prod);
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
				daoPedidos.cerrarRecursos();

				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidos; 
	}

	private Pedido darPedido(Long idPedido) throws SQLException, Exception {
		Pedido pedido; 
		DAOTablaPedidos daoPedidos = new DAOTablaPedidos();
		try {

			this.conn = darConexion();

			daoPedidos.setConn(conn);
			pedido = daoPedidos.darPedidos(DAOTablaPedidos.PEDIDO_POR_ID, idPedido.toString()).get(0);
			Restaurante restTemp = darRestaurante(pedido.getIdRest());
			pedido.setNameRest(restTemp.getName());
			String clienteName = darCliente(pedido.getIdCliente()).getNombre();
			pedido.setNameCliente(clienteName);
			Producto prod = darProducto(pedido.getProducto().getId(), pedido.getIdRest());
			pedido.setProducto(prod);


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
				daoPedidos.cerrarRecursos();

				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedido; 
	}

	public List<Producto> darProductos() throws SQLException, Exception {
		List<Producto> productos; 
		DAOTablaProductos dao = new DAOTablaProductos();

		try {
			this.conn = darConexion();
			dao.setConn(conn);
			productos = dao.darProductos();
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
		return productos; 
	}

	public List<Producto> darProductosPor(Integer filtro, String parametro)  throws SQLException, Exception {
		List<Producto> productos; 
		DAOTablaProductos dao = new DAOTablaProductos();

		try {
			this.conn = darConexion();
			dao.setConn(conn);
			productos = dao.darProductosPor(filtro, parametro);

			for(Producto prod : productos) {
				prod.setIngredientes(darIngredientesProducto(prod.getId()));
				prod.setTiposComida(darTiposProducto(prod.getId()));
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
		return productos; 
	}

	private List<TipoComida> darTiposProducto(Long id) throws SQLException, Exception {
		DAOTablaTipos dao = new DAOTablaTipos();
		List<TipoComida> tipos = new ArrayList<TipoComida>();
		try {

			this.conn = darConexion();

			dao.setConn(conn);
			tipos =dao.darTiposComidaProducto(id);

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
		return tipos; 
	}

	private List<Ingrediente> darIngredientesProducto(Long id) throws SQLException, Exception {
		DAOTablaIngredientes daoIng = new DAOTablaIngredientes();
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		try {

			this.conn = darConexion();

			daoIng.setConn(conn);
			ingredientes =daoIng.darIngredientesProducto(id);

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
				daoIng.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingredientes; 
	}

	public Zona agregarZona(Zona zona)throws SQLException, Exception{

		DAOTablaZonas dao = new DAOTablaZonas();
		try {
			this.conn = darConexion();
			dao.setConn(conn);	

			dao.addZona(zona);

		}catch (SQLException e) {
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
		return zona;
	}

	public Zona darZona(Long id) throws SQLException, Exception {

		Zona zona = null;

		DAOTablaZonas daoZona = new DAOTablaZonas();
		DAOTablaRestaurantes daoRes = new DAOTablaRestaurantes();
		DAOTablaProductos daoProd = new DAOTablaProductos();

		try {
			this.conn = darConexion();
			daoZona.setConn(conn);
			zona = daoZona.darZona(id);
			System.out.println("after dao ------> " + zona.getId() + " || " + zona.getNombre());

			//AGREGACI”N DE RESTAURANTES A ZONA
			if(zona != null) {
				daoRes.setConn(conn);
				List<Restaurante> restaurantes = daoRes.darRestaurantesDeZona(id);

				//AGREGACI”N DE PRODUCTOS A RESTAURANTES
				if(restaurantes != null && !restaurantes.isEmpty())
				{
					daoProd.setConn(conn);
					for(Restaurante rest : restaurantes) {
						rest.setProductos(daoProd.darProductosPor(DAOTablaProductos.RESTAURANTE, rest.getId().toString()));
					}
					zona.setRestaurantes(restaurantes);
				}
			}
		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {

				daoZona.cerrarRecursos();
				daoRes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zona;
	}

	public Restaurante darRestaurante(Long idRestProd) throws SQLException, Exception {

		Restaurante res;
		DAOTablaRestaurantes dao = new DAOTablaRestaurantes(); 
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			res = dao.darRestaurante(idRestProd);
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
		return res;
	}

	public void cancelarPedido(Long idPed)throws SQLException, Exception{

		DAOTablaPedidos dao = new DAOTablaPedidos();
		try {
			this.conn = darConexion();
			dao.setConn(conn);	

			dao.cancelarPedido(idPed);

		}catch (SQLException e) {
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


	public List<Informe> darRentabilidadUnificada(int i, int filtro, String initDate, String endDate)throws SQLException, Exception {
		List<Informe> rentFinal = new ArrayList<Informe>();
		rentFinal.addAll(darRentabilidad(i, filtro, initDate, endDate));

		try
		{
			String params = initDate + "," + endDate + "," + filtro + "," + "1";
			System.out.println("PAR¡METROS A ENVIAR: " + params);
			ListaRentabilidad resp = dtm.getRemoteRentabilidades(params);
			for (RentabilidadRestaurante a : resp.getRentabilidades()) {
				Informe convertido =  new Informe(a);
				rentFinal.add(convertido);
			}

		}
		catch(NonReplyException e)
		{
			System.err.println("ConexionExternaException:" + e.getMessage());
		}
		return null;
	}



	public List<Informe> darRentabilidad(int i, int filtro, String initDate, String endDate) throws SQLException, Exception {
		DAOTablaPedidos dao = new DAOTablaPedidos();
		List<Informe> rentabilidad;
		try {
			this.conn = darConexion();
			dao.setConn(conn);	
			rentabilidad = dao.darRentabilidad(i, filtro, initDate, endDate);


		}catch (SQLException e) {
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
		return rentabilidad;
	}

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
			if (buscarClientePorId(cliente.getCodigo()) == null) {
				throw new Exception("Ya hay un cliente con ese cÛdigo");
			}
			
			this.conn = darConexion();
			dao.setConn(conn);
			
			dao.crearCliente(cliente);
			
			//verificar 
			
			ret = buscarClientePorId(cliente.getCodigo());
			
			if(ret == null) {
				throw new Exception("No se guardÛ correctamente el cliente, revisar xd...");
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



}
