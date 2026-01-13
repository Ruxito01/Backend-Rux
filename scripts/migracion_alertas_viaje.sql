-- Script de migración para agregar nuevos campos a la tabla alertas_viaje
-- Ejecutar en la base de datos de producción/desarrollo

-- 1. Hacer viaje_id nullable (permitir alertas sin viaje)
ALTER TABLE alertas_viaje MODIFY COLUMN viaje_id BIGINT NULL;

-- 2. Agregar campo origen_alerta
ALTER TABLE alertas_viaje ADD COLUMN origen_alerta VARCHAR(20) DEFAULT 'sos' AFTER tipo_alerta;

-- 3. Agregar coordenadas iniciales para alertas del chatbot
ALTER TABLE alertas_viaje ADD COLUMN latitud_inicio DECIMAL(10, 7) NULL AFTER longitud;
ALTER TABLE alertas_viaje ADD COLUMN longitud_inicio DECIMAL(10, 7) NULL AFTER latitud_inicio;

-- Verificar cambios
DESCRIBE alertas_viaje;

-- Consulta de ejemplo para ver alertas del chatbot
SELECT id, tipo_alerta, origen_alerta, mensaje, latitud, longitud, latitud_inicio, longitud_inicio, viaje_id
FROM alertas_viaje
WHERE origen_alerta = 'chatbot'
ORDER BY fecha_reporte DESC
LIMIT 10;
