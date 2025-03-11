import axios from "axios"

const backendAddress = "http://localhost:8000"

export const getCategoriesAPICall = () => {
    const url = `${backendAddress}/category`
    const token = localStorage.getItem("jwt")
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const saveEditsAPICall = (transaction) => {
    const url = `${backendAddress}/transaction/${transaction.id}`
    const token = localStorage.getItem("jwt")
    return axios.put(url, {
        ...transaction,
    }, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const deleteTransactionsAPICall = (selected) => {
    const url = `${backendAddress}/transaction`
    const token = localStorage.getItem("jwt")
    return axios.delete(url, {
        data: selected,
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const addNewTransactionAPICall = (account_id, transaction) => {
    const url = `${backendAddress}/transaction?accountId=${account_id}`
    const token = localStorage.getItem("jwt")
    console.log(transaction)
    return axios.post(url, {
        ...transaction,
    }, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const getAccountsAPICall = () => {
    const url = `${backendAddress}/account`
    const token = localStorage.getItem("jwt")
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const getAllTransactionsAPICall = (sortParam, direction, accountId) => {
    const url = `${backendAddress}/transaction?sortParam=${sortParam}&direction=${direction}&accountId=${accountId}`
    const token = localStorage.getItem("jwt")
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}