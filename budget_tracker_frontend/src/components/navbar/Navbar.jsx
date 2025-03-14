import "./Navbar.css"
import {useNavigate} from "react-router-dom"
import {IoIosLogOut} from "react-icons/io"
import {logout} from "../../services/logoutService"

const Navbar = ({site}) => {
    const navigate = useNavigate()

    return (
        <div className={"navbar-items"}>
            <ul className={"navbar-menu"}>
                <li className={`menu-item ${site === "dashboard" ? "active" : ""}`} onClick={() => navigate("/")}>Overview</li>
                <li className={`menu-item ${site === "transactions" ? "active" : ""}`} onClick={() => navigate("/transactions")}>Transactions</li>
                <li className={`menu-item ${site === "accounts" ? "active" : ""}`} onClick={() => navigate("/accounts")}>Accounts</li>
                <li className={`menu-item ${site === "categories" ? "active" : ""}`} onClick={() => navigate("/categories")}>Categories</li>
                <li className={`menu-item ${site === "settings" ? "active" : ""}`} onClick={() => navigate("/settings")}>Settings</li>
            </ul>
            <div className={"logout"}>
                <IoIosLogOut className={"logout-icon"} onClick={() => logout(navigate)}/>
            </div>
        </div>
    )
}

export default Navbar
