package rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import dao.DAOTablaReservas;
import tm.AlohAndesTM;
import vos.Cliente;
import vos.Propuesta;
import vos.Reserva;




/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("consultAndes")
public class ConsultasRest {

	public class Respuesta{
		
		public class Dato{
			@JsonProperty(value="nombre")
			protected String nombre;
			
			@JsonProperty(value="valor")
			protected String valor;
			
			public Dato(String pNombre, String pValor) {
				this.nombre = pNombre;
				this.valor = pValor;
			}
		}
		
		
		@JsonProperty(value="nombre")
		protected String nombre;
		

		@JsonProperty(value="datos")
		protected List<Dato> datos;
		
		public Respuesta(String nombre) {
			this.nombre = nombre; 
			this.datos = new ArrayList<Dato>();
		}
		
		public void agregarDato(String nombre, String valor) {
			this.datos.add(new Dato(nombre, valor));
		}
	}
	
	public class RespuestaConsulta2 {
		
		@JsonProperty(value="propuesta")
		protected Propuesta propuesta;
		
		@JsonProperty(value="cantidad")
		protected Integer cantidad;
		
		public RespuestaConsulta2(Propuesta propuesta, Integer cantidad) {
			this.propuesta = propuesta;
			this.cantidad = cantidad;
		}
	}
	

	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}


	@GET
	@Path("RFC1")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons1() throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		try {
			List<Respuesta> rta = tm.reqConsUno();
			return Response.status( 200 ).entity( rta ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path("RFC2")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons2() throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		try {
			List<RespuestaConsulta2> rta = tm.reqConsDos(this);
			return Response.status( 200 ).entity( rta ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path("RFC3")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons3() throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		try {
			Respuesta rta = tm.reqConsTres(this);
			return Response.status( 200 ).entity( rta ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path("RFC5/{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons5(@PathParam("id") Integer tipo) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			List<Respuesta> rta = tm.reqConsCinco(this, tipo);
			return Response.status( 200 ).entity( rta ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	@GET
	@Path("RFC6/{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons6(@PathParam("id") Long idCliente) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			List<Respuesta> rta = tm.reqConsSeis(this, idCliente);
			return Response.status( 200 ).entity( rta ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path("RFC7")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqCons7(@QueryParam("tiempo") String tiempo, @QueryParam("unidad") Integer unidad) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			String[] datos = tm.reqConsSiete(tiempo, unidad);
			return Response.status( 200 ).entity( "{ \"FECHA_MAYOR_DEMANDA\": \" " + datos[0] +" \", \n \"FECHA_MAYOR_INGRESO\": \" " + datos[1] + " \"}" ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	
	@GET
	@Path("RFC7MEJORADO")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response reqConsMejorado7(@QueryParam("tiempo") String tiempo) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			Respuesta rta = tm.reqConsSieteMejorado(this, tiempo);
			return Response.status( 200 ).entity(rta).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
