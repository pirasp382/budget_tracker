import Header from "../../components/header/Header"
import {useEffect, useState} from "react"
import "./Transactions.css"
import axios from "axios"
import {FiPlus, FiUpload} from "react-icons/fi"
import TransactionList from "../../components/transactionlist/TransactionList"
import PopUpContainer from "../../components/pop-up/PopUpContainer"
import SideModal from "../../components/side-modal/SideModal"
import BACKEND_ADDRESS_URL from "../../services/backendAddress"

const Transactions = () => {
    const [user, setUser] = useState()
    const [transactions, setTransactions] = useState([])
    const [error, setError] = useState([])
    const [showModal, setShowModal] = useState(false)
    const [accounts, setAccounts] = useState([])
    const [categories, setCategories] = useState([])
    const [sortParam, setSortParam] = useState("date")
    const [direction, setDirection] = useState(false)
    const [selected, setSelected] = useState([])
    const [transaction, setTransaction] = useState()

    useEffect(() => {
        getAllTransactions("date", false)
    }, [])

    function openEditModal(id) {
        setTransaction(transactions.find(item => item.id === id))
        setShowModal(true)
    }

    useEffect(() => {
        setUser(localStorage.getItem("username"))
        getAccounts()
        getCategories()
    }, [])

    function getCategories() {
        const url = `${BACKEND_ADDRESS_URL}/category`
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => setCategories(data))
            .catch(error => setError(error.response.data.errorlist))
    }

    function saveEdits(transaction) {
        const url = `${BACKEND_ADDRESS_URL}/transaction/${transaction.id}`
        const token = localStorage.getItem("jwt")
        axios.put(url, {
            ...transaction,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(() => getAllTransactions(sortParam, direction))
            .catch(error => setError(error.response.data.errorlist))
    }

    function deleteTransactions() {
        const url = `${BACKEND_ADDRESS_URL}/transaction`
        const token = localStorage.getItem("jwt")
        axios.delete(url, {
            data: selected,
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(() => {
                setSelected([])
                getAllTransactions(sortParam, direction)
            })
            .catch(error => console.log(error))
    }

    function addNewTransaction(transaction, account_id) {
        const url = `${BACKEND_ADDRESS_URL}/transaction?accountId=${account_id}`
        const token = localStorage.getItem("jwt")
        axios.post(url, {
            ...transaction,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => localStorage.setItem("jwt", data.token))
            .then(() => getAllTransactions(sortParam, direction))
            .catch(error => setError(error.response.data.errorlist))
    }

    function getAccounts() {
        const url = `${BACKEND_ADDRESS_URL}/account`
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => setAccounts(data))
    }

    function getAllTransactions(sortParam, direction) {
        const url = `${BACKEND_ADDRESS_URL}/transaction?sortParam=${sortParam}&direction=${direction}`
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => setTransactions(data))
            .catch(error => setError(error.response.data.errorlist))
    }

    return (
        <div className={"transactions"}>
            {error && <PopUpContainer messenges={error}/>}
            {showModal &&
                <SideModal addTransaction={addNewTransaction} isOpen={() => setShowModal(false)} accounts={accounts}
                           categories={categories} transaction={transaction} saveChanges={saveEdits}/>}
            <Header username={user} site={"transactions"}/>
            <div className={"transaction-content"}>
                <div className={"head"}>
                    <div>
                        Transaction History
                    </div>
                    <div className={"buttons"}>
                        <div className={"add-buttons"}>
                            <button onClick={() => {
                                setShowModal(true)
                                setTransaction(null)
                            }}><FiPlus/> Add new
                            </button>
                            <button><FiUpload/> Import</button>
                        </div>
                        <div className={"delete-button"}>
                            {selected.length > 0 &&
                                <button onClick={() => deleteTransactions()}>Delete ({selected.length})</button>}
                        </div>
                    </div>
                </div>
                <div className={"table"}>
                    <TransactionList setDirection={setDirection} setSortParam={setSortParam} selected={selected}
                                     setSelected={setSelected} openEditModal={openEditModal}
                                     transactions={transactions} getAllTransactions={getAllTransactions}/>
                </div>
            </div>
        </div>
    )
}

export default Transactions