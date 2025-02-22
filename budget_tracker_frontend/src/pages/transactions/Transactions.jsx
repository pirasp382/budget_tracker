import Header from "../../components/header/Header"
import {useEffect, useState} from "react"

const Transactions = () => {
    const [user, setUser] = useState();

    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])
    return (
        <div>
            <Header username={user}/>
            <p>Transactions</p>
        </div>
    )
}

export default Transactions