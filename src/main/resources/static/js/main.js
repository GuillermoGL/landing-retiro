let projectionChart;
  let ultimoResultadoM40 = null;

  function generarTrayectoriaControlada(years, aporteMensual) {
    const labels = [];
    const ahorroSimple = [];
    const inversionProyectada = [];
    const ahorroAjustadoInflacion = [];

    let ahorroAcumulado = 0;
    let inversionAcumulada = 0;

    // Inflación estimada fija para referencia visual
    const inflacionAnual = 0.04;

    for (let year = 1; year <= years; year++) {
      labels.push(`Año ${year}`);
      ahorroAcumulado += aporteMensual * 12;

      let rendimientoAnual;
      if (year % 5 === 0) {
        rendimientoAnual = 0.03;
      } else if (year % 7 === 0) {
        rendimientoAnual = 0.11;
      } else {
        rendimientoAnual = 0.07;
      }

      for (let m = 0; m < 12; m++) {
        const tasaMensual = rendimientoAnual / 12;
        inversionAcumulada = (inversionAcumulada + aporteMensual) * (1 + tasaMensual);
      }

      const ahorroReal = ahorroAcumulado / Math.pow(1 + inflacionAnual, year);

      ahorroSimple.push(Math.round(ahorroAcumulado));
      inversionProyectada.push(Math.round(inversionAcumulada));
      ahorroAjustadoInflacion.push(Math.round(ahorroReal));
    }

    return {
      labels,
      ahorroSimple,
      inversionProyectada,
      ahorroAjustadoInflacion
    };
  }

function renderChart(labels, ahorroSimple, inversionProyectada, valorAjustadoInflacion, metaRetiro) {
  const ctx = document.getElementById('projectionChart').getContext('2d');

  if (projectionChart) {
    projectionChart.destroy();
  }

  const metaLine = labels.map(() => metaRetiro);

  projectionChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: 'Ahorro simple',
          data: ahorroSimple,
          borderColor: '#B0B0B0',
          borderWidth: 3,
          tension: 0.3,
          fill: false
        },
        {
          label: 'Proyección estimada',
          data: inversionProyectada,
          borderColor: '#2563EB',
          borderWidth: 3,
          tension: 0.35,
          fill: false
        },
        {
          label: 'Valor real del dinero',
          data: valorAjustadoInflacion,
          borderColor: '#f59e0b',
          backgroundColor: 'rgba(245, 158, 11, 0.15)',
          borderWidth: 4,
          pointRadius: 4,
          pointHoverRadius: 5,
          tension: 0.3,
          borderDash: [10, 6],
          fill: false
        },
        {
          label: 'Meta objetivo',
          data: metaLine,
          borderColor: '#16A34A',
          borderWidth: 2,
          tension: 0,
          borderDash: [4, 4],
          fill: false
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        tooltip: {
          callbacks: {
            label: function(context) {
              return `${context.dataset.label}: $${context.parsed.y.toLocaleString('es-MX')}`;
            }
          }
        }
      },
      scales: {
        y: {
          ticks: {
            callback: function(value) {
              return '$' + value.toLocaleString('es-MX');
            }
          }
        }
      }
    }
  });
}

function actualizarResumenFintech(data, emailLead) {
  const cont = document.getElementById('fintechSummary');

  cont.innerHTML = `
    <div class="row g-3">
      <div class="col-md-3">
        <div class="border rounded-4 p-3 bg-light h-100">
          <div class="small text-secondary">Ahorro acumulado</div>
          <div class="fw-bold fs-5">$${Number(data.ahorroFinal).toLocaleString('es-MX')}</div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="border rounded-4 p-3 bg-light h-100">
          <div class="small text-secondary">Proyección estimada</div>
          <div class="fw-bold fs-5 text-success">$${Number(data.montoFinalEstimado).toLocaleString('es-MX')}</div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="border rounded-4 p-3 bg-light h-100">
          <div class="small text-secondary">Valor real del dinero</div>
          <div class="fw-bold fs-5 text-warning">$${Number(data.ahorroFinalInflacion).toLocaleString('es-MX')}</div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="border rounded-4 p-3 bg-light h-100">
          <div class="small text-secondary">Meta objetivo</div>
          <div class="fw-bold fs-5">$${Number(data.metaRetiro).toLocaleString('es-MX')}</div>
        </div>
      </div>
    </div>

    <div class="row g-3 mt-2">
      <div class="col-md-4">
        <div class="border rounded-4 p-3 h-100">
          <div class="small text-secondary">Avance hacia tu meta</div>
          <div class="fw-bold fs-4 text-primary">${Number(data.avanceMeta).toLocaleString('es-MX')}%</div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="border rounded-4 p-3 h-100">
          <div class="small text-secondary">Faltante estimado</div>
          <div class="fw-bold fs-4 text-danger">$${Number(data.faltanteMeta).toLocaleString('es-MX')}</div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="border rounded-4 p-3 h-100">
          <div class="small text-secondary">Ingreso mensual estimado</div>
          <div class="fw-bold fs-4">$${Number(data.ingresoMensualEstimado).toLocaleString('es-MX')}</div>
        </div>
      </div>
    </div>

    <div class="alert alert-warning mt-4 mb-0">
      <strong>Importante:</strong> esta proyección es informativa y no representa rendimientos garantizados.
      El valor real del dinero considera un ajuste referencial por inflación. Para una estrategia personalizada,
      agenda una asesoría.
    </div>
  `;
}

  
  /*function actualizarResumen(capitalFinal, ahorroFinal, ahorroInflacionFinal, years, emailLead) {
    document.getElementById('projectionResult').classList.remove('d-none');
    document.getElementById('resultText').innerHTML = `
      Plazo estimado de inversión: <strong>${years} años</strong><br>
      Monto estimado al retiro: <strong>$${capitalFinal.toLocaleString('es-MX')}</strong><br>
      Ahorro acumulado sin proyección: <strong>$${ahorroFinal.toLocaleString('es-MX')}</strong><br>
      Valor de referencia ajustado por inflación: <strong>$${ahorroInflacionFinal.toLocaleString('es-MX')}</strong><br>
      Correo capturado para seguimiento: <strong>${emailLead}</strong>
    `;

    document.getElementById('estimatedAmount').textContent = `$${capitalFinal.toLocaleString('es-MX')}`;
  }*/

  document.getElementById('projectionForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const btn = document.getElementById('btnCalcularFintech');
    const loading = document.getElementById('loadingFintech');

    btn.disabled = true;
    btn.textContent = 'Calculando...';
    loading.classList.remove('d-none');

    try{
      const edadActual = Number(document.getElementById('edadActual').value);
      const edadRetiro = Number(document.getElementById('edadRetiro').value);
      const aporteMensual = Number(document.getElementById('aporteMensual').value);
      const emailLead = document.getElementById('emailLead').value.trim();
      const metaRetiro = Number(document.getElementById('metaRetiro').value);
      const aniosDisfruteRetiro = Number(document.getElementById('aniosDisfruteRetiro').value);

      if (edadRetiro <= edadActual) {
        alert('La edad de retiro debe ser mayor a la edad actual.');
        return;
      }

      const response = await fetch('/api/proyeccion', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          edadActual,
          edadRetiro,
          aporteMensual,
          email: emailLead,
          metaRetiro,
          aniosDisfruteRetiro
        })
      });

      if (!response.ok) {
        alert('Ocurrió un error al calcular la proyección.');
        return;
      }
    
      const data = await response.json();

      renderChart(
        data.labels,
        data.ahorroSimple,
        data.proyeccionEstimada,
        data.valorAjustadoInflacion,
        data.metaRetiro
      );

      actualizarResumenFintech(data, emailLead);
    } catch (error) {
      alert('Ocurrió un error al calcular la proyección.');
      console.error(error);
    } finally {
      btn.disabled = false;
      btn.textContent = 'Calcular y enviar';
      loading.classList.add('d-none');
    }
  });

let chartM40Instance;

document.getElementById('formM40').addEventListener('submit', async function(e) {
  e.preventDefault();

  const data = {
    edadActual: Number(document.getElementById('m40EdadActual').value),
    edadRetiro: Number(document.getElementById('m40EdadRetiro').value),
    anioInicioCotizacion: Number(document.getElementById('m40AnioInicio').value),
    semanasCotizadas: Number(document.getElementById('m40SemanasCotizadas').value),
    semanasUltimos5Anios: Number(document.getElementById('m40Semanas5').value),
    salarioPromedio: Number(document.getElementById('m40SalarioPromedio').value),
    salarioModalidad40: Number(document.getElementById('m40SalarioM40').value),
    aniosModalidad40: Number(document.getElementById('m40AniosM40').value),
    aniosSinCotizar: Number(document.getElementById('m40AniosSinCotizar').value)
  };

  try {
    const response = await fetch('/api/imss/modalidad40', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      throw new Error('No fue posible calcular la proyección.');
    }

    const res = await response.json();
    renderResultadoM40(res);

  } catch (error) {
    alert('Error al calcular la proyección de Modalidad 40.');
    console.error(error);
  }
});


document.addEventListener('submit', async function(e) {
  if (e.target && e.target.id === 'leadPdfFormM40') {
    e.preventDefault();

    const nombre = document.getElementById('leadNombreM40').value.trim();
    const correo = document.getElementById('leadCorreoM40').value.trim();
    const whatsapp = document.getElementById('leadWhatsappM40').value.trim();

    if (!ultimoResultadoM40) {
      alert('Primero calcula la proyección.');
      return;
    }

    const payload = {
      nombre,
      correo,
      whatsapp,
      producto: 'MODALIDAD_40',
      diagnostico: ultimoResultadoM40.diagnostico,
      pensionActualEstimada: ultimoResultadoM40.pensionActualEstimada,
      pensionOptimizada: ultimoResultadoM40.pensionOptimizada,
      incrementoEstimado: ultimoResultadoM40.incrementoEstimado,
      costoMensual: ultimoResultadoM40.costoMensual,
      costoTotal: ultimoResultadoM40.costoTotal
    };

    try {
      const response = await fetch('/api/leads/modalidad40/pdf', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        throw new Error('No fue posible generar el PDF');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

      const a = document.createElement('a');
      a.href = url;
      a.download = 'resultado-modalidad-40.pdf';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);

      const mensaje = encodeURIComponent(
        `Hola Guillermo, ya hice mi simulación de Modalidad 40.\n` +
        `Diagnóstico: ${ultimoResultadoM40.diagnostico}\n` +
        `Pensión actual estimada: $${Number(ultimoResultadoM40.pensionActualEstimada).toLocaleString('es-MX')}\n` +
        `Pensión optimizada: $${Number(ultimoResultadoM40.pensionOptimizada).toLocaleString('es-MX')}\n` +
        `Incremento estimado: $${Number(ultimoResultadoM40.incrementoEstimado).toLocaleString('es-MX')}\n` +
        `Me interesa una asesoría personalizada.`
      );

      const wa = document.getElementById('whatsappResultadoM40');
      if (wa) {
        wa.href = `https://wa.me/5215611855449?text=${mensaje}`;
      }

    } catch (error) {
      console.error(error);
      alert('Ocurrió un error al guardar tus datos o generar el PDF.');
    }
  }
});

document.getElementById('formGuia').addEventListener('submit', async function(e) {
  e.preventDefault();

  const btn = document.getElementById('btnGuia');
  const loading = document.getElementById('loadingGuia');
  const resultado = document.getElementById('resultadoGuia');

  const payload = {
    nombre: document.getElementById('guiaNombre').value.trim(),
    correo: document.getElementById('guiaCorreo').value.trim(),
    whatsapp: document.getElementById('guiaWhatsapp').value.trim(),
    interes: document.getElementById('guiaInteres').value,
    origen: document.getElementById('guiaOrigen').value
  };

  btn.disabled = true;
  btn.textContent = 'Enviando...';
  loading.classList.remove('d-none');
  resultado.classList.add('d-none');
  resultado.innerHTML = '';

  try {
    const response = await fetch('/api/leads/guia', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      throw new Error('No fue posible guardar el lead');
    }

    const data = await response.json();

    resultado.classList.remove('d-none');
    resultado.innerHTML = `
      <div class="alert alert-success mb-0">
        <strong>¡Listo!</strong> Tu información fue registrada correctamente.
        En breve podrás recibir contenido de seguimiento.
        <div class="mt-3">
          <a 
            href="https://wa.me/5215611855449?text=${encodeURIComponent('Hola Guillermo, me registré en la guía de retiro y quiero más información sobre ' + payload.interes)}"
            target="_blank"
            class="btn btn-success btn-sm"
          >
            Continuar por WhatsApp
          </a>
        </div>
      </div>
    `;

    document.getElementById('formGuia').reset();

  } catch (error) {
    console.error(error);
    resultado.classList.remove('d-none');
    resultado.innerHTML = `
      <div class="alert alert-danger mb-0">
        Ocurrió un error al registrar tus datos. Intenta nuevamente.
      </div>
    `;
  } finally {
    btn.disabled = false;
    btn.textContent = 'Recibir guía';
    loading.classList.add('d-none');
  }
});

function renderResultadoM40(data) {
  ultimoResultadoM40 = data;
  const cont = document.getElementById('resultadoM40');
  cont.style.display = 'block';

  let badgeClass = 'bg-warning text-dark';
  let badgeText = 'REQUIERE VALIDACIÓN';

  if (data.diagnostico === 'ELEGIBLE') {
    badgeClass = 'bg-success';
    badgeText = 'ELEGIBLE';
  } else if (data.diagnostico === 'NO_ELEGIBLE') {
    badgeClass = 'bg-danger';
    badgeText = 'NO ELEGIBLE';
  }

  cont.innerHTML = `
    <div class="card border-0 shadow-sm h-100">
      <div class="card-body p-4">

        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
          <h4 class="mb-0 fw-bold">Resultado de la proyección</h4>
          <span class="badge ${badgeClass} fs-6 px-3 py-2">${badgeText}</span>
        </div>

        <p class="text-secondary mb-4">${data.mensajeDiagnostico}</p>

        <div class="row g-3 text-center">
          <div class="col-md-4">
            <div class="border rounded-4 p-3 h-100 bg-light">
              <div class="text-secondary small mb-1">Pensión actual</div>
              <div class="fw-bold fs-4">$${Number(data.pensionActualEstimada).toLocaleString('es-MX')}</div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="border rounded-4 p-3 h-100 bg-light">
              <div class="text-secondary small mb-1">Pensión optimizada</div>
              <div class="fw-bold fs-4 text-success">$${Number(data.pensionOptimizada).toLocaleString('es-MX')}</div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="border rounded-4 p-3 h-100 bg-light">
              <div class="text-secondary small mb-1">Incremento estimado</div>
              <div class="fw-bold fs-4 text-primary">$${Number(data.incrementoEstimado).toLocaleString('es-MX')}</div>
            </div>
          </div>
        </div>

        <div class="row g-3 text-center mt-2">
          <div class="col-md-6">
            <div class="border rounded-4 p-3 h-100">
              <div class="text-secondary small mb-1">Costo mensual</div>
              <div class="fw-bold fs-5">$${Number(data.costoMensual).toLocaleString('es-MX')}</div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="border rounded-4 p-3 h-100">
              <div class="text-secondary small mb-1">Costo total</div>
              <div class="fw-bold fs-5">$${Number(data.costoTotal).toLocaleString('es-MX')}</div>
            </div>
          </div>
        </div>

        <div class="mt-4">
          <canvas id="chartM40" height="130"></canvas>
        </div>

        <div class="alert alert-warning mt-4 mb-3" role="alert">
          <strong>Importante:</strong> estas no son cifras oficiales. Son estimaciones aproximadas con fines informativos,
          calculadas con base en criterios generales y reglas públicas del IMSS. El resultado final puede variar según
          semanas reconocidas, salario pensionable, conservación de derechos y validación documental.
        </div>

        <div class="card border mt-4">
          <div class="card-body">
            <h5 class="fw-bold mb-3">Descarga tu resultado en PDF</h5>
            <p class="text-secondary mb-3">
              Déjanos tus datos para generar tu resumen y enviarte información de seguimiento.
            </p>

            <form id="leadPdfFormM40" class="row g-3">
              <div class="col-md-4">
                <label class="form-label">Nombre</label>
                <input type="text" class="form-control" id="leadNombreM40" required>
              </div>

              <div class="col-md-4">
                <label class="form-label">Correo</label>
                <input type="email" class="form-control" id="leadCorreoM40" required>
              </div>

              <div class="col-md-4">
                <label class="form-label">WhatsApp</label>
                <input type="text" class="form-control" id="leadWhatsappM40" required>
              </div>

              <div class="col-12 d-flex gap-2 flex-wrap">
                <button type="submit" class="btn btn-dark">
                  Guardar y descargar PDF
                </button>

                <a 
                  id="whatsappResultadoM40"
                  href="https://wa.me/5215611855449"
                  target="_blank"
                  class="btn btn-success"
                >
                  Agendar asesoría por WhatsApp
                </a>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  `;

  renderChartM40(data);
}

    

renderChart(
  ['Año 1', 'Año 2', 'Año 3', 'Año 4', 'Año 5'],
  [24000, 48000, 72000, 96000, 120000],
  [25000, 51800, 82200, 115500, 154000]
);

function renderChartM40(data) {
  const canvas = document.getElementById('chartM40');
  if (!canvas) return;

  const ctx = canvas.getContext('2d');

  if (chartM40Instance) {
    chartM40Instance.destroy();
  }

  chartM40Instance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Pensión actual', 'Pensión optimizada', 'Incremento'],
      datasets: [{
        data: [
          Number(data.pensionActualEstimada),
          Number(data.pensionOptimizada),
          Number(data.incrementoEstimado)
        ],
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return '$' + context.parsed.y.toLocaleString('es-MX');
            }
          }
        }
      },
      scales: {
        y: {
          ticks: {
            callback: function(value) {
              return '$' + value.toLocaleString('es-MX');
            }
          }
        }
      }
    }
  });
}

function descargarPdfM40() {
  alert('En el siguiente paso conectamos la descarga del PDF.');
}

    
function renderResultado(data) {

  const cont = document.getElementById('resultadoM40');

  cont.style.display = 'block';

  cont.innerHTML = `
    <div class="card p-3 shadow">

      <h5 class="mb-3">Diagnóstico: ${data.diagnostico}</h5>
      <p>${data.mensajeDiagnostico}</p>

      <div class="row text-center mt-3">

        <div class="col-md-4">
          <h6>Pensión actual</h6>
          <strong>$${data.pensionActualEstimada.toLocaleString()}</strong>
        </div>

        <div class="col-md-4">
          <h6>Pensión optimizada</h6>
          <strong class="text-success">$${data.pensionOptimizada.toLocaleString()}</strong>
        </div>

        <div class="col-md-4">
          <h6>Incremento</h6>
          <strong class="text-primary">$${data.incrementoEstimado.toLocaleString()}</strong>
        </div>

      </div>

      <hr>

      <div class="row text-center">

        <div class="col-md-6">
          <h6>Costo mensual</h6>
          <strong>$${data.costoMensual.toLocaleString()}</strong>
        </div>

        <div class="col-md-6">
          <h6>Costo total</h6>
          <strong>$${data.costoTotal.toLocaleString()}</strong>
        </div>

      </div>

      <div class="mt-4 text-center">
        <a href="https://wa.me/5215611855449" class="btn btn-success">
          Agendar asesoría por WhatsApp
        </a>
      </div>

    </div>
  `;
}