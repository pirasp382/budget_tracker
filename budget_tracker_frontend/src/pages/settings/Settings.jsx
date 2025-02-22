import "./Settings.css"
import {useEffect, useState} from "react"
import Header from "../../components/header/Header"

const Settings = () => {
    const [user, setUser] = useState()

    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])

    return (
        <div>
            <Header username={user}/>
            Settings
        </div>
    )
}

export default Settings