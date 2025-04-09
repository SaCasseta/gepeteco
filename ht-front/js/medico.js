document.addEventListener('DOMContentLoaded', () => {
    carregarMedicos();

    document.getElementById('medicoForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const medico = {
            nome: document.getElementById('nome').value,
            especialidade: document.getElementById('especialidade').value,
            consultorio: document.getElementById('consultorio').value
        };
        await fetch('http://localhost:8080/api/medicos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(medico)
        });
        carregarMedicos();
        e.target.reset();
    });
});

async function carregarMedicos() {
    const response = await fetch('http://localhost:8080/api/medicos');
    const medicos = await response.json();
    const table = document.getElementById('medicoTable');
    table.innerHTML = '';
    medicos.forEach(medico => {
        const row = table.insertRow();
        row.innerHTML = `
            <td>${medico.id}</td>
            <td>${medico.nome}</td>
            <td>${medico.especialidade}</td>
            <td>${medico.consultorio}</td>
            <td><button onclick="atualizarConsultorio(${medico.id})">Mudar Consultório</button></td>
        `;
    });
}

async function atualizarConsultorio(id) {
    const consultorio = prompt('Novo consultório:');
    if (consultorio) {
        await fetch(`http://localhost:8080/api/medicos/${id}/consultorio`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(consultorio)
        });
        carregarMedicos();
    }
}