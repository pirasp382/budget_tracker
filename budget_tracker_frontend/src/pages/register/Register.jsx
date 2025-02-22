import "./Register.css"

import {useState} from "react"
import axios from "axios"
import {useNavigate} from "react-router-dom"
import PopUpContainer from "../../components/pop-up/PopUpContainer"

function Register({setIsLoggedIn}) {
    const navigate = useNavigate()
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [confirmedPassword, setConfirmedPassword] = useState("")
    const [email, setEmail] = useState("")
    const [error, setError] = useState({general: "", passwordError: ""})
    const [responseError, setResponseError] = useState();
    const [loading, setLoading] = useState(false)
    const [success, setSuccess] = useState(false)

    function validateForm(input) {
        setConfirmedPassword(input)

        if (password !== input) {
            setError((prev) => ({...prev, passwordError: "Passwords don't match"}))
        } else {
            setError((prev) => ({...prev, passwordError: ""}))
        }

        if (!username || !password || !input || !email) {
            setError((prev) => ({...prev, general: "Please fill all required fields"}))
        } else {
            setError((prev) => ({...prev, general: ""}))
        }
    }

    function signup_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        setIsLoggedIn(true)
        setError({general: "", passwordError: ""})
    }

    function signup_error(errorlist) {
        setError((prev) => ({...prev, general: errorlist[0]["title"]}))
    }

    function login_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        setIsLoggedIn(true)
        navigate("/")
    }

    function login_error(errorlist) {
        setError((prev) => ({...prev, general: errorlist[0]["title"]}))
    }

    function login(e) {
        e.preventDefault()
        setLoading(true)
        const login_url = "http://localhost:8000/login"
        axios
            .post(login_url, {
                username: username,
                password: password,
            })
            .then((response) => response.data)
            .then((data) =>
                data["errorlist"].length === 0
                    ? login_success(data)
                    : login_error(data["errorlist"]),
            )
            .catch((error) => {
                setError((prev) => ({...prev, general: error.response.data.errorlist[0]?.title || "Login failed"}))
            })
            .finally(() => setLoading(false))
    }

    function submit(e) {
        e.preventDefault()
        const signup_url = "http://localhost:8000/registration"

        if (!username || !email || !password || !confirmedPassword) {
            setError((prev) => ({...prev, general: "Please fill all required fields"}))
            return
        }

        if (error.passwordError) {
            setError((prev) => ({...prev, general: "Please fix the errors above"}))
            return
        }

        setLoading(true)
        axios.post(signup_url, {
            username: username,
            password: password,
            confirmedPassword: confirmedPassword,
            email: email,
        })
            .then(response => response.data)
            .then(data => signup_success(data))
            .then(() => login(e))
            .catch((error) => {
                setResponseError(error.response.data.errorlist)
            })
            .finally(() => setLoading(false))
    }

    return (
        <div className={"register"}>
            {responseError && <PopUpContainer messenges={responseError}/>}
            <form onSubmit={submit}>
                <div className={"container"}>
                    <label htmlFor="uname"><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="uname" required
                           onChange={e => setUsername(e.target.value)}/>

                    <label htmlFor="email"><b>Email</b></label>
                    <input type="email" placeholder="Enter Email" name="email" required
                           onChange={e => setEmail(e.target.value)}/>

                    <label htmlFor="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="psw" required
                           onChange={e => setPassword(e.target.value)}/>

                    <label htmlFor="cpsw"><b>Confirm Password</b></label>
                    <input className={error.passwordError ? "error" : "success"} type="password"
                           placeholder="Confirm Password" name="cpsw" required
                           onChange={e => validateForm(e.target.value)}/>

                    {error.passwordError && <p className={"error"}>{error.passwordError}</p>}

                    <button type="submit" disabled={loading}>
                        {loading ? "Signing up..." : "Sign Up"}
                    </button>
                </div>
            </form>
        </div>
    )
}

export default Register
