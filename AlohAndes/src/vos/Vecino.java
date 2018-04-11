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
	@JsonProperty(value="caracteristicasDelSeguro")
	protected String caracteristicasDelSeguro;
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

	public Vecino(@JsonProperty(value="mombre")String nombre,
			@JsonProperty(value="tipo") int tipo,
			@JsonProperty(value="capacidad")double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="caracteristicasDelSeguro")String seguro,
			@JsonProperty(value="numeroDeHabitaciones") int valor,
			@JsonProperty(value="ubicacion") String posi,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo)
	{
		super(nombre,Operador.RESIDENTES_ALEDANIOS, capacidad, id, minimoTiempo);
		this.caracteristicasDelSeguro=seguro;
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
		return caracteristicasDelSeguro;
	}

	public void setCaracteristicasDelSeguro(String caracteristicasDelSeguro) 
	{
		this.caracteristicasDelSeguro = caracteristicasDelSeguro;
	}
}
