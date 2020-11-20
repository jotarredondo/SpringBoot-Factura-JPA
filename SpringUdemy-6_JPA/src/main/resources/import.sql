INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (1, 'Marcelo', 'Bielsa', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (2, 'Thomas', 'Wesley', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (3, 'Quincy', 'Jones', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (4, 'Jorge', 'gonzalez', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (5, 'Bruce', 'Lee', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (6, 'Muhammad', 'Ali', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (7, 'Cassius', 'Klay', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (8, 'Elon', 'Musk', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (9, 'Steve', 'Jobs', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (10, 'Jack', 'Ma', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (11, 'Quentin', 'Tarantino', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (12, 'Margot', 'Robbie', 'diplo@gmail.com', '2020-02-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (13, 'Aubrey', 'Plaza', 'estoesniuls@gmail.com', '2020-01-11', '');
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (14, 'Cristiane', 'Endler', 'diplo@gmail.com', '2020-02-11', '');

INSERT INTO productos (nombre, precio, create_at) VALUES ('Teclado casio Ctk-200', 89900, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Guitarra Squier Stratocaster', 125900, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Big Muff Pedal', 99900, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Amplificador Fender Rumble', 184900, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Kazoo Plastico', 2790, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('LaunchKey Mini', 22900, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('DrumStick Vic Firth 5b', 5200, NOW());

INSERT INTO facturas (descripcion , observacion, cliente_id, create_at) VALUES ('Factura de Instrumentos musicales', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 7);

INSERT INTO facturas (descripcion , observacion, cliente_id, create_at) VALUES ('Factura de Instrumentos ', 'Alguna nota importante ', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3, 2, 6);


