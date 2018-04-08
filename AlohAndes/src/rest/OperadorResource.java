package rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.sun.xml.internal.ws.api.message.Message;

import tm.AlohAndesTM;
import vos.Apartamento;
import vos.Cliente;
import vos.Hostal;
import vos.Hotel;
import vos.Operador;
import vos.Propuesta;
import vos.Vecino;
import vos.ViviendaUniversitaria;




/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("operadores")
public class OperadorResource {


	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	@POST
	@Path("/apartamentos")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearApartamento(Apartamento apartamento) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			tm.crearOperadorApartamento(apartamento);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	@POST
	@Path("/hoteles")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearHotel(Hotel hotel) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			tm.crearOperadorHotel(hotel);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@POST
	@Path("/hostales")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearApartamento(Hostal hostal) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			tm.crearOperadorHostal(hostal);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@POST
	@Path("/vecinos")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearVecino(Vecino vecino) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			tm.crearOperadorVecino(vecino);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@POST
	@Path("/ViviendasUniversitarias")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			tm.crearOperadorViviendaUniversitaria(viviendaUniversitaria);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@POST
	@Path("/{id: \\d+}")
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearPropuesta(@PathParam("id") Long idOperador, Propuesta propuesta ) throws SQLException, Exception{
		System.out.println("entreeeeee");
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			tm.crearPropuesta(idOperador, propuesta);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@DELETE
	@Path("/{id: \\d+}/propuestas/{idPropuesta: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response retirarPropuesta(@PathParam("id") Long idOperador, @PathParam("idPropuesta") Long idPropuesta) throws SQLException, Exception{
		System.out.println("entreeeeee");
		AlohAndesTM tm = new AlohAndesTM(getPath());
		try {
			tm.retirarPropuesta(idOperador, idPropuesta);
			return Response.status( 200 ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
