import {useEffect, useState} from "react"
import "./SideModal.css"
import AmountInput from "../amount-input/AmountInput"
import {AiOutlineCloseCircle} from "react-icons/ai"

const heute = () => new Date().toISOString().split("T")[0]

const convertType = (type) => type ? "EXPENSES" : "INCOME"
const convertToNegative = (number, type) => type ? (-1 * number) : number

export default function SideModal({isOpen, accounts = [], categories = [], addTransaction, transaction, saveChanges}) {
    const [date, setDate] = useState(heute)
    const [account, setAccount] = useState()
    const [category, setCategory] = useState("")
    const [tamount, setTamount] = useState(null)
    const [description, setDescription] = useState("")
    const [ttype, setTtype] = useState()

    const getIdByName = (title) => accounts.find(item => item.title === title).id
    const getAccountByName = (title) => accounts.find(item => item.title === title)

    useEffect(() => {
        if (transaction) {
            setDate(transaction.date)
            setDescription(transaction.description)
            setTtype(transaction.type)
            setTamount(transaction.amount)
            setCategory(transaction.category)
            const name = getAccountByName(transaction.accountTitle)
            setAccount(name ? name.title : "")

        }
    }, [transaction])

    function handleChanges() {
        const accountTitle = account
        const type = convertType(ttype)
        const amount = convertToNegative(Math.abs(tamount), ttype)
        const id = transaction.id
        const new_transaction = {amount, category, type, description, date, accountTitle, id}
        saveChanges(new_transaction)
    }


    const handleSubmit = (e) => {
        e.preventDefault()
        const account_id = getIdByName(account)
        const type = convertType(ttype)
        const amount = convertToNegative(Math.abs(tamount), ttype)
        const transaction = {amount, category, type, description, date}
        addTransaction(transaction, account_id)

        setDate(heute())
        setAccount("")
        setCategory("")
        setTamount("")
        setDescription("")
        setTtype(null)
    }

    return (
        <div className="transaction-form">
            <div className={"upper"}>
                <div className={"transaction-title"}>
                    {transaction ? <p className={"title"}>Edit Transaction</p> :
                        <p className={"title"}>Add Transaction</p>}
                    {transaction ? <p className={"sub-title"}>Edit the Transaction</p> :
                        <p className={"sub-title"}>Add a new Transaction</p>}
                </div>
                <div className={"close"} onClick={isOpen}><AiOutlineCloseCircle/></div>
            </div>

            <form className={"form"} onSubmit={handleSubmit}>
                <label>
                    Date: <br/>
                    <input type="date" value={date}
                           onChange={(event) => setDate(event.target.value)}/>
                </label>

                <label>
                    Account: <br/>
                    <select className={"account-option"} aria-placeholder={"hello"} value={account}
                            onChange={(e) => setAccount(e.target.value)}
                            required>
                        <option value="" disabled selected>Select your Account</option>
                        {accounts.map(item => <option
                            className={"account-option"}>{item.title}</option>)}
                    </select>
                </label>

                <label>
                    Category: <br/>
                    <select className={"account-option"} value={category} onChange={(e) => setCategory(e.target.value)}
                            required>
                        <option value="" disabled selected>Select your Category</option>
                        {categories.map(item => <option className={"account-option"}>{item.title}</option>)}
                    </select>
                </label>

                <label>
                    <AmountInput key={tamount} value={tamount} onChange={setTamount} setType={setTtype}/>
                </label>

                <label>
                    Notes: <br/>
                    <textarea type="textarea" placeholder={"Optionales Notes"} value={description}
                              onChange={(e) => setDescription(e.target.value)}/>
                </label>

                {transaction ? <button type="button" onClick={handleChanges}>Save changes</button> :
                    <button type="submit">Add a transaction</button>}
            </form>
        </div>
    )
}