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

import dao.DAOTablaReservas;
import tm.AlohAndesTM;
import vos.Cliente;
import vos.Reserva;




/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("clientes")
public class ClienteResorce {

	@XmlRootElement
	public static class DatosPago {
		@XmlElement Double CantPago;
	}


	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearCliente(Cliente cliente) throws SQLException, Exception{
		System.out.println("entreeeeee");
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			Cliente clienteNew = tm.crearCliente(cliente);
			return Response.status( 200 ).entity( clienteNew ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


	@GET
	@Path("{id: \\d+}/reservas")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darReservas(@PathParam("id")Long idCliente) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		try {
			List<Reserva> reservas = tm.darReservasPor(DAOTablaReservas.BUSQUEDA_CLIENTE, idCliente.toString());
			System.out.println("tam reservas: " + reservas.size());
			return Response.status( 200 ).entity( reservas ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@POST
	@Path("{id: \\d+}/reservas")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearReserva(@PathParam("id")Long idCliente, Reserva reserva) throws SQLException, Exception{
		System.out.println("entreeeeee");
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		try {
			tm.crearReserva(idCliente, reserva);

			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@DELETE
	@Path("{id: \\d+}/reservas/{idReserva: \\d+}")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response terminarReserva(@PathParam("id")Long idCliente, @PathParam("idReserva")Long idReserva, DatosPago datos) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			tm.terminarReserva(idCliente, idReserva, datos.CantPago);

			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}

}
