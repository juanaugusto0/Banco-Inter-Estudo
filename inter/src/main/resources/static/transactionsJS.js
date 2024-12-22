const backendURL = "http://localhost:8080";
const cpf = localStorage.getItem('cpf');
let transactions = [];

function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString();
}

function renderTransactions(transactions) {
    const transactionList = document.getElementById("transactionList");
    
    transactionList.innerHTML = '';

    transactions.forEach(transaction => {
        const item = document.createElement("div");
        item.classList.add("transaction-item");

        item.innerHTML = `
            <span class="type ${transaction.type}">${transaction.type}</span>
            <span class="amount">R$ ${transaction.amount.toFixed(2)}</span>
            <span class="timestamp">${formatTimestamp(transaction.timestamp)}</span>
        `;

        transactionList.appendChild(item);
    });
}

function updateTransactionList() {
    fetch(`${backendURL}/client/transactions/${cpf}`)
        .then(response => response.json())
        .then(data => {
            // Ordena os dados por timestamp (mais recente primeiro)
            transactions = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
            // Limpa a lista atual
            const transactionList = document.getElementById("transactionList");
            transactionList.innerHTML = '';
            // Renderiza novamente
            renderTransactions(transactions);
        })
        .catch(error => console.error('Erro ao atualizar as transações:', error));
}

document.addEventListener("DOMContentLoaded", () => {
    fetch(`${backendURL}/client/transactions/${cpf}`)
    .then(response => response.json())
    .then(data => {
        transactions = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
        updateTransactionList(transactions);
    })
    .catch(error => console.error('Erro ao carregar as transações:', error));
});

function goToAccount() {
    window.location.href = 'index.html';
}

function makePutRequest(endpoint, params) {
    const urlParams = new URLSearchParams(params).toString();
    const url = `${endpoint}?${urlParams}`;

    return fetch(url, {
        method: 'PUT',
    })
    .then((response) => {
        return response.text().then(text => {
            try {
                const jsonResponse = JSON.parse(text);
                return { isJson: true, data: jsonResponse };
            } catch (e) {
                return { isJson: false, data: text };
            }
        });
    })
    .then(result => {
        if (result.isJson) {
            if (result.data.status !== 200) {
                throw new Error(result.data.message || 'Erro desconhecido.');
            }
        } else {
            return result.data;
        }
    })
    .catch((error) => {
        console.error('Erro na requisição:', error);
        throw new Error(error.message || 'Falha na comunicação com o servidor.');
    });
}

function showDeposit() {
    Swal.fire({
        title: 'Novo Depósito',
        input: 'number',
        inputLabel: 'Digite o valor para depósito',
        inputPlaceholder: 'Ex: 500.00',
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar',
    }).then((result) => {
        if (result.isConfirmed) {
            const depositAmount = parseFloat(result.value);

            makePutRequest(`${backendURL}/transaction/deposit`, {
                clientCpf: cpf,
                depositAmount: depositAmount,
            })
                .then((data) => {
                    Swal.fire('Sucesso!', data, 'success');
                    updateTransactionList();
                })
                .catch((error) => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
    });
}

function showWithdraw() {
    Swal.fire({
        title: 'Novo Saque',
        input: 'number',
        inputLabel: 'Digite o valor para saque',
        inputPlaceholder: 'Ex: 300.00',
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar',

    }).then((result) => {
        if (result.isConfirmed) {
            const withdrawalAmount = parseFloat(result.value);

            makePutRequest(`${backendURL}/transaction/withdraw`, {
                clientCpf: cpf,
                withdrawalAmount: withdrawalAmount,
            })
                .then((data) => {
                    Swal.fire('Sucesso!', data, 'success');
                    updateTransactionList();
                })
                .catch((error) => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
    });
}

function showTransfer() {
    Swal.fire({
        title: 'Nova Transferência',
        html: `
            <input type="text" id="recipient-cpf" class="swal2-input" placeholder="CPF do destinatário">
            <input type="number" id="transfer-amount" class="swal2-input" placeholder="Valor para transferência" step="0.01" min="0.1">
        `,
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar',
        didOpen: () => {
            const cpfInput = document.getElementById('recipient-cpf');
            cpfInput.addEventListener('input', () => {
                let value = cpfInput.value.replace(/\D/g, '');
                if (value.length > 11) value = value.slice(0, 11);
                cpfInput.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            });
        },
        preConfirm: () => {
            const recipientCpf = document.getElementById('recipient-cpf').value;
            const amount = document.getElementById('transfer-amount').value;
            if (!recipientCpf || !amount) {
                Swal.showValidationMessage('Por favor, preencha ambos os campos');
            }
            return {
                recipientCpf: document.getElementById('recipient-cpf').value.replace(/\D/g, ''),
                amount: parseFloat(document.getElementById('transfer-amount').value),
                senderCpf: cpf,
            };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const { recipientCpf, amount, senderCpf } = result.value;

            makePutRequest(`${backendURL}/transaction/transfer`, {
                recipientCpf: recipientCpf,
                amount: amount,
                senderCpf: senderCpf,
            })
                .then((data) => {
                    Swal.fire('Sucesso!', data, 'success');
                    updateTransactionList();
                })
                .catch((error) => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
    });
}