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

@Path("consultAndes")
public class consultasRest {


	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}


	@GET
	@Path("RFC7")
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response darReservas(@QueryParam("tiempo") String tiempo, @QueryParam("unidad") Integer unidad) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			String[] datos = tm.reqConsSiete(tiempo, unidad);
			return Response.status( 200 ).entity( "{ \"FECHA_MAYOR_DEMANDA\": \" " + datos[0] +" \", \n \"FECHA_MAYOR_INGRESO\": \" " + datos[1] + " \"}" ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
