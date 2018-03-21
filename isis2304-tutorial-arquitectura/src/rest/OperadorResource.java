package rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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

import tm.AlohAndesTM;
import vos.Apartamento;
import vos.Cliente;
import vos.Operador;




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
	@Consumes({ MediaType.APPLICATION_JSON } )
	@Produces({ MediaType.APPLICATION_JSON } )
	public Response crearOperador(Operador operador) throws SQLException, Exception{
		AlohAndesTM tm = new AlohAndesTM(getPath());
		
		if(operador.getTipo() == Operador.APARTAMENTO)
		{
			try {
				Apartamento ret = tm.crearOperadorApartamento(operador);
				return Response.status( 200 ).entity( ret ).build();	
			}catch( Exception e )
			{
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
		}
		
	}




}
