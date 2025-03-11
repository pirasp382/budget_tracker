import Header from "../../components/header/Header"
import {useEffect, useState} from "react"
import DataField from "../../components/data-field/DataField"
import "./Dashboard.css"
import Charts from "../../components/charts/Charts"
import {getGraphDataTransactionAPICall, getUserDataAPICall} from "../../services/DashBoardServices"
import {useDispatch, useSelector} from "react-redux"
import PopUpContainer from "../../components/pop-up/PopUpContainer"

const timeOptions = {
    day: <span>Day</span>,
    week: <span>Week</span>,
    month: <span>Month</span>,
    year: <span>Year</span>,
}

const Dashboard = () => {

    const [user, setUser] = useState()
    const [error, setError] = useState()
    const [income, setIcome] = useState()
    const [expenses, setExpenses] = useState()
    const [balance, setBalance] = useState()
    const [incomeData, setIncomeData] = useState([])
    const [expensesData, setExpensesData] = useState([])
    const [categoryData, setCategoryData] = useState([])
    const [history, setHistory] = useState([])
    const [timeType, setTimeType] = useState("day")

    const accountId = useSelector((state) => state.accounts.filter_account)


    useEffect(() => {
        setUser(localStorage.getItem("username"))
        getUserData()
        getGraphDataTransaction()
    }, [])

    useEffect(() => {
        getUserData()
        getGraphDataTransaction()
    }, [accountId, timeType])

    function getUserData() {
        getUserDataAPICall()
            .then(data => {
                setIcome(data.income)
                setExpenses(data.expenses)
                setBalance(data.balance)
            })
            .catch(error => setError(error.response.data.errorlist))
    }

    function getGraphDataTransaction() {
        getGraphDataTransactionAPICall(accountId, timeType)
            .then(data => {
                setIncomeData(data.income)
                setExpensesData(data.expenses)
                setCategoryData(data.categoryData)
                setHistory(data.history)
            })
            .catch(error => setError(error.response.data.errorlist))
    }

    return (
        <div>
            {error && <PopUpContainer messenges={error}/>}
            <Header username={user} site={"dashboard"}/>
            <div className="dashboard-content">
                <DataField title="Balance" value={balance}/>
                <DataField title="Income" value={income}/>
                <DataField title="Expenses" value={expenses}/>
            </div>
            <div className={"plots"}>
                <Charts incomeData={incomeData} expenseData={expensesData} history={history}
                        categoryData={categoryData} timeType={timeType} setTimeType={setTimeType}
                        timeOptions={timeOptions}/>
            </div>
        </div>
    )

}

export default Dashboard