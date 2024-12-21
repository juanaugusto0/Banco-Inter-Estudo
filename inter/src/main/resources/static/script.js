const backendURL = "http://localhost:8080/client";
function login() {
    const cpf = document.getElementById('cpf-input').value;

    fetch(`${backendURL}/cpf/${cpf}`)
        .then(response => {
            if (!response.ok) throw new Error("CPF inválido.");
            return response.json();
        })
        .then(data => {
            document.getElementById('account-name').innerText = `Bem-vindo, ${data.name}`;
            document.getElementById('account-balance').innerText = `Saldo: R$ ${data.balance}`;
            document.getElementById('login-screen').classList.remove('active');
            document.getElementById('account-screen').classList.add('active');
            document.getElementById('account-header').style.display = 'block';
            localStorage.setItem('cpf', cpf);
        })
        .catch(err => {
            document.getElementById('login-error').style.display = 'block';
        });
}

function showAccountDetails() {
    const cpf = document.getElementById('cpf-input').value;

    fetch(`${backendURL}/cpf/${cpf}`)
        .then(response => response.json())
        .then(data => {
            alert(`Detalhes da Conta:\n` +
                `Nome: ${data.name}\n` +
                `CPF: ${data.cpf}\n` +
                `E-mail: ${data.email}\n` +
                `ID: ${data.id}\n` +
                `Tipo de Membro: ${data.membershipTier}\n` +
                `Saldo: R$ ${data.balance}`);
        })
        .catch(err => {
            alert("Erro ao buscar detalhes da conta.");
        });
}

function goToTransactions() {
    window.location.href = 'transactions.html';
}
