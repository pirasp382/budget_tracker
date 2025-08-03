import axios from "axios"
import BACKEND_ADDRESS_URL from "./backendAddress"

const backendAddress = BACKEND_ADDRESS_URL


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