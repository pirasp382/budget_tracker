import axios from "axios"


const backendAddress = "http://localhost:8000"


export const getUserDataAPICall = () => {
    const token = localStorage.getItem("jwt")
    const url = `${backendAddress}/userData`
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}

export const getGraphDataTransactionAPICall = (accountId, timeType) => {
    const token = localStorage.getItem("jwt")
    const url = `${backendAddress}/transaction/transactionGraph?accountId=${accountId}&time=${timeType.toUpperCase()}`
    return axios.get(url, {
        headers: {
            'Authorization': `Bearer: ${token}`,
        },
    })
        .then(response => response.data)
}