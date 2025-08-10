-- para crear la tabla de persona que modificare mas adelante
CREATE TABLE persona (
    id SERIAL PRIMARY KEY,
    dpi VARCHAR(20),
    nombres VARCHAR(100)
);
-- alter table para hacer el add column
ALTER TABLE persona ADD COLUMN IF NOT EXISTS rol VARCHAR(20);

ALTER TABLE persona ADD COLUMN IF NOT EXISTS contrasena TEXT;
-- alter table para hacerle el not null a las dos columnas agregadas recientemente
--primero hice un truncate table
TRUNCATE TABLE persona RESTART IDENTITY; -- restart reinicia el id
--para los alter table 
ALTER TABLE persona ALTER COLUMN rol SET NOT NULL;
ALTER TABLE persona ALTER COLUMN contrasena SET NOT NULL;
--un insert a la tabla con todos los datos incluyendo los dos nuevos
INSERT INTO persona (dpi, nombres, rol, contrasena)
VALUES ('75902417877', 'jose manuel', 'administrador', 'admin123');
--para limitar dpi a solo 13 numeros: 
ALTER TABLE persona
ALTER COLUMN dpi TYPE VARCHAR(13);
-- añadir una restriccion de suario, adminstrador: 
-- Si ya existe la columna y quieres añadir la restricción
ALTER TABLE persona
ADD CONSTRAINT chk_rol CHECK (rol IN ('usuario', 'administrador'));
--para  evitar duplicados en la base de datos del dpi
ALTER TABLE persona ADD CONSTRAINT uq_persona_dpi UNIQUE (dpi);
