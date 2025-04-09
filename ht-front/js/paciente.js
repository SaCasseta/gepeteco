document.addEventListener('DOMContentLoaded', () => {
    carregarMedicos();
    carregarPacientes();

    document.getElementById('pacienteForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const paciente = {
            nome: document.getElementById('nome').value,
            cpf: document.getElementById('cpf').value,
            telefone: document.getElementById('telefone').value,
            medico: { id: document.getElementById('medicoId').value }
        };
        await fetch('http://localhost:8080/api/pacientes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(paciente)
        });
        carregarPacientes();
        e.target.reset();
    });
});

async function carregarMedicos() {
    const response = await fetch('http://localhost:8080/api/medicos');
    const medicos = await response.json();
    const select = document.getElementById('medicoId');
    medicos.forEach(medico => {
        const option = document.createElement('option');
        option.value = medico.id;
        option.textContent = medico.nome;
        select.appendChild(option);
    });
}

async function carregarPacientes() {
    const response = await fetch('http://localhost:8080/api/pacientes');
    const pacientes = await response.json();
    const table = document.getElementById('pacienteTable');
    table.innerHTML = '';
    pacientes.forEach(paciente => {
        const row = table.insertRow();
        row.innerHTML = `
            <td>${paciente.id}</td>
            <td>${paciente.nome}</td>
            <td>${paciente.cpf}</td>
            <td>${paciente.telefone}</td>
            <td>${paciente.medico ? paciente.medico.nome : 'Sem médico'}</td>
        `;
    });
}