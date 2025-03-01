import "./AccountModal.css"
import {AiOutlineCloseCircle} from "react-icons/ai"
import {useEffect, useState} from "react"
import AmountInput from "../amount-input/AmountInput"
import transaction from "../transaction/Transaction"

const AccountModal = ({account, isOpen, addAccount, saveEdits}) => {

    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [accountBalance, setAccountBalance] = useState(null)
    const [type, setType] = useState()
    const [isDisabled, setIsDisabled] = useState(false)

    useEffect(() => {
        if (account) {
            setTitle(account.title)
            setAccountBalance(account.balance)
            setDescription(account.description)
            setIsDisabled(true)
        }
    }, [account])

    console.log(account)

    function handleSave() {
        const id = account.id
        const new_account = {id, title, description}
        saveEdits(new_account)
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        const balance = (type) ? -1 * accountBalance : accountBalance
        const new_account = {balance, title, description, type}
        addAccount(new_account)
    }

    return (
        <div className={"account-form"}>
            <div className={"upper"}>
                <div className={"account-title"}>
                    {account ? <p className={"title"}>Edit Account</p> :
                        <p className={"title"}>New Account</p>}
                    {account ? <p className={"sub-title"}>Edit the Account</p> :
                        <p className={"sub-title"}>Add a new Account</p>}
                </div>
                <div className={"close"} onClick={isOpen}><AiOutlineCloseCircle/></div>
            </div>
            <form className={"form"} onSubmit={handleSubmit}>
                <label>
                    Title <br/>
                    <input type={"text"} value={title}
                           onChange={(event) => setTitle(event.target.value)}/>
                </label>
                <label>
                    <AmountInput key={accountBalance} value={accountBalance} onChange={setAccountBalance}
                                 setType={setType} isDisabled={isDisabled}/>
                </label>

                <label>
                    Notes: <br/>
                    <textarea type="textarea" placeholder={"Optionales Notes"} value={description}
                              onChange={(e) => setDescription(e.target.value)}/>
                </label>
                {account ? <button type="button" onClick={handleSave}>Save changes</button> :
                    <button type="submit">Add a transaction</button>}

            </form>
        </div>
    )
}

export default AccountModal