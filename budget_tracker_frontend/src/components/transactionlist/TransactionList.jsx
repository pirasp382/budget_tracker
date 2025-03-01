import "./TransactionList.css"
import {LuArrowUpDown} from "react-icons/lu"
import Transaction from "../transaction/Transaction"
import PropTypes from "prop-types"
import {useState} from "react"

const TransactionList = ({
                             transactions,
                             getAllTransactions,
                             setSortParam,
                             setDirection,
                             selected,
                             setSelected,
                             openEditModal,
                         }) => {
    const [sortConfig, setSortConfig] = useState({key: "date", ascending: true})

    const allSelected = selected.length === transactions.length && transactions.length > 0

    const checkHandler = () => {
        if (allSelected) {
            setSelected([])
        } else {
            setSelected(transactions.map(t => t.id))
        }
    }

    const handleSort = (key) => {
        setSortConfig(prev => {
            const isAscending = prev.key === key ? !prev.ascending : true
            setSortParam(key)
            setDirection(isAscending)
            getAllTransactions(key, isAscending)
            return {key, ascending: isAscending}
        })
    }

    return (
        <div className={"liste"}>
            <div className={"list-header"}>
                <input type={"checkbox"} checked={allSelected} onChange={checkHandler}/>
                {["date", "category", "account", "amount"].map((key) => (
                    <p key={key} className={"sort"} onClick={() => handleSort(key)}>
                        {key.charAt(0).toUpperCase() + key.slice(1)}
                        <LuArrowUpDown
                            className={`icon ${sortConfig.key === key ? (sortConfig.ascending ? "asc" : "desc") : ""}`}/>
                    </p>
                ))}
                <p></p>
            </div>
            {transactions.map(item => (
                <Transaction
                    key={item.id}
                    id={item.id}
                    type={item.type}
                    date={item.date}
                    category={item.category}
                    accountTitle={item.accountTitle}
                    amount={item.amount}
                    selected={selected}
                    setSelected={setSelected}
                    openEditModal={openEditModal}
                />
            ))}
        </div>
    )
}

TransactionList.propTypes = {
    transactions: PropTypes.array.isRequired,
    getAllTransactions: PropTypes.func.isRequired,
}

export default TransactionList
