import "./Categories.css"
import {useEffect, useState} from "react"
import Header from "../../components/header/Header"

const Caregories=()=>{
    const [user, setUser] = useState();

    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])

    return(
        <div>
            <Header username={user}/>
            Caregories
        </div>
    )
}

export default Caregories;