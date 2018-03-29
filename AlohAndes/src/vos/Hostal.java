package vos;


import org.codehaus.jackson.annotate.JsonProperty;

public class Hostal extends Operador
{
	
	@JsonProperty(value="horaApertura")
	protected String horaApertura;	
	@JsonProperty(value="horaClausura")
	protected String horaClausura;
	@JsonProperty(value="codIndendencia")
	protected Long codIndendencia;


	public Long getCodIntendencia() {
		return codIndendencia;
	}


	public void setCodIntendencia(Long codIntendencia) {
		codIndendencia = codIntendencia;
	}


	public Hostal(
			@JsonProperty(value="mombre")String nombre,
			@JsonProperty(value="tipo") int tipo,
			@JsonProperty(value="capacidad")double capacidad,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="horaApertura") String horaA,
			@JsonProperty(value="horaClausura") String horaC,
			@JsonProperty(value="codIndendencia") Long Cod,
			@JsonProperty(value="minimoTiempo") Integer minimoTiempo
)
	{
		super(nombre,Operador.HOSTAL,capacidad,id, minimoTiempo);
		this.horaApertura=horaA;
		this.horaClausura=horaC;
		this.codIndendencia=Cod;
		
	}


	public String getHoraApertura() {
		return horaApertura;
	}


	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}


	public String getHoraClausura() {
		return horaClausura;
	}


	public void setHoraClausura(String horaClausura) {
		horaClausura = horaClausura;
	}


	

}

