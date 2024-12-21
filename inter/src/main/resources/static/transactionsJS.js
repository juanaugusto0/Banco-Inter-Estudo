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

document.addEventListener("DOMContentLoaded", () => {
    fetch(`${backendURL}/client/transactions/${cpf}`)
    .then(response => response.json())
    .then(data => {
        transactions = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
        renderTransactions(transactions);
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
                })
                .catch((error) => {
                    Swal.fire('Erro!', error.message, 'error');
                });
        }
    });
}
