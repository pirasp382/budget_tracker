import PropTypes from "prop-types"
import {useState} from "react"
import {useNavigate} from "react-router-dom"
import axios from "axios"
import "./Login.css"
import PopUpContainer from "../../components/pop-up/PopUpContainer"

const Login = ({setIsLoggedIn}) => {

    const [username, setUsername] = useState()
    const [password, setPassword] = useState()
    const [error, setError] = useState()
    const [loading, setLoading] = useState()
    const [showPassword, setShowPassword] = useState(false)
    const navigate = useNavigate()

    function login_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        setError("")
        setIsLoggedIn(true)
        navigate("/")
    }

    function login_error(errorlist) {
        setError(errorlist)
    }

    function submit(e) {
        e.preventDefault()
        setLoading(true)
        const login_url = "http://localhost:8000/login"
        axios.post(login_url, {
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
                if (error.response && error.response.status === 401) {
                    setError(error.response.data.errorlist)
                } else {
                    setError(error)
                }
            })
            .finally(() => setLoading(false))
    }

    return (<div className={"login"}>
        {error && <PopUpContainer messenges={error}/> }
        <form onSubmit={submit}>
            <header>
                <div className={"login-title"}>
                    <h2>Log In</h2>
                </div>
                <button type="button" className={"register-button"} onClick={() => navigate("/register")}>Register
                </button>
            </header>

            <div className="container">
                <label htmlFor="uname"><b>Username</b></label>
                <input
                    type="text"
                    placeholder="Enter Username"
                    name="uname"
                    required
                    onChange={(e) => setUsername(e.target.value)}
                />

                <label htmlFor="psw"><b>Password</b></label>
                <div className="password-container">
                    <input
                        type={showPassword ? "text" : "password"} // Dynamischer Typ
                        placeholder="Enter Password"
                        name="psw"
                        required
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button
                        type="button"
                        className="toggle-password"
                        onClick={() => setShowPassword(!showPassword)}
                    >
                        {showPassword ? "Hide" : "Show"}
                    </button>
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? "Logging in..." : "Login"}
                </button>
                <label>
                    <input type="checkbox" name="remember"/> Remember me
                </label>
            </div>
        </form>
    </div>)
}

Login.propTypes = {
    setIsLoggedIn: PropTypes.func,
}

export default Login