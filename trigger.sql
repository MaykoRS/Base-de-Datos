# Selecciono la BD
use vuelos;


# cargar_instanciasdevuelo carga automaticamente las instancias de vuelo asociadas a una nueva salida de vuelo programada.
# Este trigger se activa cuando se inserta una nueva salida y cargara todas las instancias de vuelos asociadas a dicha salida,
# por el periodo de un año a partir de la fecha actual. El campo "estado" de todas las instancias cargadas inician con "a tiempo".


delimiter !

# Creo el trigger
CREATE TRIGGER cargar_instanciasdevuelo 
# Se activa despues de insertar una nueva salida
AFTER INSERT ON salidas

FOR EACH ROW
BEGIN

	DECLARE fechaDesde DATE;
	DECLARE fechaHasta DATE;
	DECLARE offset INT;
	
	SET fechaDesde=CURDATE();

	SET offset=ABS(NEW.dia - DAYOFWEEK(fechaDesde));
	
	IF (DAYOFWEEK(fechaDesde) <> NEW.dia) THEN
		SELECT DATE_ADD(fechaDesde,INTERVAL (offset) DAY) INTO fechaDesde;
	END IF;
	
	SET fechaHasta = (SELECT DATE_ADD(fechaDesde,INTERVAL 1 YEAR));

	WHILE (DATEDIFF(fechaHasta,fechaDesde)>0) DO
		INSERT INTO instancias_vuelo( vuelo, fecha, dia, estado) 
			VALUES (NEW.vuelo,fechaDesde,NEW.dia,'a tiempo');
		SELECT DATE_ADD(fechaDesde,INTERVAL 7 DAY) INTO fechaDesde;
	END WHILE;

END; !
delimiter ;
# restablece ';' como delimitador de sentencias
delimiter ;