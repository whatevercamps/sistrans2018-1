package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class HabitacionHotel extends Operador
{	//Atributos
	@JsonProperty(value="CodigoSuperIntendencia")
	protected String CodigoSuperIntendecnia;	
	@JsonProperty(value="TipoHabitacion")
	protected String TipoHabitacion;
	@JsonProperty(value="Capacidad")
	protected Double Capacidad;
	@JsonProperty(value="Tamanio")
	protected String Tamanio;

	public HabitacionHotel(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad
			,@JsonProperty(value="id") Long id,
			@JsonProperty(value="CodigoSuperIntendencia") String codigo,@JsonProperty(value="TipoHabitacion") String tipoH
			,@JsonProperty(value="Capacidad") double capacidadH,
			@JsonProperty(value="Tamanio") String tamanio)
	{
		super(nombre,tipo,minTiempo,capacidad,id);
		this.CodigoSuperIntendecnia=codigo;
		this.TipoHabitacion=tipoH;
		this.Capacidad=capacidadH;
		this.Tamanio=tamanio;

	}
	//Metodos

	public String getCodigoSuperIntendecnia() {
		return CodigoSuperIntendecnia;
	}

	public void setCodigoSuperIntendecnia(String codigoSuperIntendecnia) {
		CodigoSuperIntendecnia = codigoSuperIntendecnia;
	}

	public String getTipoHabitacion() {
		return TipoHabitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		TipoHabitacion = tipoHabitacion;
	}

	public Double getCapacidad() {
		return Capacidad;
	}

	public void setCapacidad(double capacidad) {
		Capacidad = capacidad;
	}

	public String getTamanio() {
		return Tamanio;
	}

	public void setTamanio(String tamanio) {
		Tamanio = tamanio;
	}



}

