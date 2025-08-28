--tabla  usuario
CREATE TABLE Usuarios (
    id_usuario SERIAL PRIMARY KEY,
    dpi VARCHAR(13),
    nombres VARCHAR(40),
    usuario VARCHAR(15) UNIQUE,
    correo VARCHAR(100) UNIQUE,
    contrasena VARCHAR(100),
    rol VARCHAR(20) CHECK (rol IN ('residente', 'administrador', 'seguridad')),
    lote VARCHAR(20),
    numero_casa VARCHAR(10),
    estado VARCHAR(20) DEFAULT 'activo' -- activo/inactivo
);

--tabla tajeta
CREATE TABLE tarjeta (
    id_tarjeta SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario),
    numero_tarjeta VARCHAR(20) UNIQUE,
    fecha_vencimiento DATE,
    cvv VARCHAR(4),
    nombre_titular VARCHAR(100),
    tipo_tarjeta VARCHAR(20),
    saldo DECIMAL(10,2)
);

--tabla pagos
CREATE TABLE Pago (
    id_pago SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario),
    tipo_pago VARCHAR(30),
    metodo_pago VARCHAR(30),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2),
    mora DECIMAL(10,2) DEFAULT 0,
    observaciones TEXT
);

--qr_usuarios
CREATE TABLE qr_usuario (
    id_qr_usuario SERIAL PRIMARY KEY,
    codigo_qr_usuario VARCHAR(100) UNIQUE,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario),
    fecha_hora_generada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);
--tabla accesos suario
CREATE TABLE acceso_usuario (
    id_acceso SERIAL PRIMARY KEY,
    id_qr_usuario INTEGER REFERENCES qr_usuario(id_qr_usuario),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    resultado VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);
--notificaciones usuario general: 
CREATE TABLE Notificaciones (
    id_notificaciones SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario),
    asunto VARCHAR(100),
    mensaje TEXT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_evento VARCHAR(50)
);
--visita
CREATE TABLE vista (
    id_visita SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario),
    dpi_visita VARCHAR(13),
    nombre VARCHAR(100),
    tipo_visita VARCHAR(30),
    correo_visita VARCHAR(100)
);

-- qr visita
CREATE TABLE qr_visita (
    id_qr_visita SERIAL PRIMARY KEY,
    id_visita INTEGER REFERENCES vista(id_visita),
    codigo_qr_visita VARCHAR(100) UNIQUE,
    fecha_hora_generada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    numero_accesos INTEGER DEFAULT 0
);
--acceso visita: 
CREATE TABLE acceso_visita (
    id_acceso_visita SERIAL PRIMARY KEY,
    id_qr_visita INTEGER REFERENCES qr_visita(id_qr_visita),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    resultado VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);

CREATE TABLE notificaciones_visita (
    id_notificaciones_visita SERIAL PRIMARY KEY,
    id_visita INTEGER REFERENCES vista(id_visita),
    asunto VARCHAR(100),
    mensaje TEXT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_evento VARCHAR(50)
);

