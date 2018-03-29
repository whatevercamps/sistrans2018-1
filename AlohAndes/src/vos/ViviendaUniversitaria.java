package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *Clase que representa un operador de tipo vivienda Universitaria,
 *su responsabilidades son las de un operador admeas de :
 *-Brindar los servicios de sala de estudio, Gimnasio,Restaurante,Sala de esparcimiento
 *es obligación de esta clase si estos tienen un precio adicional
 */


public class ViviendaUniversitaria extends Operador
{
	/**
	 * Servicio de sala de estudio si su valor es 0 no tiene costo adicional
	 */
	@JsonProperty(value="servicioSalaDeEstudioPrecioAdicional")
	protected Double servicioSalaDeEstudioPrecioAdicional;
	/**
	 * Servicio de gimnasio si su valor es 0 no tiene costo adicional
	 */
	@JsonProperty(value="servicioGymPrecioAdicional")
	protected Double servicioGymPrecioAdicional;

	/**
	 * Servicio de restaurante si su valor es 0 no tiene costo adicional
	 */
	@JsonProperty(value="restaurantePrecioAdicional")
	protected Double restaurantePrecioAdicional;
	/**
	 * Servicio de sala de esparcimiento si su valor es 0 no tiene costo adicional
	 */ 
	@JsonProperty(value="salaDeEsparcimientoPrecioAdicional")
	protected Double salaDeEsparcimientoPrecioAdicional;

	public ViviendaUniversitaria(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad,
			@JsonProperty(value="id") Long id,@JsonProperty(value="restaurantePrecioAdicional") double precioRestaurante,
			@JsonProperty(value="servicioGymPrecioAdicional") double precioGym,
			@JsonProperty(value="servicioSalaDeEstudioPrecioAdicional") double precioSalaDeEstudio,
			@JsonProperty(value="salaDeEsparcimientoPrecioAdicional")double precioEsparcimiento,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo)
	{
		super(nombre,Operador.VIVIENDA_U,capacidad,id, minimoTiempo);
		this.servicioSalaDeEstudioPrecioAdicional=precioSalaDeEstudio;
		this.servicioGymPrecioAdicional=precioGym;
		this.restaurantePrecioAdicional=precioRestaurante;
		this.salaDeEsparcimientoPrecioAdicional=precioEsparcimiento;
	}

	public Double getServicioSalaDeEstudioPrecioAdicional() {
		return servicioSalaDeEstudioPrecioAdicional;
	}

	public void setServicioSalaDeEstudioPrecioAdicional(Double servicioSalaDeEstudioPrecioAdicional) {
		this.servicioSalaDeEstudioPrecioAdicional = servicioSalaDeEstudioPrecioAdicional;
	}

	public Double getServicioGymPrecioAdicional() {
		return servicioGymPrecioAdicional;
	}

	public void setServicioGymPrecioAdicional(Double servicioGymPrecioAdicional) {
		this.servicioGymPrecioAdicional = servicioGymPrecioAdicional;
	}

	public Double getRestaurantePrecioAdicional() {
		return restaurantePrecioAdicional;
	}

	public void setRestaurantePrecioAdicional(Double restaurantePrecioAdicional) {
		this.restaurantePrecioAdicional = restaurantePrecioAdicional;
	}

	public Double getSalaDeEsparcimientoPrecioAdicional() {
		return salaDeEsparcimientoPrecioAdicional;
	}

	public void setSalaDeEsparcimientoPrecioAdicional(Double salaDeEsparcimientoPrecioAdicional) {
		this.salaDeEsparcimientoPrecioAdicional = salaDeEsparcimientoPrecioAdicional;
	}
	


}

