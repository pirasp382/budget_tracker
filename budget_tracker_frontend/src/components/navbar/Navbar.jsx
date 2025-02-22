import "./Navbar.css"
import {useNavigate} from "react-router-dom"
import {IoIosLogOut} from "react-icons/io"

const Navbar = () => {
    const navigate = useNavigate()

    const logout = () => {
        localStorage.clear()
        navigate("/login")
    }

    return (
        <div className={"navbar-items"}>
            <ul className={"navbar-menu"}>
                <li className={"menu-item"} onClick={() => navigate("/")}>Overview</li>
                <li className={"menu-item"} onClick={() => navigate("/transactions")}>Transactions</li>
                <li className={"menu-item"}>Accounts</li>
                <li className={"menu-item"}>Categories</li>
                <li className={"menu-item"}>Settings</li>
            </ul>
            <div className={"logout"}>
                <IoIosLogOut className={"logout-icon"} onClick={() => logout()}/>
            </div>
        </div>
    )
}

export default Navbar