create table if not exists lead_guias (
  id bigserial primary key,
  nombre varchar(150) not null,
  correo varchar(255) not null,
  whatsapp varchar(50),
  interes varchar(100) not null,
  origen varchar(100) not null,
  created_at timestamptz not null default now()
);

create index if not exists idx_lead_guias_correo on lead_guias(correo);
create index if not exists idx_lead_guias_interes on lead_guias(interes);