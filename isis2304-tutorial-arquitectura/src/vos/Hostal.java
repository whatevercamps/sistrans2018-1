package vos;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hostal extends Operador
{
	
	@JsonProperty(value="HoraApertura")
	protected String HoraApertura;	
	@JsonProperty(value="HoraClausura")
	protected String HoraClausura;
	@JsonProperty(value="CodIntendencia")
	protected Long CodIntendencia;


	public Long getCodIntendencia() {
		return CodIntendencia;
	}


	public void setCodIntendencia(Long codIntendencia) {
		CodIntendencia = codIntendencia;
	}


	public Hostal(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad
			,@JsonProperty(value="id") Long id,
			@JsonProperty(value="HoraApertura") String horaA,
			@JsonProperty(value="HoraClausura") String horaC,@JsonProperty(value="CodIntendencia") Long Cod
)
	{
		super(nombre,Operador.HOSTAL,minTiempo,capacidad,id);
		this.HoraApertura=horaA;
		this.HoraClausura=horaC;
		this.CodIntendencia=Cod;
		
	}


	public String getHoraApertura() {
		return HoraApertura;
	}


	public void setHoraApertura(String horaApertura) {
		HoraApertura = horaApertura;
	}


	public String getHoraClausura() {
		return HoraClausura;
	}


	public void setHoraClausura(String horaClausura) {
		HoraClausura = horaClausura;
	}


	

}

