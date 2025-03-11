import axios from "axios"

const backendAddress = 'http://localhost:8000'

export const getAllAccountsAPICall = (sortParam, direction) => {
    const url = `${backendAddress}/account?sortParam=${sortParam}&direction=${direction}`
    const token = localStorage.getItem("jwt")
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const deleteAccountsAPICall = (selected) => {
    const url = `${backendAddress}/account`
    const token = localStorage.getItem("jwt")
    return axios.delete(url, {
        data: selected,
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const saveEditsAPICall = (newAccount) => {
    const url = `${backendAddress}/account/${newAccount.id}`
    const token = localStorage.getItem("jwt")
    return axios.put(url, {
        ...newAccount,
    }, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const addAccountAPICall = (account) => {
    const url = `${backendAddress}/account`
    const token = localStorage.getItem("jwt")
    return axios.post(url, {
        ...account,
    }, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}