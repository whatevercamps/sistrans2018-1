package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Factura
{
	//Atributos
	@JsonProperty(value="fecha")
	protected Date fecha;
	@JsonProperty(value="id")
	protected Long id;
	@JsonProperty(value="costoTotal")
	protected Double total;
	@JsonProperty(value="abonado")
	protected Double abonado;
	@JsonProperty(value="reserva")
	protected Reserva reserva;

	/*
	 * 
	 */
	public Factura() {

	}

	/*
	 * 
	 */
	public Factura(
			@JsonProperty(value="fecha") Date fecha,
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="costoTotal") Double costoTotal,
			@JsonProperty(value="reserva") Reserva reserva,
			@JsonProperty(value="abonado") Double abonado)

	{
		this.fecha = fecha;
		this.id = id;
		this.total = costoTotal;
		this.reserva = reserva;
		this.abonado = abonado;

	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the debe
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @return the abonado
	 */
	public Double getAbonado() {
		return abonado;
	}

	/**
	 * @param abonado the abonado to set
	 */
	public void setAbonado(Double abonado) {
		this.abonado = abonado;
	}

	/**
	 * @return the reserva
	 */
	public Reserva getReserva() {
		return reserva;
	}

	/**
	 * @param reserva the reserva to set
	 */
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	/**
	 * @param total the debe to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
}

