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
import vos.Cliente;




/**
 * Clase que expone servicios REST con ruta base: http://<ip o nombre del host>:8080/RotondAndes/rest/clientes/...
 * @author David Bautista
 */

@Path("clientes")
public class ClienteResorce {

	@XmlRootElement
	public static class RequestBody {
		@XmlElement Long[][] idsPedidos;
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
	public Response crearCliente( Cliente cliente) throws SQLException, Exception{
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


	@POST
	@Path("{id: \\d+}/pedido")
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response agregarPedido(@PathParam("id") Long id, @QueryParam("idProd") Long idProd, @QueryParam("idRest") Long idRestProd) throws SQLException, Exception {
		AlohAndesTM tm = new AlohAndesTM(getPath());

		Restaurante res = tm.darRestaurante(idRestProd);
		if(res == null) {
			return Response.status( 404 ).entity( "{ \"ERROR\": \""+ "NO SE ENCUENTRA EL RESTAURANTE" + "\"}" ).build( );
		}

		Cliente cliente = tm.darCliente(id);
		if(cliente == null) {
			return Response.status( 404 ).entity( "{ \"ERROR\": \""+ "NO SE ENCUENTRA EL CLIENTE" + "\"}" ).build( );
		}

		Producto prod = tm.darProducto(idProd, idRestProd);
		if(prod == null) {
			return Response.status( 404 ).entity( "{ \"ERROR\": \""+ "NO SE ENCUENTRA EL PRODUCTO" + "\"}" ).build( );
		}

		try {
			Pedido pedido = tm.agregarPedido(cliente, prod, res);

			return Response.status( 200 ).entity( pedido ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	 

	@POST
	@Path("{id: \\d+}/pedidos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( MediaType.APPLICATION_JSON )
	public Response agregarPedidos(@PathParam("id") Long id, RequestBody request) throws SQLException, Exception {
		AlohAndesTM tm = new AlohAndesTM(getPath());

		try {
			List<Pedido> pedidosMesa = tm.agregarPedidosMesa(id, request.idsPedidos);

			return Response.status( 200 ).entity( pedidosMesa ).build();	
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}



}
