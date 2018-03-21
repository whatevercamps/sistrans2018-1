package vos;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import java.time.LocalDateTime;
public class Hostal extends Operador
{
	
	@JsonProperty(value="HoraApertura")
	protected LocalDateTime HoraApertura;	
	@JsonProperty(value="HoraClausura")
	protected LocalDateTime HoraClausura;


	public Hostal(@JsonProperty(value="Nombre")String nombre,
			@JsonProperty(value="Tipo") int tipo,
			@JsonProperty(value="MinDeTiempo") double minTiempo,
			@JsonProperty(value="Capacidad")double capacidad
			,@JsonProperty(value="id") Long id,
			@JsonProperty(value="HoraApertura") LocalDateTime horaA,
			@JsonProperty(value="HoraClausura") LocalDateTime horaC)
	{
		super(nombre,tipo,minTiempo,capacidad,id);
		this.HoraApertura=horaA;
		this.HoraClausura=horaC;
		
	}


	public LocalDateTime getHoraApertura() {
		return HoraApertura;
	}


	public void setHoraApertura(LocalDateTime horaApertura) {
		HoraApertura = horaApertura;
	}


	public LocalDateTime getHoraClausura() {
		return HoraClausura;
	}


	public void setHoraClausura(LocalDateTime horaClausura) {
		HoraClausura = horaClausura;
	}


	

}

