
function getCliente(form) {
	var TestVar = form.id.value;
	var url = "http://localhost:8080/RotondAndes/rest/clientes/" + TestVar;

	$.getJSON( url, function( json ) {
		console.log( "JSON Data: " + json.nombre );
		document.getElementById("rta").innerHTML = json.nombre;
	});
}