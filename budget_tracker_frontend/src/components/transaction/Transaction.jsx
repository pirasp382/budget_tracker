import "./Transaction.css"

const Transaction = ({id, amount, type, category, accountTitle, date, selected, setSelected, openEditModal}) => {
    const isChecked = selected.includes(id)

    const checkHandler = () => {
        setSelected((prevSelected) =>
            prevSelected.includes(id)
                ? prevSelected.filter(item => item !== id)
                : [...prevSelected, id],
        )
    }

    return (
        <div className="transaction">
            <input type="checkbox" checked={isChecked} onChange={checkHandler}/>
            <div className={"inner"} onClick={()=>openEditModal(id)}>
                <p className="date">{new Intl.DateTimeFormat("de-DE", {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                }).format(new Date(date))}</p>
                <p className="category">{category}</p>
                <p className="account">{accountTitle}</p>
                <span className={`amount ${type === "EXPENSES" ? "expenses" : "income"}`}>
                {amount}€
            </span>

            </div>
        </div>
    )
}

export default Transaction
