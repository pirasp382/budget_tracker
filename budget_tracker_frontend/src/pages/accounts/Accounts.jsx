import "./Accounts.css"
import {useEffect, useState} from "react"
import Header from "../../components/header/Header"
import PopUpContainer from "../../components/pop-up/PopUpContainer"
import AccountList from "../../components/accountlist/AccountList"
import axios from "axios"
import {FiPlus} from "react-icons/fi"
import AccountModal from "../../components/accountmodal/AccountModal"
import BACKEND_ADDRESS_URL from "../../services/backendAddress"

const Accounts = () => {

    const [user, setUser] = useState()
    const [error, setError] = useState([])
    const [accounts, setAccounts] = useState([])
    const [account, setAccount] = useState()
    const [showModal, setShowModal] = useState(false)
    const [sortParam, setSortParam] = useState("title")
    const [direction, setDirection] = useState(false)
    const [selected, setSelected] = useState([])

    useEffect(() => {
        setUser(localStorage.getItem("username"))
        getAllAccounts(sortParam, direction)
    }, [])

    function openEditModal(id) {
        setAccount(accounts.find(item => item.id === id))
        setShowModal(true)
    }

    function getAllAccounts(sortParam, direction) {
        const url = `${BACKEND_ADDRESS_URL}/account?sortParam=${sortParam}&direction=${direction}`
        const token = localStorage.getItem("jwt")
        axios.get(url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => setAccounts(data))
            .catch(error => console.log(error.response.data[0]))
    }

    function deleteAccounts() {
        const url = `${BACKEND_ADDRESS_URL}/account`
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
                getAllAccounts(sortParam, direction)
            })
            .catch(error => console.log(error))
    }

    function saveEdits(newAccount) {
        const url = `${BACKEND_ADDRESS_URL}/account/${newAccount.id}`
        const token = localStorage.getItem("jwt")
        axios.put(url, {
            ...newAccount,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(() => setShowModal(false))
            .then(() => getAllAccounts(sortParam, direction))
            .catch(error => console.log(error))
    }

    function addAccount(account) {
        const url = `${BACKEND_ADDRESS_URL}/account`
        const token = localStorage.getItem("jwt")
        axios.post(url, {
            ...account,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(() => setShowModal(false))
            .then(() => getAllAccounts(sortParam, direction))
            .catch(error => console.log(error))
    }

    return (
        <div className={"accounts"}>
            {error && <PopUpContainer messenges={error}/>}
            {showModal && <AccountModal isOpen={() => setShowModal(false)} addAccount={addAccount} account={account}
                                        saveEdits={saveEdits}/>}
            <Header username={user} site={"accounts"}/>
            <div className={"account-content"}>
                <div className={"head"}>
                    <div>
                        Accounts
                    </div>
                    <div className={"buttons"}>
                        <div className={"add-buttons"}>
                            <button onClick={() => {
                                setShowModal(true)
                                setAccount(null)
                            }}><FiPlus/> Add new
                            </button>
                        </div>
                        <div className={"delete-button"}>
                            {selected.length > 0 &&
                                <button onClick={() => deleteAccounts()}>Delete ({selected.length})</button>}
                        </div>
                    </div>
                </div>
                <div className={"table-content"}>
                    <AccountList accounts={accounts} setSelected={setSelected} selected={selected}
                                 setSortParam={setSortParam} setDirection={setDirection}
                                 getAllAccounts={getAllAccounts} openEditModal={openEditModal}/>
                </div>
            </div>
        </div>
    )
}

export default Accounts