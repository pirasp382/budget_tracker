import "./TransactionList.css"
import {LuArrowUpDown} from "react-icons/lu"
import Transaction from "../transaction/Transaction"
import PropTypes from "prop-types"
import {useState} from "react"

const TransactionList = ({transactions}) => {

    const [checked, setChecked] = useState(false)

    const checkHandler = () => {
        setChecked(!checked)
    }

    return (
        <div className={"liste"}>
            <div className={"list-header"}>
                <input type={"checkbox"} checked={checked} onChange={checkHandler}/>
                <p>Date <LuArrowUpDown className={"icon"}/></p>
                <p>Category <LuArrowUpDown className={"icon"}/></p>
                <p>Amount <LuArrowUpDown className={"icon"}/></p>
                <p>Account <LuArrowUpDown className={"icon"}/></p>
            </div>
            {transactions.map(item => <Transaction id={item.id} type={item.type} date={item.date}
                                                   category={item.category} accountTitle={item.accontTitle}
                                                   amount={item.amount} isChecked={checked}/>)}
        </div>
    )
}

TransactionList.propTypes = {
    transactions: PropTypes.array.isRequired,
}

export default TransactionList