-- ==========================
-- TABLA USUARIOS
-- ==========================
CREATE TABLE Usuarios (
    id_usuario SERIAL PRIMARY KEY,
    dpi VARCHAR(13),
    nombres VARCHAR(40),
    usuario VARCHAR(15),
    correo VARCHAR(100),
    contrasena VARCHAR(100),
    rol VARCHAR(20),
    lote VARCHAR(20),
    numero_casa VARCHAR(10),
    estado VARCHAR(20) DEFAULT 'activo',
    CONSTRAINT chk_rol CHECK (rol IN ('residente', 'administrador', 'seguridad'))
);

-- ==========================
-- TABLA TARJETA
-- ==========================
CREATE TABLE tarjeta (
    id_tarjeta SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    numero_tarjeta VARCHAR(20), -- QUITADO UNIQUE
    fecha_vencimiento DATE,
    cvv VARCHAR(4),
    nombre_titular VARCHAR(100),
    tipo_tarjeta VARCHAR(20),
    saldo DECIMAL(10,2)
);

-- ==========================
-- TABLA PAGOS
-- ==========================
CREATE TABLE Pago (
    id_pago SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    tipo_pago VARCHAR(30),
    metodo_pago VARCHAR(30),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2),
    mora DECIMAL(10,2) DEFAULT 0,
    observaciones TEXT
);

ALTER TABLE Pago
ADD COLUMN estado VARCHAR(20) DEFAULT 'Pendiente';


ALTER TABLE Pago
ADD CONSTRAINT chk_estado_pago 
CHECK (estado IN ('Pendiente', 'Confirmado', 'Rechazado', 'En proceso'));


-- ==========================
-- TABLA QR_USUARIO
-- ==========================
CREATE TABLE qr_usuario (
    id_qr_usuario SERIAL PRIMARY KEY,
    codigo_qr_usuario VARCHAR(100), -- QUITADO UNIQUE
    id_usuario INTEGER REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    fecha_hora_generada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);

-- ==========================
-- TABLA ACCESO_USUARIO
-- ==========================
CREATE TABLE acceso_usuario (
    id_acceso SERIAL PRIMARY KEY,
    id_qr_usuario INTEGER REFERENCES qr_usuario(id_qr_usuario) ON DELETE CASCADE,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    resultado VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);

-- ==========================
-- TABLA NOTIFICACIONES
-- ==========================
CREATE TABLE Notificaciones (
    id_notificaciones SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    asunto VARCHAR(100),
    mensaje TEXT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_evento VARCHAR(50)
);

-- ==========================
-- TABLA VISITA
-- ==========================
CREATE TABLE visita (
    id_visita SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    dpi_visita VARCHAR(13),
    nombre VARCHAR(100),
    tipo_visita VARCHAR(30),
    correo_visita VARCHAR(100)
);

-- ==========================
-- TABLA QR_VISITA
-- ==========================
CREATE TABLE qr_visita (
    id_qr_visita SERIAL PRIMARY KEY,
    id_visita INTEGER REFERENCES visita(id_visita) ON DELETE CASCADE,
    codigo_qr_visita VARCHAR(100), -- QUITADO UNIQUE
    fecha_hora_generada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    numero_accesos INTEGER DEFAULT 0
);

-- ==========================
-- TABLA ACCESO_VISITA
-- ==========================
CREATE TABLE acceso_visita (
    id_acceso_visita SERIAL PRIMARY KEY,
    id_qr_visita INTEGER REFERENCES qr_visita(id_qr_visita) ON DELETE CASCADE,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(30),
    resultado VARCHAR(30),
    estado VARCHAR(20) DEFAULT 'activo'
);

-- ==========================
-- TABLA NOTIFICACIONES_VISITA
-- ==========================
CREATE TABLE notificaciones_visita (
    id_notificaciones_visita SERIAL PRIMARY KEY,
    id_visita INTEGER REFERENCES visita(id_visita) ON DELETE CASCADE,
    asunto VARCHAR(100),
    mensaje TEXT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_evento VARCHAR(50)
);


INSERT INTO Usuarios (
    dpi, 
    nombres, 
    usuario, 
    correo, 
    contrasena, 
    rol, 
    lote, 
    numero_casa, 
    estado
) VALUES (
    '1234567890123',
    'jose manuel',                 
    'josema',                     
    'sipacchuquiejj@gmail.com',
    '123',              -- contrasena 
    'administrador',                 
    'Lote 5',                     
    '12-A',                      
    'activo'                     
);
