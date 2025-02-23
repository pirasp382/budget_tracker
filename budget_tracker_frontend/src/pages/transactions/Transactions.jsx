import Header from "../../components/header/Header"
import {useEffect, useState} from "react"
import "./Transactions.css"
import axios from "axios"
import TransactionList from "../../components/transactionlist/TransactionList"

const Transactions = () => {
    const [user, setUser] = useState()
    const [transactions, setTransactions] = useState([])

    useEffect(() => {
        getAllTransactions()
    }, [])


    useEffect(() => {
        setUser(localStorage.getItem("username"))
    }, [])

    function getAllTransactions() {
        const url = "http://localhost:8000/transaction"
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => {
                console.log(data)
                return data
            })
            .then(data => setTransactions(data))
            .catch(error => console.log(error))
    }

    return (
        <div className={"transactions"}>
            <Header username={user}/>
            <div className={"transaction-content"}>
                <div className={"head"}>
                    <div>
                        Transaction History
                    </div>
                    <div className={"buttons"}>
                        <button >hello</button>
                    </div>
                </div>
                <div className={"table"}>
                    {<TransactionList transactions={transactions}/>}
                </div>
            </div>
        </div>
    )
}

export default Transactions