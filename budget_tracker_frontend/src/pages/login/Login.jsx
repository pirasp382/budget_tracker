import PropTypes from "prop-types"
import {useState} from "react"
import {useNavigate} from "react-router-dom"
import "./Login.css"
import PopUpContainer from "../../components/pop-up/PopUpContainer"
import {loginAPICall} from "../../services/LoginServices"
import {useDispatch, useSelector} from "react-redux"
import {setErrorList} from "../../redux/actions"

const Login = ({setIsLoggedIn}) => {

    const [username, setUsername] = useState()
    const [password, setPassword] = useState()
    const error = useSelector((state)=>state.errorLists.errorList)
    const [loading, setLoading] = useState()
    const [showPassword, setShowPassword] = useState(false)
    const navigate = useNavigate()
    const dispatch = useDispatch()
    function login_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        localStorage.setItem("accounts", JSON.stringify(data.accountDTOList))
        setIsLoggedIn(true)
        navigate("/")
    }

    function submit(e) {
        e.preventDefault()
        setLoading(true)
        loginAPICall(username, password)
            .then((data) =>
                login_success(data),
            )
            .catch((errorResponse) => {
                if (errorResponse.response && errorResponse.response.status === 401) {
                    dispatch(setErrorList(errorResponse.response.data.errorlist))
                }
            })
            .finally(() => setLoading(false))
    }

    return (<div className={"login"}>
        {error && <PopUpContainer messenges={error}/>}
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
                        type={showPassword ? "text" : "password"}
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
                <button className={"submit"} type="submit" disabled={loading}>
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