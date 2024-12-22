const backendURL = "http://localhost:8080/client";

document.addEventListener('DOMContentLoaded', () => {
    const inputElement = document.getElementById('cpf-input');
    inputElement.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            login();
        }
    });
});

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

function sendBodyRequest(url, method, body) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.message);
            });
        }
        return response.text();
    });
}

function createAccount() {
    Swal.fire({
        title: 'Criar Conta',
        html:
            '<input id="swal-input1" class="swal2-input" placeholder="Nome">' +
            '<input type="text" id="swal-input2" class="swal2-input" placeholder="CPF">' +
            '<input id="swal-input3" class="swal2-input" placeholder="E-mail">' +
            '<select id="swal-input4" class="swal2-input">' +
            '<option value="" disabled selected>Selecione o Tipo de Membro</option>' +
            '<option value="SILVER">SILVER</option>' +
            '<option value="GOLD">GOLD</option>' +
            '<option value="PLATINUM">PLATINUM</option>' +
            '<option value="BLACK">BLACK</option>' +
            '</select>',
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Criar',
        cancelButtonText: 'Cancelar',
        didOpen: () => {
            const cpfInput = document.getElementById('swal-input2');
            cpfInput.addEventListener('input', () => {
                let value = cpfInput.value.replace(/\D/g, '');
                if (value.length > 11) value = value.slice(0, 11);
                cpfInput.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            });
        },
        preConfirm: () => {
            const name = document.getElementById('swal-input1').value;
            const cpf = document.getElementById('swal-input2').value.replace(/\D/g, '');
            const email = document.getElementById('swal-input3').value;
            const membershipTier = document.getElementById('swal-input4').value;

            if (!name || !cpf || !email || !membershipTier) {
                Swal.showValidationMessage('Por favor, preencha todos os campos');
                return false;
            }

            return { name, cpf, email, membershipTier };
        }
    }).then(result => {
        if (result.isConfirmed) {
            const accountData = result.value;
            sendBodyRequest(`${backendURL}/add`, 'POST', accountData)
                .then(message => {
                    Swal.fire('Sucesso!', message, 'success');
                })
                .catch(error => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
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
