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
	@JsonProperty(value="ServicioSalaDeEstudioPrecioAdicional")
	protected Double ServicioSalaDeEstudioPrecioAdicional;
	/**
	 * Servicio de gimnasio si su valor es 0 no tiene costo adicional
	 */
	@JsonProperty(value="ServicioGymPrecioAdicional")
	protected Double ServicioGymPrecioAdicional;

	/**
	 * Servicio de restaurante si su valor es 0 no tiene costo adicional
	 */
	@JsonProperty(value="RestaurantePrecioAdicional")
	protected Double RestaurantePrecioAdicional;
	/**
	 * Servicio de sala de esparcimiento si su valor es 0 no tiene costo adicional
	 */ 
	@JsonProperty(value="SalaDeEsparcimientoPrecioAdicional")
	protected Double SalaDeEsparcimientoPrecioAdicional;

	public ViviendaUniversitaria(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad
			,@JsonProperty(value="id") Long id,@JsonProperty(value="RestaurantePrecioAdicional") double precioRestaurante
			,@JsonProperty(value="ServicioGymPrecioAdicional") double precioGym,
			@JsonProperty(value="ServicioSalaDeEstudioPrecioAdicional") double precioSalaDeEstudio,
			@JsonProperty(value="SalaDeEsparcimientoPrecioAdicional")double precioEsparcimiento)
	{
		super(nombre,Operador.VIVIENDA_U,minTiempo,capacidad,id);
		this.ServicioSalaDeEstudioPrecioAdicional=precioSalaDeEstudio;
		this.ServicioGymPrecioAdicional=precioGym;
		this.RestaurantePrecioAdicional=precioRestaurante;
		this.SalaDeEsparcimientoPrecioAdicional=precioEsparcimiento;
	}

	public Double getServicioSalaDeEstudioPrecioAdicional() {
		return ServicioSalaDeEstudioPrecioAdicional;
	}

	public void setServicioSalaDeEstudioPrecioAdicional(Double servicioSalaDeEstudioPrecioAdicional) {
		ServicioSalaDeEstudioPrecioAdicional = servicioSalaDeEstudioPrecioAdicional;
	}

	public Double getServicioGymPrecioAdicional() {
		return ServicioGymPrecioAdicional;
	}

	public void setServicioGymPrecioAdicional(Double servicioGymPrecioAdicional) {
		ServicioGymPrecioAdicional = servicioGymPrecioAdicional;
	}

	public Double getRestaurantePrecioAdicional() {
		return RestaurantePrecioAdicional;
	}

	public void setRestaurantePrecioAdicional(Double restaurantePrecioAdicional) {
		RestaurantePrecioAdicional = restaurantePrecioAdicional;
	}

	public Double getSalaDeEsparcimientoPrecioAdicional() {
		return SalaDeEsparcimientoPrecioAdicional;
	}

	public void setSalaDeEsparcimientoPrecioAdicional(Double salaDeEsparcimientoPrecioAdicional) {
		SalaDeEsparcimientoPrecioAdicional = salaDeEsparcimientoPrecioAdicional;
	}
	


}

