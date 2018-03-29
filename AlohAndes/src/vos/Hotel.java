package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hotel extends Operador
{	//Atributos
	@JsonProperty(value="codigoSuperIntendencia")
	protected String codigoSuperIntendecnia;	
	@JsonProperty(value="tamanio")
	protected String tamanio;

	public Hotel(@JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="tipo") int tipo,
			@JsonProperty(value="capacidad")double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="codigoSuperIntendencia") String codigo,
			@JsonProperty(value="tamanio") String tamanio,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo)
	{
		super(nombre,Operador.HOTEL,capacidad,id, minimoTiempo);
		this.codigoSuperIntendecnia=codigo;
		this.tamanio=tamanio;

	}
	//Metodos

	public String getCodigoSuperIntendencia() {
		return codigoSuperIntendecnia;
	}

	public void setCodigoSuperIntendencia(String codigoSuperIntendecnia) {
		this.codigoSuperIntendecnia = codigoSuperIntendecnia;
	}

	public String getTamanio() {
		return tamanio;
	}

	public void setTamanio(String tamanio) {
		this.tamanio = tamanio;
	}



}

