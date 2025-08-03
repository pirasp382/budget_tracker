import axios from "axios"
import BACKEND_ADDRESS_URL from "./backendAddress"

export const loginAPICall=(username, password)=>{
    const login_url = `${BACKEND_ADDRESS_URL}/login`
    return axios.post(login_url, {
        username: username,
        password: password,
    })
        .then((response) => response.data)
}