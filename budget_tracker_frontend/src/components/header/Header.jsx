import "./Header.css"
import Logo from "../logo/Logo"
import PropTypes from "prop-types"
import Navbar from "../navbar/Navbar"

const Header = ({username}) => {
    return (
        <div className={"header"}>
            <div className={"navbar"}>
                <Logo/>
                <Navbar/>
            </div>
            <div className={"title"}>
                <div className={"greetings"}>
                    Welcome Back, {username} 👋
                </div>
                <div className={"sub-title"}>
                    This is your financial Overiew Report
                </div>
            </div>
        </div>
    )
}

Header.propTypes = {
    username: PropTypes.string,
}

export default Header