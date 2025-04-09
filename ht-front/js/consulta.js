document.addEventListener('DOMContentLoaded', () => {
    carregarMedicos();
    carregarPacientes();
    carregarMedicosRelatorio();

    document.getElementById('consultaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const consulta = {
            medico: { id: document.getElementById('medicoId').value },
            paciente: { id: document.getElementById('pacienteId').value },
            dataHora: document.getElementById('dataHora').value,
            descricao: document.getElementById('descricao').value,
            valor: parseFloat(document.getElementById('valor').value),
            retorno: document.getElementById('retorno').checked
        };
        await fetch('http://localhost:8080/api/consultas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(consulta)
        });
        carregarConsultasPorMedico();
        e.target.reset();
    });
});

async function carregarMedicos() {
    const response = await fetch('http://localhost:8080/api/medicos');
    const medicos = await response.json();
    const select = document.getElementById('medicoId');
    const selectConsulta = document.getElementById('medicoConsulta');
    medicos.forEach(medico => {
        const option = document.createElement('option');
        option.value = medico.id;
        option.textContent = medico.nome;
        select.appendChild(option);
        selectConsulta.appendChild(option.cloneNode(true));
    });
}

async function carregarPacientes() {
    const response = await fetch('http://localhost:8080/api/pacientes');
    const pacientes = await response.json();
    const select = document.getElementById('pacienteId');
    pacientes.forEach(paciente => {
        const option = document.createElement('option');
        option.value = paciente.id;
        option.textContent = paciente.nome;
        select.appendChild(option);
    });
}

async function carregarMedicosRelatorio() {
    const response = await fetch('http://localhost:8080/api/medicos');
    const medicos = await response.json();
    const select = document.getElementById('medicoRelatorio');
    medicos.forEach(medico => {
        const option = document.createElement('option');
        option.value = medico.id;
        option.textContent = medico.nome;
        select.appendChild(option);
    });
}

async function carregarConsultasPorMedico() {
    const medicoId = document.getElementById('medicoConsulta').value;
    if (!medicoId) return;
    const response = await fetch(`http://localhost:8080/api/consultas/medico/${medicoId}`);
    const consultas = await response.json();
    const table = document.getElementById('consultaTable');
    table.innerHTML = '';
    consultas.forEach(consulta => {
        const row = table.insertRow();
        row.innerHTML = `
            <td>${consulta.id}</td>
            <td>${new Date(consulta.dataHora).toLocaleString()}</td>
            <td>${consulta.paciente.nome}</td>
            <td>${consulta.descricao}</td>
            <td>${consulta.valor.toFixed(2)}</td>
            <td>${consulta.retorno ? 'Sim' : 'Não'}</td>
        `;
    });
}

async function carregarRelatorioFinanceiro() {
    const medicoId = document.getElementById('medicoRelatorio').value;
    if (!medicoId) return;
    const response = await fetch(`http://localhost:8080/api/consultas/relatorio-financeiro/${medicoId}`);
    const total = await response.json();
    document.getElementById('totalFinanceiro').textContent = total.toFixed(2);
}