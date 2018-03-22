package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Representa un tipo de operador de tipo vecino, el cual cumple con :
 * -Participa en la comunidad uniandina
 * -Vive por los alrededores de la univeridad
 * -Posee una vivienda la cual es presentada en una propuesta 
 */

public class Vecino extends Operador
{
	//Atributos
	/**
	 * Este atributo describe el seguro y en el se describen restricciones y limitaciones
	 */
	@JsonProperty(value="CaracteristicasDelSeguro")
	protected String CaracteristicasDelSeguro;
	@JsonProperty(value="numeroDeHabitaciones")
	private Integer numeroDeHabitaciones;
	@JsonProperty(value="ubicacion")
	private String ubicacion;
	public Integer getNumeroDeHabitaciones() {
		return numeroDeHabitaciones;
	}

	public void setNumeroDeHabitaciones(Integer numeroDeHabitaciones) {
		this.numeroDeHabitaciones = numeroDeHabitaciones;
	}

	public Vecino(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad
			,@JsonProperty(value="id") Long id,@JsonProperty(value="CaracteristicasDelSeguro")String seguro
			,@JsonProperty(value="numeroDeHabitaciones") int valor,@JsonProperty(value="ubicacion") String posi )
	{
		super(nombre,Operador.RESIDENTES_ALEDAÑOS,minTiempo,capacidad,id);
		this.CaracteristicasDelSeguro=seguro;
		this.numeroDeHabitaciones=valor;
		this.ubicacion=posi;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getCaracteristicasDelSeguro() {
		return CaracteristicasDelSeguro;
	}

	public void setCaracteristicasDelSeguro(String caracteristicasDelSeguro) 
	{
		CaracteristicasDelSeguro = caracteristicasDelSeguro;
	}
}
