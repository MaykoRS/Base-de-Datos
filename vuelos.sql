/*
#-------------------------------------------------------------------------
#Comisión:
#Rodriguez Samana, Mayko (L.U: 109130)
#Delgado, Romina Soledad (L.U: 104218)
#Base de Datos
#-------------------------------------------------------------------------

#Archivo batch (vuelos.sql) para la creación de la 

#Base de datos del Proyecto N°2 de SQL
*/

# Creo la Base de Datos
CREATE DATABASE vuelos;

#Selecciono la base de datos sobre la cual voy a hacer modificaciones
USE vuelos;

########################################################################################
########################################################################################
#-----------------------------------Entidades------------------------------------------#
########################################################################################
########################################################################################
#-------------------------------------------------------------------------
# Creación Tablas para las entidades
CREATE TABLE ubicaciones(
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	huso TINYINT NOT NULL,

	CONSTRAINT pk_ubicaciones
	PRIMARY KEY (pais,estado,ciudad),

	CONSTRAINT chk_ubicaciones_huso CHECK (huso > -13 AND huso < 13)
) ENGINE=InnoDB;


CREATE TABLE aeropuertos(
	codigo VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,

	CONSTRAINT pk_aeropuertos
	PRIMARY KEY (codigo),

	CONSTRAINT FK_aeropuertos_ubicaciones
	FOREIGN KEY (pais,estado,ciudad) REFERENCES ubicaciones(pais,estado,ciudad)


) ENGINE=InnoDB;

CREATE TABLE vuelos_programados(
	numero VARCHAR(45) NOT NULL,
	aeropuerto_salida VARCHAR(45) NOT NULL,
	aeropuerto_llegada VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_vuelos_programados
	PRIMARY KEY (numero),
	
	CONSTRAINT FK_vuelos_programados_salida
	FOREIGN KEY(aeropuerto_salida) REFERENCES aeropuertos(codigo),
	
	CONSTRAINT FK_vuelos_programados_llegada	
	FOREIGN KEY(aeropuerto_llegada) REFERENCES aeropuertos(codigo)
)ENGINE=InnoDB;

CREATE TABLE modelos_avion(
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas SMALLINT unsigned NOT NULL, 
	cant_asientos INT unsigned NOT NULL,
	
	CONSTRAINT pk_modelos_avion
	PRIMARY KEY(modelo)
)ENGINE=InnoDB;

CREATE TABLE salidas(
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	hora_sale TIME NOT NULL,
	hora_llega TIME NOT NULL,
	modelo_avion VARCHAR(45) NOT NULL,

	CONSTRAINT pk_salidas
	PRIMARY KEY (vuelo,dia),

	CONSTRAINT FK_salidas_vuelo
	FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero),

	CONSTRAINT FK_salidas_modelo_avion
	FOREIGN KEY (modelo_avion) REFERENCES modelos_avion(modelo)

)ENGINE=InnoDB;

CREATE TABLE instancias_vuelo(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	estado VARCHAR(45) ,

	CONSTRAINT pk_instancias_vuelo
	PRIMARY KEY (vuelo,fecha),

	CONSTRAINT  FK_instancias_vuelo_salidas
	FOREIGN KEY (vuelo,dia) REFERENCES salidas(vuelo,dia)

)ENGINE=InnoDB;

CREATE TABLE clases(
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL(2,2) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_clases
	PRIMARY KEY (nombre),
	
	CONSTRAINT chk_clases_porcentaje CHECK (porcentaje >= 0 AND porcentaje <= 0.99)
)ENGINE=InnoDB;

CREATE TABLE comodidades(
	codigo INT unsigned NOT NULL,
	descripcion TINYTEXT NOT NULL,
	
	CONSTRAINT pk_comodidades
	PRIMARY KEY(codigo)
)ENGINE=InnoDB;

CREATE TABLE pasajeros(
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT unsigned NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_pasajeros
	PRIMARY KEY(doc_tipo,doc_nro)
)ENGINE=InnoDB;

CREATE TABLE empleados(
	legajo INT unsigned NOT NULL,
	password CHAR(32) NOT NULL, 
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT unsigned NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_empleados
	PRIMARY KEY(legajo)
)ENGINE=InnoDB;

CREATE TABLE reservas(
	numero INT unsigned NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT unsigned NOT NULL,
	legajo INT unsigned NOT NULL,
	
	CONSTRAINT pk_reservas
	PRIMARY KEY(numero),
	
	CONSTRAINT  fk_reservas_pasajeros
	FOREIGN KEY(doc_tipo,doc_nro) REFERENCES pasajeros(doc_tipo,doc_nro),

	CONSTRAINT fk_reservas_empleados
	FOREIGN KEY(legajo) REFERENCES empleados(legajo)	
)ENGINE=InnoDB;

########################################################################################
########################################################################################
#-----------------------------------Relaciones-----------------------------------------#
########################################################################################
########################################################################################
#-------------------------------------------------------------------------
# Creación Tablas para las relaciones

CREATE TABLE brinda(
	vuelo VARCHAR(45) NOT NULL, 
	dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL, 
	clase VARCHAR(45) NOT NULL, 
	precio DECIMAL(7,2) unsigned NOT NULL, /*# PREGUNTAR */ 
	cant_asientos INT unsigned NOT NULL,
	
	CONSTRAINT pk_brinda
	PRIMARY KEY(vuelo,dia,clase),

	CONSTRAINT fk_brinda_salidas
	FOREIGN KEY (vuelo,dia) REFERENCES salidas(vuelo,dia),

	CONSTRAINT fk_brinda_clases
	FOREIGN KEY(clase) REFERENCES clases(nombre)
	
)ENGINE=InnoDB;

CREATE TABLE posee(
	clase VARCHAR(45) NOT NULL,
	comodidad INT unsigned NOT NULL,
	
	CONSTRAINT pk_posee
	PRIMARY KEY(clase,comodidad),
	
	CONSTRAINT fk_posee_clase
	FOREIGN KEY(clase) REFERENCES clases(nombre),

	CONSTRAINT fk_posee_comodidad
	FOREIGN KEY(comodidad) REFERENCES comodidades(codigo)
	
)ENGINE=InnoDB;

CREATE TABLE reserva_vuelo_clase(
	numero INT unsigned NOT NULL AUTO_INCREMENT,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_reserva_vuelo_clase
	PRIMARY KEY (numero, vuelo, fecha_vuelo),
	
	CONSTRAINT fk_reserva_vuelo_clase_numero
	FOREIGN KEY (numero) REFERENCES reservas(numero),
	
	CONSTRAINT fk_reserva_vuelo_clase_instancias_vuelo
	FOREIGN KEY (vuelo, fecha_vuelo) REFERENCES instancias_vuelo(vuelo, fecha),
	
	CONSTRAINT fk_reserva_vuelo_clase_clase
	FOREIGN KEY (clase) REFERENCES clases(nombre)

)ENGINE=InnoDB;

CREATE TABLE asientos_reservados(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	clase VARCHAR (45) NOT NULL,
	cantidad INT unsigned NOT NULL,

	CONSTRAINT pk_asientos_reservados
	PRIMARY KEY(vuelo,fecha,clase),

	CONSTRAINT fk_asientos_reservados_instancias_vuelo
	FOREIGN KEY(vuelo,fecha) REFERENCES instancias_vuelo(vuelo,fecha)
		ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT fk_asientos_reservados_clases
	FOREIGN KEY(clase) REFERENCES clases(nombre)
		ON DELETE RESTRICT ON UPDATE CASCADE

)ENGINE=InnoDB;


########################################################################################
########################################################################################
#-----------------------------------Vistas---------------------------------------------#
########################################################################################
########################################################################################
#-------------------------------------------------------------------------
#Creación de vista 
# -Número de vuelo,modelo de avión, fecha, dia, hora de salida, hora de llegada y tiempo
# estimado de viaje(se asume que no hay vuelo que dure más de 24 hs.)
# -Código, nombre, ciudad, estado y pais del aeropuerto tanto de salida, como de llegada.
# -Precio del pasaje y cantidad de asientos disponibles en cada clase brindada por el vuelo.
#  (Número de asientos: (cantidad de asientos que brinda el vuelo en una clase más el 
#                       porcentaje asociado a la clase) menos (cantidad total de reservas
#			hechas para el vuelo y la clase) ).

CREATE VIEW vuelos_aeropuertos as

	SELECT	instancias_vuelo.vuelo AS Numero_Vuelo, 
		salidas.modelo_avion AS Modelo_Avion, 
		instancias_vuelo.fecha AS Fecha, 
		instancias_vuelo.dia AS Dia,
		salidas.hora_sale AS Hora_Salida,
		salidas.hora_llega AS Hora_Llegada,
		#SEC_TO_TIME(TIMESTAMPDIFF(SECOND, salidas.hora_sale, salidas.hora_llega)) AS Tiempo_Estimado,
		IF(salidas.hora_llega > salidas.hora_sale, TIMEDIFF(salidas.hora_llega,salidas.hora_sale), TIME(TIMEDIFF('24:00:00', salidas.hora_sale) + salidas.hora_llega)) AS Tiempo_Estimado,
		
		aSalida.codigo AS Codigo_Aeropuerto_Salida,
		aSalida.nombre AS Nombre_Aeropuerto_Salida,
		aSalida.ciudad AS Ciudad_Aeropuerto_Salida,
		aSalida.estado AS Estado_Aeropuerto_Salida,
		aSalida.pais   AS Pais_Aeropuerto_Salida,

		aLlegada.codigo AS Codigo_Aeropuerto_Llegada,
		aLlegada.nombre AS Nombre_Aeropuerto_Llegada,
		aLlegada.ciudad AS Ciudad_Aeropuerto_Llegada,
		aLlegada.estado AS Estado_Aeropuerto_Llegada,
		aLlegada.pais   AS Pais_Aeropuerto_Llegada
	FROM 	
		instancias_vuelo 
		JOIN salidas ON instancias_vuelo.vuelo = salidas.vuelo AND instancias_vuelo.dia = salidas.dia
		JOIN vuelos_programados ON vuelos_programados.numero = salidas.vuelo
		JOIN aeropuertos as aSalida ON aSalida.codigo=vuelos_programados.aeropuerto_salida
		JOIN aeropuertos as aLlegada ON aLlegada.codigo=vuelos_programados.aeropuerto_llegada;
		


CREATE VIEW reservas_clase AS
SELECT 
	b.precio as Precio, 
	ROUND(b.cant_asientos + b.cant_asientos*clases.porcentaje,0) - COUNT(rvc.numero) 
	AS Cant_asientos_disponibles,
	iv.fecha as Fecha,
	clases.nombre as Clase,
	b.vuelo as Vuelo

FROM
	instancias_vuelo as iv
	JOIN brinda as b ON iv.vuelo = b.vuelo AND iv.dia = b.dia
	JOIN clases ON b.clase = clases.nombre
	LEFT JOIN reserva_vuelo_clase as rvc ON rvc.vuelo=iv.vuelo AND rvc.fecha_vuelo=iv.fecha AND rvc.clase=b.clase
	
	GROUP BY
		b.precio,iv.fecha,clases.nombre,b.vuelo;


/*Muestra el contenido de las 2 vistas anteriores: vuelos_aeropuertos, reservas_clase*/
CREATE VIEW vuelos_disponibles AS
SELECT 	
	rClase.vuelo,
	vAeropuerto.Modelo_Avion,rClase.Fecha,
	vAeropuerto.Dia,
	vAeropuerto.Hora_Salida,
	vAeropuerto.Hora_Llegada,
	vAeropuerto.Tiempo_Estimado,
	   
	vAeropuerto.Codigo_Aeropuerto_Salida,
	vAeropuerto.Nombre_Aeropuerto_Salida,
	vAeropuerto.Ciudad_Aeropuerto_Salida,
	vAeropuerto.Estado_Aeropuerto_Salida,
	vAeropuerto.Pais_Aeropuerto_Salida,

	vAeropuerto.Codigo_Aeropuerto_Llegada,
	vAeropuerto.Nombre_Aeropuerto_Llegada,
	vAeropuerto.Ciudad_Aeropuerto_Llegada,
	vAeropuerto.Estado_Aeropuerto_Llegada,
	vAeropuerto.Pais_Aeropuerto_Llegada,
	rClase.Precio,
	rClase.Cant_asientos_disponibles,
	rClase.Clase
	   
FROM
	reservas_clase AS rClase
	JOIN vuelos_aeropuertos AS vAeropuerto 
	ON rClase.Fecha=vAeropuerto.Fecha AND rClase.Vuelo=vAeropuerto.Numero_Vuelo;


########################################################################################
########################################################################################
#-----------------------------------Usuarios-------------------------------------------#
########################################################################################
########################################################################################
#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios
#-------------------------------------------------------------------------
#admin
# El usuario 'admin' tiene acceso total a todas las tablas de 
# la B.D. vuelos, puede conectarse solo desde la computadora 
# donde se encuentra el servidor de MySQL (localhost), el password de su 
# cuenta es 'admin' y puede otorgar privilegios.
CREATE USER admin@localhost IDENTIFIED BY 'admin';
#Privilegios
GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@'localhost' WITH GRANT OPTION;


#-------------------------------------------------------------------------
#cliente
# El usuario 'cliente' esta destinado a permitir un acceso público para
# satisfacer las consultas sobre vuelos de los clientes de la empresa, ya
# sean tanto personas como agencias de viaje.
# Visión restringida: solo las disponibilidad de vuelos. Solo acceso de lectura.
# Puede conectarse desde cualquier dominio.
# El password de su cuenta es 'cliente'.

CREATE USER 'cliente'@'%' IDENTIFIED BY 'cliente';
#Privilegios
GRANT SELECT ON vuelos.vuelos_disponibles TO 'cliente'@'%';

delimiter !

CREATE PROCEDURE validarDatos(IN numVuelo VARCHAR(45), IN fecha DATE, IN clase VARCHAR(45), 
							IN tipoDoc VARCHAR(45), IN numDoc INT unsigned, IN legajo INT unsigned, 
							OUT datosValidos BOOLEAN, OUT msjeError VARCHAR(100))
BEGIN
	IF EXISTS(SELECT * FROM vuelos_disponibles as vd WHERE vd.vuelo = numVuelo AND vd.Fecha = fecha)
	THEN
		IF EXISTS(SELECT * FROM clases WHERE nombre=clase)
		THEN
			IF EXISTS(SELECT * FROM pasajeros as p WHERE p.doc_tipo = tipoDoc AND p.doc_nro = numDoc)
			THEN
				IF EXISTS(SELECT * FROM empleados as e WHERE e.legajo=legajo)
				THEN
					SET datosValidos=true;
				ELSE
					SET datosValidos=false;
					SET msjeError='Error: El empleado no existe en la base de datos';
				END IF;
			ELSE
				SET datosValidos=false;
				SET msjeError='Error: El pasajero no existe en la base de datos';
			END IF;
		ELSE
			SET datosValidos=false;
			SET msjeError='Error: La clase seleccionada no existe en la base de datos';
		END IF;	
	ELSE
		SET datosValidos = false;
		SET msjeError = 'Error: El vuelo seleccionado no existe en la base de datos';
	END IF;
END; !

CREATE PROCEDURE obtener_asientos_disponibles(IN numVuelo VARCHAR(45), IN fecha DATE, IN clase VARCHAR(45),
												IN asientosReservados INT UNSIGNED, 
												OUT asientosReales INT UNSIGNED, OUT sobreventa_disponibles INT UNSIGNED)
BEGIN
	DECLARE asientos_brinda INT UNSIGNED;

	SELECT b.cant_asientos INTO asientos_brinda FROM brinda as b JOIN vuelos_disponibles as vd ON (b.vuelo = numVuelo AND vd.vuelo = numVuelo AND b.dia = vd.Dia AND b.clase = clase AND vd.Clase = clase AND vd.Fecha = fecha);
	SELECT ROUND(asientos_brinda*c.porcentaje,0) as asi INTO sobreventa_disponibles FROM clases as c JOIN vuelos_disponibles as vd ON c.nombre = clase AND vd.vuelo = numVuelo AND vd.Fecha = fecha AND vd.Clase = clase;
	IF asientos_brinda >= asientosReservados THEN
		SET asientosReales = asientos_brinda - asientosReservados;
	ELSE  #No hay asientos reales disponibles
		SET asientosReales = 0;
		IF sobreventa_disponibles >= asientosReservados - asientos_brinda THEN
			SET sobreventa_disponibles = sobreventa_disponibles - (asientosReservados - asientos_brinda);
		ELSE
			SET sobreventa_disponibles = 0;
		END IF;
	END IF;

END; !

CREATE PROCEDURE reserva_ida(IN numVuelo VARCHAR(45),IN fecha DATE, IN clase VARCHAR(45), IN tipoDoc VARCHAR(45), 
								IN numDoc int unsigned ,IN legajo INT unsigned, OUT result VARCHAR(100))
BEGIN
	 DECLARE fechaVencimiento DATE;

	 DECLARE asientosReales int unsigned;
	 DECLARE asientosReservados int unsigned;
	 DECLARE sobreventa_disponibles int unsigned;
	 DECLARE estado VARCHAR(45);
	 DECLARE datosValidos BOOLEAN;
	 DECLARE msjeError VARCHAR(100);
	 
	 DECLARE codigo_SQL CHAR(5) DEFAULT '00000';
	 DECLARE codigo_MYSQL INT DEFAULT 0;
	 DECLARE mensaje_error TEXT;
     DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  BEGIN  # Si se produce una SQLEXCEPTION, se retrocede la transacción con ROLLBACK 
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, 
		        codigo_MySQL, codigo_SQL,  mensaje_error;
		        SET result = 'Error SQL';
        ROLLBACK;
	  END;
		 
	START TRANSACTION;	# Comienza la transacción  
	SELECT SUBDATE(fecha,INTERVAL 15 DAY) INTO fechaVencimiento;

	call validarDatos(numVuelo,fecha,clase,tipoDoc,numDoc,legajo,datosValidos,msjeError);
	IF datosValidos THEN
		SELECT  cantidad INTO asientosReservados FROM asientos_reservados as ar WHERE ar.vuelo=numVuelo AND ar.fecha=fecha AND ar.clase=clase FOR UPDATE;
		call obtener_asientos_disponibles(numVuelo,fecha,clase,asientosReservados,asientosReales,sobreventa_disponibles);
		IF asientosReales > 0 THEN
			SELECT 'confirmada' INTO estado;
			INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,estado,tipoDoc,numDoc,legajo);
			INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVuelo,fecha,clase);
			UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVuelo AND ar.fecha=fecha AND ar.clase=clase;
			SELECT 'La reserva se realizo con exito. Estado de la reserva: Confirmada' AS resultado INTO result;
		ELSE
			IF sobreventa_disponibles > 0 THEN #Si se puede sobrevender asientos
				SELECT 'en espera' INTO estado;
				INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,estado,tipoDoc,numDoc,legajo);
				INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVuelo,fecha,clase);
				UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVuelo AND ar.fecha=fecha AND ar.clase=clase;
				SELECT 'La reserva se realizo con exito. Estado de la reserva: En espera' AS resultado INTO result;
			ELSE
				SELECT 'No se pudo realizar la reserva: No hay asientos disponibles' AS resultado INTO result;
			END IF;
		END IF;
	ELSE
		SELECT msjeError AS resultado INTO result;
	END IF;

	COMMIT;
END;	!

CREATE PROCEDURE reserva_ida_vuelta(IN numVueloIda VARCHAR(45),IN fechaIda DATE, IN claseIda VARCHAR(45), 
									IN numVueloVuelta VARCHAR(45),IN fechaVuelta DATE, IN claseVuelta VARCHAR(45),
									IN tipoDoc VARCHAR(45), IN numDoc int unsigned ,IN legajo INT unsigned, OUT result VARCHAR(100))
BEGIN
	DECLARE fechaVencimiento DATE;

	DECLARE asientosRealesIda int unsigned;
	DECLARE asientosReservadosIda int unsigned;
	DECLARE sobreventa_disponiblesIda int unsigned;
	DECLARE datosValidosIda BOOLEAN;
	DECLARE msjeError VARCHAR(100);
	DECLARE asientosRealesVuelta int unsigned;
	DECLARE asientosReservadosVuelta int unsigned;
	DECLARE sobreventa_disponiblesVuelta int unsigned;
	DECLARE datosValidosVuelta BOOLEAN;
	 
	DECLARE codigo_SQL CHAR(5) DEFAULT '00000';
	DECLARE codigo_MYSQL INT DEFAULT 0;
	DECLARE mensaje_error TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	BEGIN  # Si se produce una SQLEXCEPTION, se retrocede la transacción con ROLLBACK 
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, 
		        codigo_MySQL, codigo_SQL,  mensaje_error;
		        SET result = 'Error SQL';
        ROLLBACK;
	END;

	#INICIO DE TRANSACCION
	START TRANSACTION;
	SELECT SUBDATE(fechaIda,INTERVAL 15 DAY) INTO fechaVencimiento;
	call validarDatos(numVueloIda,fechaIda,claseIda,tipoDoc,numDoc,legajo,datosValidosIda,msjeError);
	call validarDatos(numVueloVuelta,fechaVuelta,claseVuelta,tipoDoc,numDoc,legajo,datosValidosVuelta,msjeError);
	IF datosValidosIda and datosValidosVuelta then #Si los datos tanto de ida y de vuelta sean válidos
		SELECT  cantidad INTO asientosReservadosIda FROM asientos_reservados as ar WHERE ar.vuelo=numVueloIda AND ar.fecha=fechaIda AND ar.clase=claseIda FOR UPDATE;
		SELECT  cantidad INTO asientosReservadosVuelta FROM asientos_reservados as ar WHERE ar.vuelo=numVueloVuelta AND ar.fecha=fechaVuelta AND ar.clase=claseVuelta FOR UPDATE;
		call obtener_asientos_disponibles(numVueloIda,fechaIda,claseIda,asientosReservadosIda,asientosRealesIda,sobreventa_disponiblesIda);
		call obtener_asientos_disponibles(numVueloVuelta,fechaVuelta,claseVuelta,asientosReservadosVuelta,asientosRealesVuelta,sobreventa_disponiblesVuelta);
		IF asientosRealesIda > 0 then #disponibilidad de asientos reales en vuelo de ida
			IF asientosRealesVuelta > 0 then #disponibilidad de asientos reales en vuelo de vuelta
				INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,'confirmada',tipoDoc,numDoc,legajo);
				INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloIda,fechaIda,claseIda);
				INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloVuelta,fechaVuelta,claseVuelta);
				UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloIda AND ar.fecha=fechaIda AND ar.clase=claseIda;
				UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloVuelta AND ar.fecha=fechaVuelta AND ar.clase=claseVuelta;
				SELECT 'La reserva se realizo con exito. Estado de la reserva: Confirmada' AS resultado INTO result;
			ELSE
				IF sobreventa_disponiblesVuelta > 0 then
					INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,'en espera',tipoDoc,numDoc,legajo);
					INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloIda,fechaIda,claseIda);
					INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloVuelta,fechaVuelta,claseVuelta);
					UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloIda AND ar.fecha=fechaIda AND ar.clase=claseIda;
					UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloVuelta AND ar.fecha=fechaVuelta AND ar.clase=claseVuelta;
					SELECT 'La reserva se realizo con exito. Estado de la reserva: En espera' AS resultado INTO result;
				ELSE #No hay asientos de ningún tipo para el vuelo de vuelta pero si hay asientos disponibles reales para el vuelo de ida
					SELECT 'No se pudo realizar la reserva: No hay asientos disponibles para el vuelo de vuelta' AS resultado INTO result;
				END IF;
			END IF;
		ELSE
			IF sobreventa_disponiblesIda > 0 then
				IF asientosRealesVuelta > 0 then
					INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,'en espera',tipoDoc,numDoc,legajo);
					INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloIda,fechaIda,claseIda);
					INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloVuelta,fechaVuelta,claseVuelta);
					UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloIda AND ar.fecha=fechaIda AND ar.clase=claseIda;
					UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloVuelta AND ar.fecha=fechaVuelta AND ar.clase=claseVuelta;
					SELECT 'La reserva se realizo con exito. Estado de la reserva: En espera' AS resultado INTO result;
				ELSE #Hay asientos sobrevendidos disponibles en el vuelo de ida pero no hay asientos reales disponibles en el de vuelta
					IF sobreventa_disponiblesVuelta > 0 then
						INSERT INTO reservas(fecha,vencimiento,estado,doc_tipo,doc_nro,legajo) VALUES (curdate(),fechaVencimiento,'en espera',tipoDoc,numDoc,legajo);
						INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloIda,fechaIda,claseIda);
						INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(),numVueloVuelta,fechaVuelta,claseVuelta);
						UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloIda AND ar.fecha=fechaIda AND ar.clase=claseIda;
						UPDATE asientos_reservados as ar SET cantidad=cantidad+1 WHERE ar.vuelo=numVueloVuelta AND ar.fecha=fechaVuelta AND ar.clase=claseVuelta;
						SELECT 'La reserva se realizo con exito. Estado de la reserva: En espera' AS resultado INTO result;
					ELSE #Hay sobreventa disponible para el vuelo de ida pero no hay asientos de ninguna clase en el de vuelta
						SELECT 'No se pudo realizar la reserva: No hay asientos disponibles para el vuelo de vuelta' AS resultado INTO result;
					END IF;
				END IF;
			ELSE #No hay sobreventa disponible para el vuelo de ida
				SELECT 'No se pudo realizar la reserva: No hay asientos disponibles para el vuelo de ida' AS resultado INTO result;
			END IF;
		END IF;
	ELSE #Alguno de los datos son inválidos
		SELECT msjeError AS resultado INTO result;
	END IF;
	COMMIT;	#FIN DE TRANSACCION
END;	!

delimiter ;

#-------------------------------------------------------------------------
#empleado
# El usuario 'empleado' esta destinado a las consultas y reservas de vuelos.
# -Consultas: acceso de lectura sobre tablas de b.d "vuelos".
# -Reservas: privilegios para ingresar, modificar y borrar datos sobre las tablas
#            "reservas", "pasajeros" y "reserva_vuelo_clase".
# Puede conectarse desde cualquier dominio.
# El password de su cuenta es 'empleado'.

CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';
#Privilegios
GRANT SELECT ON vuelos.* TO 'empleado'@'%';

GRANT EXECUTE ON PROCEDURE reserva_ida TO 'empleado'@'%';
GRANT EXECUTE ON PROCEDURE reserva_ida_vuelta TO 'empleado'@'%';

GRANT INSERT ON vuelos.reservas TO 'empleado'@'%';
GRANT UPDATE ON vuelos.reservas TO 'empleado'@'%';
GRANT DELETE ON vuelos.reservas TO 'empleado'@'%';
	
GRANT INSERT ON vuelos.pasajeros TO 'empleado'@'%';
GRANT UPDATE ON vuelos.pasajeros TO 'empleado'@'%';
GRANT DELETE ON vuelos.pasajeros TO 'empleado'@'%';
	
GRANT INSERT ON vuelos.reserva_vuelo_clase TO 'empleado'@'%';
GRANT UPDATE ON vuelos.reserva_vuelo_clase TO 'empleado'@'%';
GRANT DELETE ON vuelos.reserva_vuelo_clase TO 'empleado'@'%';