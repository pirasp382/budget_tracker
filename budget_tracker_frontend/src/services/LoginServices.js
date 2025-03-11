import axios from "axios"


export const loginAPICall=(username, password)=>{
    const login_url = "http://localhost:8000/login"
    return axios.post(login_url, {
        username: username,
        password: password,
    })
        .then((response) => response.data)
}