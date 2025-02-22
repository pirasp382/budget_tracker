import Header from "../../components/header/Header"
import {useEffect, useState} from "react"

const Dashboard=()=>{

    const [user, setUser] = useState();

    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])

    return (
       <div>
           <Header username={user}/>
           <p>Dashboard</p>
       </div>
    )
}

export default Dashboard;