import "./Header.css"
import Logo from "../logo/Logo"
import PropTypes from "prop-types"
import Navbar from "../navbar/Navbar"
import {useDispatch, useSelector} from "react-redux"
import {useEffect, useState} from "react"
import {setFilterAccount} from "../../redux/actions"
import "react-datepicker/dist/react-datepicker.css"

const today = () => new Date()

const Header = ({username, site}) => {

    const dispatch = useDispatch()
    const [accounts, setAccounts] = useState([])
    const filter = useSelector((state) => state.accounts.filter_account)

    const handleFilteredAccount = (e) => {
        dispatch(setFilterAccount(e))
    }

    const resetFilter = () => {
        dispatch(setFilterAccount(""))
    }

    useEffect(() => {
        setAccounts(JSON.parse(localStorage.getItem("accounts")))
    }, [])

    return (
        <div className={"header"}>
            <div className={"navbar"}>
                <Logo/>
                <Navbar site={site}/>
            </div>
            <div className={"title"}>
                <div className={"greetings"}>
                    Welcome Back, {username} 👋
                </div>
                <div className={"sub-title"}>
                    This is your financial Overview Report
                </div>
                <div className={"account-list-header"}>
                    <div>
                        <select value={filter} onChange={event => {
                            handleFilteredAccount(event.target.value)
                        }}>
                            <option value="" disabled selected>Select your Account</option>
                            {accounts.map(item => (
                                <option value={item.id}>{item.title}</option>))}
                        </select>
                        <div className={"buttons"}>
                            <button onClick={resetFilter}>reset filter</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

Header.propTypes = {
    username: PropTypes.string,
    site: PropTypes.string,
}

export default Header
