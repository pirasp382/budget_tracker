import "./Account.css"

const Account = ({id, balance, description, title, selected, setSelected, openEditModal}) => {

    const isChecked = selected.includes(id)

    const checkHandler = () => {
        setSelected((prevSelected) =>
            prevSelected.includes(id)
                ? prevSelected.filter(item => item !== id)
                : [...prevSelected, id],
        )
    }

    return (
        <div className={"account"}>
            <input type="checkbox" checked={isChecked} onChange={checkHandler}/>
            <div className={"inner account-inner"} onClick={() => openEditModal(id)}>
                <p className={"name"}>{title}</p>
                <p className={"balance"}>{balance}€</p>
            </div>
        </div>
    )
}

export default Account