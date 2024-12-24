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
    const cpf = document.getElementById('cpf-input').value.replace(/\D/g, '');

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
            '<input id="details-name" class="details-input" placeholder="Nome">' +
            '<input type="text" id="details-cpf" class="details-input" placeholder="CPF">' +
            '<input type="email" id="details-email" class="details-input" placeholder="E-mail">' +
            '<select id="details-membership" class="details-input">' +
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
            const cpfInput = document.getElementById('details-cpf');
            cpfInput.addEventListener('input', () => {
                let value = cpfInput.value.replace(/\D/g, '');
                if (value.length > 11) value = value.slice(0, 11);
                cpfInput.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            });
        },
        preConfirm: () => {
            const name = document.getElementById('details-name').value;
            const cpf = document.getElementById('details-cpf').value.replace(/\D/g, '');
            const email = document.getElementById('details-email').value;
            const membershipTier = document.getElementById('details-membership').value;

            if (!name || !cpf || !email || !membershipTier) {
                Swal.showValidationMessage('Por favor, preencha todos os campos');
                return false;
            }
            
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                Swal.showValidationMessage('Por favor, insira um e-mail válido');
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
    const cpf = document.getElementById('cpf-input').value.replace(/\D/g, '');
    fetch(`${backendURL}/cpf/${cpf}`).then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.message);
            });
        }
        return response.json();
    })
    .then(data => {
        document.getElementById('account-details-screen').classList.add('active');
        document.getElementById('account-details-name').innerHTML = `${data.name}`;
        document.getElementById('account-details-email').innerHTML = `${data.email}`;
        document.getElementById('account-details-balance').innerHTML = `R$ ${data.balance}`;
        document.getElementById('account-details-membership-tier').innerHTML = `${data.membershipTier}`;
        document.getElementById('account-details-id').innerHTML = `${data.id}`;
        const formattedCpf = data.cpf.toString().replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
        document.getElementById('account-details-cpf').innerHTML = `${formattedCpf}`;
    });
}

document.addEventListener('click', (event) => {
    const accountDetailsContainer = document.getElementById('account-details-screen');
    if (accountDetailsContainer.classList.contains('active') && !accountDetailsContainer.contains(event.target)) {
        accountDetailsContainer.classList.remove('active');
    }
});

function deleteAccount() {
    const cpf = document.getElementById('cpf-input').value.replace(/\D/g, '');
    Swal.fire({
        title: 'Tem certeza?',
        text: 'Essa ação não pode ser desfeita.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sim, deletar',
        cancelButtonText: 'Cancelar'
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`${backendURL}/remove/${cpf}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message);
                    });
                }
                return response.text();
            })
            .then(message => {
                Swal.fire('Deletado!', message, 'success');
                document.getElementById('login-screen').classList.add('active');
                document.getElementById('account-screen').classList.remove('active');
                document.getElementById('account-header').style.display = 'none';
                localStorage.removeItem('cpf');

            })
            .catch(error => {
                Swal.fire('Erro!', error.message, 'error');
            });
        }
    });
}

function changeData() {
    Swal.fire({
        title: 'Alterar Dados',
        html:
            '<input id="change-name" class="details-input" placeholder="Nome">' +
            '<input id="change-email" class="details-input" placeholder="E-mail">' +
            '<select id="change-membership" class="details-input">' +
            '<option value="" disabled selected>Selecione o Tipo de Membro</option>' +
            '<option value="SILVER">SILVER</option>' +
            '<option value="GOLD">GOLD</option>' +
            '<option value="PLATINUM">PLATINUM</option>' +
            '<option value="BLACK">BLACK</option>' +
            '</select>' +
            '<p>Deixe em branco os campos que não deseja alterar.</p>',
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Alterar',
        cancelButtonText: 'Cancelar',
        preConfirm: () => {
            let name = document.getElementById('change-name').value;
            const cpf = localStorage.getItem('cpf');
            let email = document.getElementById('change-email').value;
            let membershipTier = document.getElementById('change-membership').value;
            if (!name) name = null;
            if (!email) email = null;
            if (!membershipTier) membershipTier = null;
            return { name, cpf, email, membershipTier };
        }
    }).then(result => {
        if (result.isConfirmed) {
            const updatedData = result.value;
            const cpf = document.getElementById('cpf-input').value.replace(/\D/g, '');

            sendBodyRequest(`${backendURL}/update`, 'PUT', updatedData)
                .then(message => {
                    Swal.fire('Sucesso!', message, 'success');
                    login();
                })
                .catch(error => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
    });
}


function goToTransactions() {
    window.location.href = 'transactions.html';
}
