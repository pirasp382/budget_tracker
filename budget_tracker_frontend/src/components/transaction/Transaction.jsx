import "./Transaction.css"
import {useState} from "react"

const Transaction = ({id, amount, type, category, accountTitle, date, isChecked}) => {

    const [checked, setChecked] = useState(isChecked)

    const checkHandler = () => {
        setChecked(!checked)
    }

    return (
        <div className={"transaction"}>
            <input type={"checkbox"} checked={checked|| isChecked} onChange={checkHandler}/>
            <p>{new Intl.DateTimeFormat("de-DE", {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            }).format(new Date(date))}</p>
            <p className={"category"}>{category}</p>
            {
                type === "EXPENSES" ? <div className={"expenses amount"}>{amount}€</div> :
                    <div className={"income amount"}>{amount}€</div>
            }
            <p>{accountTitle}</p>
        </div>
    )
}

export default Transaction