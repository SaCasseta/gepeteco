const BASE_URL = 'http://localhost:8080'; // Altere para sua URL

// Elementos DOM
const form = document.getElementById('medicoForm');
const tabela = document.getElementById('medicosTable');

// Carrega médicos ao iniciar
document.addEventListener('DOMContentLoaded', carregarMedicos);

// Formulário de envio
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const medico = {
        id: document.getElementById('medicoId').value || null,
        nome: document.getElementById('nome').value,
        crm: document.getElementById('crm').value
    };

    const url = medico.id ? `${BASE_URL}/medicos/${medico.id}` : `${BASE_URL}/medicos`;
    const method = medico.id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(medico)
        });

        if (!response.ok) throw new Error('Falha ao salvar');
        
        form.reset();
        document.getElementById('medicoId').value = '';
        carregarMedicos();
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao salvar médico');
    }
});

// Carrega lista de médicos
async function carregarMedicos() {
    try {
        const response = await fetch(`${BASE_URL}/medicos`);
        const medicos = await response.json();
        renderizarTabela(medicos);
    } catch (error) {
        console.error('Erro ao carregar médicos:', error);
    }
}

// Renderiza a tabela
function renderizarTabela(medicos) {
    tabela.innerHTML = medicos.map(medico => `
        <tr>
            <td>${medico.id}</td>
            <td>${medico.nome}</td>
            <td>${medico.crm}</td>
            <td>
                <button onclick="editarMedico(${medico.id})">Editar</button>
                <button onclick="excluirMedico(${medico.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

// Funções globais para edição/exclusão
window.editarMedico = async (id) => {
    try {
        const response = await fetch(`${BASE_URL}/medicos/${id}`);
        const medico = await response.json();
        
        document.getElementById('medicoId').value = medico.id;
        document.getElementById('nome').value = medico.nome;
        document.getElementById('crm').value = medico.crm;
    } catch (error) {
        console.error('Erro ao editar:', error);
    }
};

window.excluirMedico = async (id) => {
    if (!confirm('Tem certeza que deseja excluir?')) return;
    
    try {
        const response = await fetch(`${BASE_URL}/medicos/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Falha ao excluir');
        carregarMedicos();
    } catch (error) {
        console.error('Erro ao excluir:', error);
    }
};