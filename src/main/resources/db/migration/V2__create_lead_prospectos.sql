create table if not exists lead_prospectos (
  id bigserial primary key,
  nombre varchar(150) not null,
  correo varchar(255) not null,
  whatsapp varchar(50) not null,
  producto varchar(100) not null,
  diagnostico varchar(100) not null,
  pension_actual_estimada numeric(12,2),
  pension_optimizada numeric(12,2),
  incremento_estimado numeric(12,2),
  costo_mensual numeric(12,2),
  costo_total numeric(12,2),
  created_at timestamptz not null default now()
);

create index if not exists idx_lead_prospectos_correo on lead_prospectos(correo);