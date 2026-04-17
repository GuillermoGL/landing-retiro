create table if not exists leads (
  id bigserial primary key,
  email varchar(255) not null,
  edad_actual int,
  edad_retiro int,
  aporte_mensual numeric(12,2),
  created_at timestamptz not null default now()
);

create index if not exists idx_leads_email on leads(email);