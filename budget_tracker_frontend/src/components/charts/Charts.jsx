import "./Charts.css"
import LineChart from "../barchart/BarChart"
import DonutChart from "../donutchart/DonutChart"

const Charts = ({incomeData, expenseData, history, categoryData, timeType, setTimeType, timeOptions}) => {


    return (
        <div className="charts">
            <div className="main-chart">
                <LineChart incomeData={incomeData} expensesData={expenseData} history={history} timeType={timeType}
                           setTimetype={setTimeType} timeOptions={timeOptions}/>
            </div>
            <div className="secondary-chart">
                <DonutChart categoryData={categoryData}/>
            </div>
        </div>
    )
}

export default Charts
