import {LuArrowUpDown} from "react-icons/lu"
import {useState} from "react"
import Account from "../account/Account"
import "./AccountList.css"

const AccountList = ({
                         accounts,
                         setSortParam,
                         setDirection,
                         selected,
                         setSelected,
                         getAllAccounts,
                         openEditModal,
                     }) => {

    const [sortConfig, setSortConfig] = useState({key: "date", ascending: true})
    const allSelected = selected.length === accounts.length && accounts.length > 0

    const checkHandler = () => {
        if (allSelected) {
            setSelected([])
        } else {
            setSelected(accounts.map(t => t.id))
        }
    }

    const handleSort = (key) => {
        setSortConfig(prev => {
            const isAscending = prev.key === key ? !prev.ascending : true
            setSortParam(key)
            setDirection(isAscending)
            getAllAccounts(key, isAscending)
            return {key, ascending: isAscending}
        })
    }

    return (
        <div className={"liste "}>
            <div className={"list-header account-liste-header"}>
                <input type={"checkbox"} checked={allSelected} onChange={checkHandler}/>
                {["title", "balance"].map((key) => (
                    <p key={key} className={"sort"} onClick={() => handleSort(key)}>
                        {key.charAt(0).toUpperCase() + key.slice(1)}
                        <LuArrowUpDown
                            className={`icon ${sortConfig.key === key ? (sortConfig.ascending ? "asc" : "desc") : ""}`}/>
                    </p>
                ))}
                <p></p>
            </div>
            {accounts.map(item => (
                <Account id={item.id} title={item.title} balance={item.balance} description={item.description}
                         selected={selected} setSelected={setSelected} openEditModal={openEditModal}/>
            ))}
        </div>
    )
}

export default AccountList