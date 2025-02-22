import "./Account.css"
import {useEffect, useState} from "react"
import Header from "../../components/header/Header"

const Accounts = () => {

    const [user, setUser] = useState()

    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])

    return (
        <div>
            <Header username={user}/>
            Accounts
        </div>
    )
}

export default Accounts