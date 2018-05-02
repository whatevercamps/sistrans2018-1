package vos;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;



/**
 * 
 * Clase que representa una propuesta,una propuesta es quuella que que es ofrecida por un operador ademas se encarga de manejar los servicios. 
 */


public class Propuesta
{
	public final static int HABITACION_SENCILLA=1;
	public final static int HABITACION_COMPARTIDA=2;
	public final static int HABITACION_SUITE_SENCILLA=3;
	public final static int HABITACION_SUITE_COMPARTIDA=4;
	public final static int HABITACION_SEMI_SUITE_SENCILLA=5;
	public final static int HABITACION_SEMI_SUITE_COMPARTIDA=6;
	public final static int INHABILITADA = 1000;

	/**
	 * Identificador de una propuesta
	 */
	@JsonProperty(value="id")
	protected Long id ;
	@JsonProperty(value="nombre")
	protected String nombre;
	@JsonProperty(value="costo")
	protected Double costo;
	@JsonProperty(value="tipo")
	protected Integer tipo;
	@JsonProperty(value="diasCancelancionOportuna")
	protected Integer diasCancelacion;
	
	/**
	 * Representa la lista de servicios adicionales asociadas con una propuesta 
	 * 
	 */
	@JsonProperty(value="servicios")
	protected List<Servicio> servicios;
	


	///Constructor
	public Propuesta() {
	}

	public Propuesta(@JsonProperty(value="Operador") Operador operador,@JsonProperty(value="id")Long id,
			@JsonProperty(value="Nombre") String nombre,@JsonProperty(value="costo") double costo, 
			@JsonProperty(value="diasCancelancionOportuna") Integer diasCancelacion,
			@JsonProperty(value="tipo") Integer tipo,
			@JsonProperty(value="Servicios") List<Servicio> servicios)
	{

		this.diasCancelacion = diasCancelacion;
		this.servicios = new ArrayList<>();
		if (servicios != null)
			this.servicios = servicios;
		this.id=id;
		this.nombre=nombre;
		this.costo=costo;
		if(tipo==HABITACION_SENCILLA||tipo==HABITACION_COMPARTIDA||tipo==HABITACION_SEMI_SUITE_COMPARTIDA
				   ||tipo==HABITACION_SEMI_SUITE_SENCILLA||tipo==HABITACION_SUITE_COMPARTIDA||tipo==HABITACION_SUITE_SENCILLA)
		this.tipo = tipo;
	}
	// Getters y setters





	/**
	 * 
	 * @return todos los servicios adicionales asociados a una propuesta
	 */
	public List<Servicio> getServicios() {
		if(this.servicios == null) {
			this.servicios = new ArrayList<>();
		}
		return this.servicios;
	}

	/**
	 * 
	 * @return el identificador de una propuesta
	 */
	public Long getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the costo
	 */
	public Double getCosto() {
		return costo;
	}

	/**
	 * @param costo the costo to set
	 */
	public void setCosto(Double costo) {
		this.costo = costo;
	}

	

	

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(List<Servicio> servicios) {
		servicios = servicios;
	}

	/**
	 * 
	 * @return el tipo de una propuesta
	 */
	public Integer getTipo() {
		return this.tipo;
	}
	
	/**
	 * 
	 * @return el nombre de una propuesta
	 */
	public String getNombre() {
		return this.nombre;
	}
	/**
	 * 
	 * @param newServicios nueva lista de servicios adicionales a agregar
	 */

	public void addAllServicios(Set<Servicio> newServicios) {
		if (this.servicios == null) {
			this.servicios = new ArrayList<>();
		}
		this.servicios.addAll(newServicios);
	}
	/**
	 * 
	 * @param newServicios lista de servicios adicionales a elliminar
	 */
	public void removeAllServicios(Set<Servicio> newServicios) {
		if(this.servicios == null) {
			return;
		}

		this.servicios.removeAll(newServicios);
	}

	/**
	 * 
	 * @param mySevicioAgua cambio el estado respecto al servicio
	 */





	public void addServicios(Servicio newServicios) {
		if(this.servicios == null) {
			this.servicios = new ArrayList<>();
		}

		this.servicios.add(newServicios);
	}



	
	public Integer getDiasCancelacion() {
		return diasCancelacion;
	}

	public void setDiasCancelacion(Integer diasCancelacion) {
		this.diasCancelacion = diasCancelacion;
	}

	public void removeServicios(Servicio oldServicios) {
		if(this.servicios == null)
			return;

		this.servicios.remove(oldServicios);
	}

}

